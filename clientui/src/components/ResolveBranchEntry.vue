<template>
  <div id="resolve-branch-entry" class="container">

    <!-- Modal -->
    <div class="modal fade" id="treeModal" tabindex="-1" role="dialog" aria-labelledby="treeTitle" aria-hidden="true">
      <div class="modal-dialog modal-dialog-scrollable" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="treeTitle">NCIt Tree</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <v-jstree
              :data="asyncData"
              :async= "loadData"
              show-checkbox
              multiple:false
              @item-click="itemClick">
            </v-jstree>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary" data-dismiss="modal"  v-on:click="userSelectTreeBranchNode">Select</button>
          </div>
        </div>
      </div>
    </div>

    <!-- WIZARD DECLARATION -->
    <form-wizard
      @on-complete="onComplete"
      step-size="xs"
      title="Resolved Branch Export"
      subtitle="Steps to select a top node, its properties and export the results"
      finish-button-text="Export"
      color="#017ebe">

      <!-- STEP 1: SELECT CODES -->
      <tab-content icon="ti-settings" title="Select a Code"
        :before-change="validateFirstStep">
        <div class="container">
              <div class="row justify-content-center">
                 <div class="col-12 col-md-8">

                   <label for="tags">Select one NCI Thesaurus top node code or enter your own</label>
                   <div class="row">
                      <div class="col-md-12">
                          <form class="row form-group">
                              <div class="col-12 col-sm pr-sm-0">
                                  <tags-input element-id="tags"
                                    v-model="selectedTags"
                                    :existing-tags=this.curratedTopNodesUI
                                    :typeahead="true"
                                    :typeahead-always-show="false"
                                    :typeahead-hide-discard="true"
                                    :add-tags-on-comma="true"
                                    :add-tags-on-space="true"
                                    :limit=1
                                    :typeahead-activation-threshold=0
                                    :hide-input-on-limit="true"
                                    :case-sensitive-tags="true"
                                    placeholder="Add Top Node"
                                    typeahead-style="dropdown"
                                    @tag-added="value =>onTagAdded(value)">
                                  </tags-input>
                              </div>
                              <div class="col-12 col-sm-auto pl-sm-0">
                                <button type="button" class="btn btn-primary btn-md float-right treeViewButton" data-toggle="modal" data-target="#treeModal">
                                  Tree View
                                </button>
                              </div>
                          </form>
                      </div>
                  </div>
                  <div class="row">
                        <div class="col-md-12">
                          <div class="form-group">
                                <label for="levelSelection">Select how many levels to retrieve</label>
                                <select v-model="selectedLevel" id="levelSelection" class="form-control">
                                  <option v-for="level in levels"
                                    :value="level.id"
                                    :key="level.name">
                                    {{ level.name }}
                                  </option>
                                </select>
                            </div>
                        </div>
                    </div>
                 </div>
              </div>

        </div>
      </tab-content>

      <!-- STEP 2: SELECT PROPERTIES -->
      <tab-content icon="ti-view-list-alt" title="Select Properties"
        :before-change="validatePropertyStep">

        <div class="container">
          <form>
            <div class="form-group">
              <label for="selectedProperties">Select properties to include in the export</label>
            </div>
            <div class="form-group">
              <v-multiselect-listbox  v-model="selectedProperties" :options="this.availableProperties"
                  :reduce-display-property="(option) => option.name"
                  :reduce-value-property="(option) => option.code"
                  search-input-class="custom-input-class"
                  search-options-placeholder="Search properties"
                  selected-options-placeholder="Search selected properties">
              </v-multiselect-listbox>
            </div>
          </form>
        </div>
       </tab-content>

       <!-- STEP 3: SELECT DOWNLOAD FORMAT AND DOWNLOAD -->
       <tab-content icon="ti-download" title="Select Format and Export"
          :before-change="validateExportStep">
         <div class="container">
             <div class="row justify-content-center">
                <div class="col-12 col-md-6">
                 <form ref="formContainer">
                   <div class="form-group">
                     <label for="downloadFormat">Select format for export</label>

                     <select v-model="userSelectedFormat"
                          id="downloadFormat"
                          class="form-control">
                       <option
                          v-for="availableFormat in availableFormats"
                          :value="availableFormat"
                          :key="availableFormat.name">
                          {{ availableFormat.name + ' (' +
                             availableFormat.extension + ')  ' +
                             availableFormat.description }}
                        </option>
                     </select>
                    </div>
                 </form>
              </div>
            </div>
         </div>
       </tab-content>
    </form-wizard>


    <!-- Summary Information -->
    <div id="accordion" class="pb-3 pt-3">
      <div class="card">
        <div class="card-header" id="headingOne" style="
              padding-left: 1px;
              padding-right: 1px;
              padding-bottom: 1px;
              padding-top: 1px;
          ">
            <center>
            <button class="btn btn-link"  v-on:click="this.updateShowSummary" data-toggle="collapse" data-target="#collapseSummary" aria-expanded="true" aria-controls="collapseSummary">
              {{this.showSummaryText}}
            </button>
          </center>
        </div>

        <div id="collapseSummary" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
          <div class="card-body pb-1">
            <div class="row p-1">
              <div class="col-sm-4">
                <div class="card bg-light border-dark mb-3">
                  <div class="card-header">Selected Top Node and Levels <span class="badge badge-secondary">{{ selectedLevel + 1 }}</span></div>
                  <div class="card-body">
                    <ul class="list-group" id="selectedTagList">
                      <li v-for="selectedTag in selectedTags" :key="selectedTag.key">
                        {{ selectedTag.value }}
                      </li>
                      <li>
                        Levels to Export: {{ selectedLevel + 1 }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="col-sm-4">
                <div class="card bg-light border-dark mb-3">
                  <div class="card-header">Selected Properties <span class="badge badge-secondary">{{Object.keys(this.selectedProperties).length}}</span></div>
                  <div class="card-body">

                    <ul class="list-group" id="selectedPropertyList">
                      <li v-for="selectedProperty in selectedProperties" :key="selectedProperty.code">
                        {{ selectedProperty.name }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="col-sm-4">
                <div class="card bg-light border-dark mb-3">
                  <div class="card-header">Selected Export Format</div>
                  <div class="card-body">
                    <ul class="list-group" id="selectedPropertyList">
                      <li>
                        {{
                            userSelectedFormat.length !== 0 ?
                              userSelectedFormat.name + ' (' +
                              userSelectedFormat.extension + ')  ' +
                              userSelectedFormat.description
                            : 'None'
                        }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

 </div>
</template>

<script>

// Custom input tags
import VoerroTagsInput from '@voerro/vue-tagsinput'
import vMultiselectListbox from 'vue-multiselect-listbox'
import api from '../api.js'
import axios from 'axios'
import {FormWizard, TabContent} from 'vue-form-wizard'
import 'vue-loading-overlay/dist/vue-loading.css'
import VJstree from 'vue-jstree'

export default {
  name: 'resolve-branch-entry',
  props: {
    msg: String
  },
  components: {
    'tags-input': VoerroTagsInput,
    'vMultiselectListbox': vMultiselectListbox,
    //'v-select': vSelect,
    FormWizard,
    TabContent,
   'v-jstree': VJstree
  },
  data(){
    return {
      selectedTags: [],
      message: 'Hello World!',
      userEnteredCodes: [],
      entityList: [],
      availableProperties: [],
      selectedProperties: [],
      userSelectedProperyNames: [],
      availableFormats: [],
      userSelectedFormat: {"name":"JSON","description":"JavaScript Object Notation Format","extension":"json"},
      curratedTopNodes: [],
      userSelectedTopNode: '',
      filename: 'branch',
      downloadReturnCode: null,
      curratedTopNodesUI:[],
      getPropertyError: false,
      selectedLevel: 0,
      levels:[
        { id: 0, name: '1 Level' },
        { id: 1, name: '2 Levels' },
        { id: 2, name: '3 Levels' },
        { id: 3, name: '4 Levels' },
        { id: 4, name: '5 Levels' },
        { id: 5, name: '6 Levels' },
        { id: 6, name: '7 Levels' },
        { id: 7, name: '8 Levels' },
        { id: 8, name: '9 Levels' },
        { id: 9, name: '10 Levels' },
      ],

      showTree: true,
      asyncData: [],
      treeSelectedCode: null,
      showSummary: true,
      showSummaryText: '',

      // function to get tree data
      loadData: function (oriNode, resolve) {
          // set id to the node to retrieve children for.
          // set to null to indicate this is the root.
          var id = oriNode.data.id ? oriNode.data.id : null
          var data = []
          console.log('id: ' + id)

          // if id is null, this is the root.  get all root children
          if (id == null) {
            api.getRoots(this.$baseURL)
            .then((children)=>{
              if (children != null) {

                for (let x=0; x < children.length; x++){
                  //console.log(children[x].code + '  :  ' + children[x].name)
                  data.push(
                    {
                      "id": children[x].code,
                      "text": children[x].code + ' : ' + children[x].name,
                      "isLeaf": false,
                      "disabled": children[x].leaf,
                    },
                  )
                }
                resolve(data)
              }
              else {
                  console.log("Error retrieving roots");
                  data.push(
                    {
                      "id": '0',
                      "text": 'Error retrieving tree',
                      "isLeaf": true,
                    },
                  )
                  resolve(data)
                }
              })
          }

          // Id was not null, get the children
          else {
            api.getChildren(this.$baseURL, id)
            .then((children)=>{
              if (children != null) {
                for (let x=0; x < children.length; x++){
                  //console.log(children[x].code + '  :  ' + children[x].name)
                  data.push(
                    {
                      "id": children[x].code,
                      "text": children[x].code + ' : ' + children[x].name,
                      "isLeaf": children[x].leaf,
                      "disabled": children[x].leaf,
                    },
                  )
                }
                resolve(data)
              }
              else {
                console.log("Error retrieving children");
                data.push(
                  {
                    "id": '0',
                    "text": 'Error retrieving tree',
                     "isLeaf": true,
                  },
                )
                resolve(data)
              }
            })
          }
      },

    }
  },

  methods: {

      // Tree dialog user chose a tree node
      userSelectTreeBranchNode() {
        console.log('userSelectTreeBranchNode - user selected:' + this.treeSelectedCode)

        this.selectedTags = [
          { key: this.treeSelectedCode, value: this.treeSelectedCode },
        ]
      },

      // tree item clicked
      itemClick (node) {
        console.log(node.model.id + ' clicked !')
        this.treeSelectedCode = node.model.id;
      },

      // Wizard methods
      validateFirstStep() {
        // make sure the user has a code entered
        return Object.keys(this.selectedTags).length>0
      },

      validatePropertyStep() {
        // make sure the user has selected at least one property
        return Object.keys(this.selectedProperties).length>0
      },

      validateExportStep() {
        // make sure there is an export format selected.
        return this.userSelectedFormat !== null
      },

      onComplete: function() {
        this.downloadFile();
      },

      // Toggle the Show/Hide Selection Summary title
      updateShowSummary() {
        this.showSummaryText = this.showSummary? 'Hide Selection Summary' : 'Show Selection Summary'
        this.showSummary = !this.showSummary;
      },

      setCurratedTags() {
        this.curratedTopNodesUI = []
        //console.log ("length: " + Object.keys(this.curratedTopNodes).length);

        // loop through the curratedTopNodes and create another object array
        // in the form the the tags input widget requires:
        // [{ key: 'someKey', value: 'someValue' }];
        for (let i = 0; i < Object.keys(this.curratedTopNodes).length; i++) {
          //console.log ("key " + this.curratedTopNodes[i].code + "  value " + this.curratedTopNodes[i].name)
          this.curratedTopNodesUI.push({"key":this.curratedTopNodes[i].code, "value":this.curratedTopNodes[i].code + ":" +this.curratedTopNodes[i].name})
        }
      },

      onTagAdded(newCode) {
        console.log("Added tag: " + newCode)
        // When a top node is entered/selected, verify it.
        this.getEntities();
      },

      setSelectedTags() {
        // clear the internal user codes that are entered
        this.userEnteredCodes = []

        for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
          // currated top nodes (from the server hava a value of "C12434:Blood")
          // so we need to strip off everything from the : to the right.
          this.userEnteredCodes.push(this.selectedTags[i].value.split(":",1))
        }
      },

      setSelectedPropertyNames() {
        this.userSelectedProperyNames = []

        for (let i = 0; i < Object.keys(this.selectedProperties).length; i++) {
          this.userSelectedProperyNames.push(this.selectedProperties[i].name)
        }
      },

      // Update the top node that was entered with the description.
      // User enters "C12434", the updated value displayed will be "C12434:Blood".
      // If entered value is not valid, remove it and display an error message.
      updateSelectedTopNodeDescription(topNode){
        this.selectedTags[0].key = topNode[0].code;
        this.selectedTags[0].value = topNode[0].code + ":" + topNode[0].name;

        console.log ("Updating top node: " + topNode[0].code + "  " + topNode[0].name)
        for (let i = 0; i < Object.keys(this.curratedTopNodesUI).length; i++) {
          console.log ("key " + this.curratedTopNodesUI[i].key + "  value " + this.curratedTopNodesUI[i].value)
        }
      },

      updateCurratedTopNodes (topNode) {
        this.userSelectedTopNode = topNode;
      },

      getEntities(){
        // clear the entry list
        this.entityList = []
        this.setSelectedTags()

        //console.log(this.selectedTags[0].key +" --- " + this.selectedTags[0].value)
        api.getCodes(this.$baseURL, this.userEnteredCodes)
          .then((data)=>{

            if (data != null) {
              this.entityList = data;
              this.updateSelectedTopNodeDescription(data);
            }
            else {
              console.log("Error retrieving top node code");
              //alert("Invalid Top Node");

              this.$notify({
                group: 'app',
                title: 'Invalid Top Node',
                text: 'The top node code <b>' + this.userEnteredCodes[0] +'</b> is not valid. It has been removed.',
                type: 'error',
                duration: 4000,
                position: 'bottom left'
              });

              this.selectedTags = [];
              this.getPropertyError=true;
            }
          })
      },

      downloadFile() {
        this.$notify({
          group: 'download',
          title: 'Export in Progress',
          text: 'Your export is running.  Please wait.',
          type: 'success',
          duration: 2000,
          position: "bottom left"
        });

        // show the busy indicator
        let loader = this.$loading.show({
            container: this.$refs.formContainer,
            loader: 'dots',
            isFullPage: false,
          });

        // set the user selected tags and properties
        this.setSelectedTags()
        this.setSelectedPropertyNames()

          axios({
                url: this.$baseURL + 'download/get-file-for-resolved-branch/'  +
                    this.userEnteredCodes + '/' +
                    this.userSelectedProperyNames + '/' +
                    this.selectedLevel + '/' +
                    this.userSelectedFormat.name + '/' +
                    this.filename + '.' + this.userSelectedFormat.extension,
                method: 'GET',
                responseType: 'blob',
            }).then((response) => {
                  var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                  var fileLink = document.createElement('a');

                  fileLink.href = fileURL;
                  fileLink.setAttribute('download', this.filename + '.' + this.userSelectedFormat.extension);
                  document.body.appendChild(fileLink);
                  fileLink.click();
              }).catch(function(error) {
                  console.error("Download Error: " + error);
                  alert("Error Downloading file");
              }).finally(function() { loader.hide()});
      },


      getRoots(){
        api.getRoots(this.$baseURL)
        .then((data)=>{
          if (data != null) {
            console.log("got roots : " + data);
        }
        else {
            console.log("Error retrieving roots");
          }
        })
      },

      getChildren(){
        api.getChildren(this.$baseURL, this.userEnteredCodes)
        .then((data)=>{
          if (data != null) {
            console.log("got children : " + data);
        }
        else {
            console.log("Error retrieving children");
          }
        })
      },
  },
    created() {
      this.updateShowSummary();

      // load properties after the page is loaded.
      api.getProperties(this.$baseURL)
          .then((data)=>{this.availableProperties = data;
        })

      // load the download formats.
      api.getFormats(this.$baseURL)
          .then((data)=>{this.availableFormats = data;
       })

      // get the currated tags from the server,
      // then set the input field with these
      api.getCuratedTopNodes(this.$baseURL)
          .then((data)=>{
            this.curratedTopNodes = data;
            this.setCurratedTags();
       })
    }
  }
</script>

<!-- styling for the component -->
<style>

/* Summary list box formatting */
.list-group{
    max-height: 150px;
    min-height: 150px;
    overflow-y:auto;
}
/* Typeahead elements style/theme
   for tags-input... override the defaults.        */
.tags-input-typeahead-item-default {
    color: black;
    background-color: whitesmoke;
}
.tags-input-root {
  line-height: 1.0;
}
/* .form-group {
    margin-bottom: 0rem;
} */
.modal-active{
	display:block;
}
.msl-multi-select {
  /* make the multi-select take up the entire width of the container */
  width: 100%
}
.treeViewButton {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}
.btn-primary, .btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open>.dropdown-toggle.btn-primary {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}
</style>
