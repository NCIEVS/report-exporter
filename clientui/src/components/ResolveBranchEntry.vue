<template>
  <div id="resolve-branch-entry" class="container" ref="formContainer">

    <!-- Modal -->
    <div class="modal fade" id="treeModal" style="display:none" tabindex="-1" role="dialog" aria-labelledby="treeTitle" aria-hidden="true">
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
            <button type="button" class="btn btn-primary" data-dismiss="modal" v-on:click="userSelectTreeBranchNode">Select</button>
          </div>
        </div>
      </div>
    </div>

    <!-- WIZARD DECLARATION -->
    <form-wizard
        @on-complete="onComplete"
        step-size="xs"
        title="Resolved Branch Export"
        subtitle="Steps to select a top node, then its properties and export the results. Resolutions in the thousands and more will take some time."
        finish-button-text="Export"
        nextButtonText="Select Next Option"
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
                                  placeholder="Add Top Node, or type in your own and click enter"
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
                    <select v-model="selectedLevel" id="levelSelection" class="form-control" v-on:change="onLevelChange()">
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
              <form>

                <!-- Export format pulldown plugin -->
                <div class="form-group">
                  <export-format
                      :baseURL=this.$baseURL
                      @formatUpdated= "onFormatUpdated">
                  </export-format>
                </div>

              </form>
            </div>
          </div>
          <div class="row justify-content-center">
            <div class="col-12 col-md-6">
              <div class="alert alert-secondary" role="alert">
                This report will resolve {{ selectedLevel}} level(s)
                with a total of {{ this.childrenToResolveObj.childrenCount }} children.
              </div>

              <label for="exportRadio">Select how to export</label>
              <div class="custom-control custom-radio">
                <input type="radio" v-model="exportType" value="exportNow" id="exportNow" checked="" name="exportRadio" class="custom-control-input">
                <label class="custom-control-label" for="exportNow">Export now</label>
              </div>
              <div class="custom-control custom-radio">
                <input type="radio" v-model="exportType" value="exportDeferred" id="exportDeferred" name="exportRadio" class="custom-control-input">
                <label class="custom-control-label" for="exportDeferred">Export and download later</label>
              </div>

            </div>
          </div>

          <div class="row justify-content-center">
            <div class="col-12 col-md-6">
              <div class="alert alert-light" role="alert" v-if="exportType == 'exportDeferred'  && this.deferredStatusHash != ''">
                Use this Download ID on the
                <b><router-link v-bind:to="'/exports'" title="Link to Downloads">Downloads page</router-link> </b>
                to retrieve your report: <b>{{ this.deferredStatusHash }} </b>
              </div>
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
                  <div class="card-header">Selected Top Node and Levels <span class="badge badge-secondary">{{ selectedLevel }}</span></div>
                  <div class="card-body">
                    <ul class="list-group" id="selectedTagList">
                      <li v-for="selectedTag in selectedTags" :key="selectedTag.key">
                        {{ selectedTag.value }}
                      </li>
                      <li>
                        Levels to Export: {{ selectedLevel }}
                      </li>
                      <li>
                        Children to Resolve: {{ this.childrenToResolveObj.childrenCount }}
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
import ExportFormat from './ExportFormat.vue'

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
    'v-jstree': VJstree,
    ExportFormat
  },
  metaInfo: {
    title: 'EVS Report Exporter - Branch Resolve',
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
      userSelectedFormat: {"name":"JSON","description":"JavaScript Object Notation Format", "extension":"json" },
      curratedTopNodes: [],
      userSelectedTopNode: '',
      filename: 'branch',
      downloadReturnCode: null,
      curratedTopNodesUI:[],
      getPropertyError: false,
      selectedLevel: 0,
      childrenToResolve: 0,
      levels:[
        { id: 1, name: '1 Level' },
        { id: 2, name: '2 Levels' },
        { id: 3, name: '3 Levels' },
        { id: 4, name: '4 Levels' },
        { id: 5, name: '5 Levels' },
        { id: 6, name: '6 Levels' },
        { id: 7, name: '7 Levels' },
        { id: 8, name: '8 Levels' },
        { id: 9, name: '9 Levels' },
        { id: 10, name: '10 Levels' },
        { id: 11, name: '11 Levels' },
        { id: 12, name: '12 Levels' },
        { id: 13, name: '13 Levels' },
        { id: 14, name: '14 Levels' },
        { id: 15, name: '15 Levels' },
        { id: 16, name: '16 Levels' },
        { id: 17, name: '17 Levels' },
        { id: 18, name: '18 Levels' },
        { id: 19, name: '19 Levels' },
        { id: 20, name: '20 Levels' },
      ],
      childrenToResolveObj: {
        selectedLevel:0,
        selectedTag:"",
        childrenCount:0
      },
      deferredStatusUrl: '',
      deferredStatusHash: '',
      deferredStatus: false,
      showTree: true,
      asyncData: [],
      treeSelectedCode: null,
      showSummary: true,
      showSummaryText: '',
      exportType: 'exportNow',

      // function to get tree data
      loadData: function (oriNode, resolve) {
        // set id to the node to retrieve children for.
        // set to null to indicate this is the root.
        alert(oriNode);
        alert(resolve);
        var id = oriNode.data.id ? oriNode.data.id : null
        var data = []
        //console.log('id: ' + id)

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
          api.getChildren(this.$baseURL, id, 1)
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

    gaTrackDownload () {
      // Send Google analytics download event
      this.$gtag.query('event', "Branch Resolve Download", {
        'event_category': "Download",
        'event_label': this.userSelectedFormat.name
      })
    },
    gaTrackDeferredDownload () {
      // Send Google analytics deferred download event
      this.$gtag.query('event', "Branch Resolve Deferred Download", {
        'event_category': "Download",
        'event_label': this.userSelectedFormat.name
      })
    },
    // Tree dialog user chose a tree node
    userSelectTreeBranchNode() {
      //console.log('userSelectTreeBranchNode - user selected:' + this.treeSelectedCode)
      if (this.treeSelectedCode !== null) {
        this.selectedTags = [
          { key: this.treeSelectedCode, value: this.treeSelectedCode },
        ]
      }
    },

    // tree item clicked
    itemClick (node) {
      //console.log(node.model.id + ' clicked !')
      this.treeSelectedCode = node.model.id;
    },

    // Wizard methods
    validateFirstStep() {
      // make sure the user has a code entered and level.
      // if they did, then get number of children
      var stepIsValid = Object.keys(this.selectedTags).length>0 && this.selectedLevel>0
      return stepIsValid
    },

    validatePropertyStep() {
      // make sure the user has selected at least one property
      return Object.keys(this.selectedProperties).length>0
    },

    validateExportStep() {
      // make sure there is an export format selected.
      return this.userSelectedFormat !== null
    },

    onFormatUpdated (updatedFormat) {
      this.userSelectedFormat = updatedFormat
    },

    onComplete: function() {
      //this.downloadFile();

      // set the user selected tags and properties
      this.setSelectedTags()
      this.setSelectedPropertyNames()

      if (this.exportType == 'exportNow') {
        // export and wait for it to complete
        this.initiateDeferredDownloadAndWait()
      }
      else {
        // export and get a URL to go to later
        this.initiateDeferredDownloadAndReturn()
      }
    },

    async pollForStatus(hashId) {

      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });

      // check if a polling url was returned.
      if (this.deferredStatusUrl != null && this.deferredStatusUrl.length >0) {

        // loop and wait until the status comes back as true
        while (this.deferredStatus != null &&
        this.deferredStatus != "ERROR" &&
        this.deferredStatus != 'TRUE') {
          this.pollDeferredStatus()
          await this.sleep(500);
        }
        loader.hide()

        if (this.deferredStatus === "ERROR") {
          alert("Error downloading deferred file");
        }
        else {
          //this.clearDeferredData()
          // verify status is good and we can download
          this.downloadDeferredResult(hashId)
        }
        this.clearDeferredData()
      }
      else {
        console.log("deferredStatusUrl not good")
      }
    },

    // Toggle the Show/Hide Selection Summary title
    updateShowSummary() {
      this.showSummaryText = this.showSummary? 'Hide Selection Summary' : 'Show Selection Summary'
      this.showSummary = !this.showSummary;
    },

    // clear the entitiy code in the input selection
    clearSelection() {
      this.userEnteredCodes = []
      this.selectedTags = []
      this.entityList = []
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

    onTagAdded() {
      //console.log("Added tag: " + newCode)
      // When a top node is entered/selected, verify it.
      this.getEntities();
      this.updateChildrenToResolve()
    },

    onLevelChange() {
      this.updateChildrenToResolve()
    },

    updateChildrenToResolve() {
      // if the selectedTag and selectLevel have changed, set them in the
      // object and get the NEW childrenCount
      if ((this.childrenToResolveObj.selectedTag != this.selectedTags[0].key) ||
          (this.childrenToResolveObj.selectedLevel != this.selectedLevel))
      {
        this.childrenToResolveObj.selectedTag = this.selectedTags[0].key
        this.childrenToResolveObj.selectedLevel = this.selectedLevel

        // show the busy indicator
        let loader = this.$loading.show({
          container: this.$refs.formSelectCodes,
          loader: 'dots',
          isFullPage: false,
        });

        api.getChildren(this.$baseURL, this.selectedTags[0].key, this.selectedLevel)
            .then((children)=>{
              if (children != null) {
                this.childrenToResolveObj.childrenCount = children.length
              }
              else {
                this.childrenToResolveObj.childrenCount = 0
              }
            }).catch(function(error) {
          console.error("Error retrieving children to resolve: " + error);
        }).finally(function() { loader.hide()});
      }
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
      if (topNode.length >0) {
        this.selectedTags[0].key = topNode[0].code;
        this.selectedTags[0].value = topNode[0].code + ":" + topNode[0].name;
      }

      //console.log ("Updating top node: " + topNode[0].code + "  " + topNode[0].name)
      //for (let i = 0; i < Object.keys(this.curratedTopNodesUI).length; i++) {
      //console.log ("key " + this.curratedTopNodesUI[i].key + "  value " + this.curratedTopNodesUI[i].value)
      //}
    },

    updateCurratedTopNodes (topNode) {
      this.userSelectedTopNode = topNode;
    },

    getEntities(){
      // clear the entry list
      this.entityList = []
      this.setSelectedTags()
      var tempCode = ''
      var tempStatus = ''

      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formSelectCodes,
        loader: 'dots',
        isFullPage: false,
      });

      //console.log(this.selectedTags[0].key +" --- " + this.selectedTags[0].value)
      api.getCodes(this.$baseURL, this.userEnteredCodes, "ENTITY")
          .then((data)=>{

            if (data != null) {
              // If a code is retired, the object may be empty.
              for (let x = data.length -1; x >=0; x--) {
                if (data[x].queryCode < 0) {
                  //console.log("Code: " + data[x].code + " is invalid: " + data[x].queryStatus)
                  tempCode =  data[x].code
                  tempStatus = data[x].queryStatus
                  data.splice(x,1)

                  // need to remove from selectedTags
                  for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
                    if (tempCode == this.selectedTags[i].value) {
                      this.selectedTags.splice(i,1)
                    }
                  }
                  // Display error message for this code
                  this.$notify({
                    group: 'app',
                    title: 'Invalid Concept Code',
                    text: '<b>' +tempCode+'</b> is not valid. Reason: ' +tempStatus+ '.',
                    type: 'error',
                    duration: 6000,
                    position: "left bottom"
                  });
                }
              }

              this.entityList = data;
              this.updateSelectedTopNodeDescription(data);
            }
            else {
              // There was a failure making the REST call.
              this.clearSelection()
              this.$notify({
                group: 'app',
                title: 'Validation Failure',
                text: 'Could not verify top node.  Possible network issue.',
                type: 'error',
                duration: 4000,
                position: "left bottom"
              });

              this.selectedTags = [];
              //this.getPropertyError=true;
            }
          }).catch(function(error) {
        console.error("Error retrieving branch: " + error);
      }).finally(function() { loader.hide()});
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

    async initiateDeferredDownloadAndWait() {
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

      this.gaTrackDownload();

      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes,
          this.userSelectedProperyNames, this.selectedLevel,
          this.userSelectedFormat.name)
          .then((data)=>{
            if (data != null) {
              this.deferredStatusUrl = data
              //const hashId = this.getHashFromURL(this.deferredStatusUrl)
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              this.pollForStatus(this.deferredStatusHash)
            }
            else {
              this.deferredStatusUrl = null
              console.log("Error making Deferred call");
              alert("Error downloading deferred file");
            }
          }).finally(function() { loader.hide()});
    },

    async initiateDeferredDownloadAndReturn() {
      this.$notify({
        group: 'download',
        title: 'Export ID',
        text: 'Retrieving your Export ID.',
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

      this.gaTrackDeferredDownload();

      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes,
          this.userSelectedProperyNames, this.selectedLevel,
          this.userSelectedFormat.name)
          .then((data)=>{
            if (data != null) {
              this.deferredStatusUrl = data
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              //console.log("Deferred Call made.  return: " + data);
              //console.log("Deferred Call - Hash " + this.deferredStatusHash);

              this.addHashToLocalStorage(this.deferredStatusHash)
            }
            else {
              this.deferredStatusUrl = null
              console.log("Error making Deferred call");
            }
          }).finally(function() { loader.hide()});
    },

    pollDeferredStatus: function() {
      api.pollDeferredDownloadStatus(this.$baseURL, this.deferredStatusUrl)
          .then((data)=>{
            if (data != null) {
              this.deferredStatus = data.status
            }
            else {
              this.deferredStatus = "ERROR"
            }
          }).catch(function(error) {
        this.clearDeferredData()
        console.error("Polling Deferred Status Error: " + error)
      });
    },

    downloadDeferredResult(hashId) {
      axios({
        url:this.$baseURL +
            'download/deferred/checkFileForHashFormatResponseEntity/'  +
            hashId + '/' +
            this.userSelectedFormat.name + '/' +
            this.filename,
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
        console.error("Deferred Download Error: " + error);
        alert("Error Downloading file");
      }).finally(function() {
        //this.clearDeferredData()
      });

    },

    getRoots(){
      api.getRoots(this.$baseURL)
          .then((data)=>{
            if (data != null) {
              //console.log("got roots : " + data);
            }
            else {
              console.log("Error retrieving roots");
            }
          })
    },

    getChildren(){
      api.getChildren(this.$baseURL, this.userEnteredCodes, 1)
          .then((data)=>{
            if (data != null) {
              //console.log("got children : " + data);
            }
            else {
              console.log("Error retrieving children");
            }
          })
    },

    // Add to local storage
    addHashToLocalStorage() {
      // ensure there is a hashID
      if (!this.deferredStatusHash) {
        return;
      }

      this.saveDeferredDownloads();
    },

    // save to local storage
    saveDeferredDownloads() {
      this.$storage.set(this.deferredStatusHash,
          {
            key: this.deferredStatusHash,
            format: this.userSelectedFormat.name,
            date: new Date().toLocaleString(),
            status: "Unknown"
          },
          { ttl: 60 * 60 * 1000 })

      localStorage.name = "Cory"
    },

    clearDeferredData() {
      this.deferredStatusUrl = ''
      this.deferredStatusHash = ''
      this.deferredStatus = false
    },

    getHashFromURL(hash) {
      const startIndex = hash.lastIndexOf('/') + 1
      return hash.substring(startIndex)
    },

    sleep: function(ms) {
      return new Promise((resolve) => {
        setTimeout(resolve, ms);
      });
    }
  },
  created() {
    // scroll to the top of the page
    window.scrollTo(0,0);

    this.updateShowSummary();

    // load properties after the page is loaded.
    api.getProperties(this.$baseURL)
        .then((data)=>{this.availableProperties = data;
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
