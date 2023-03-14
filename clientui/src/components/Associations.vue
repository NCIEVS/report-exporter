
<template>
  <div id="read-codes-entry" class="container">


    <div class="vue-form-wizard">
      <div class="wizard-header">
        <h4 class="wizard-title">Assocations Export</h4>
        <p class="category">Steps to select concept codes, then their associations and export the results</p>
      </div>
    </div>

    <!--Vue 3 Start-->
    <div class="container" id = "exportStep">
      <div class="row justify-content-center">
        <div class="col-12 col-md-6">
          <form>
            <div class="form-group">
              <div>
                <label for="downloadFormatLabel">Select format for export</label>
                <select id="downloadFormat" class="form-control"  @change="changeSelectedExportList($event)">
                  <option value="json"> JSON (json) JavaScript Object Notation Format </option>
                  <option value="csv"> CSV (csv) Comma Separated Value Format </option>
                  <option value="txt"> TABD (txt) Tab Delimited Value Format </option>
                  <option value="xlsx"> EXCEL (xlsx) Microsoft Excel Format </option>
                </select>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!--Vue 3 End-->



    <!--Vue 3 Entity Label field  Start-->
    <div class="entityLabel" id = "entityLabelId">
      <label for="tags" >Enter NCI Thesaurus concept codes</label>
    </div>
    <!--Vue 3 Entity Label field  End-->

    <!--Vue 3 Entity Text field  Start-->
    <div class="entityText" id = "entityTextID" element-id="tag-input">
      <input placeholder="Type entity code, then click enter"
             class="entityCodeInput" v-model="newTag"
             @keyup.enter.exact="addTag1(newTag)"
             @keyup.space.exact="addTag1(newTag)">
      <br>
      <br>
      <div class = "tag-input"></div>
      <ul class="tags" id = "listOfTags">
        <li><a v-for="tag in tags" :key="tag" class="tag" id="tags2">
          {{ tag }}
          <button class="delete" @click="removeTag(index)">x</button>
        </a></li>
      </ul>

      <input type="hidden" v-for="baseLink in loadBaseURL" :key="baseLink" id="basURL" name="basURL" value="@{{loadBaseURL.value}}" >
    </div>


    <!--Vue 3 step 2 list boxes Start -->
    <div id="app" class="container">
      <div role="tabpanel" id="SelectProperties1" aria-labelledby="step-SelectProperties1" class="wizard-tab-container" style>
        <div class="container">
          <form>
            <div class="form-group">
              <label for="selectedProperties" id = "SelectProperties3">No associations selected for these concept codes</label>
              <br>
              <label for="selectedProperties" id = "enteredCodeLabelLeft">{{ this.tags }}</label>
              <br>
              <label for="selectedProperties" id = "SelectProperties2">Select associations to include in the export</label>
            </div>
            <div class="form-group">
              <div class="msl-multi-select">
                <div class="msl-searchable-list msl-multi-select__list">
                  <input placeholder="Search properties" class="msl-search-list-input custom-input-class" id = "searchProperties" @keyup = "searchPropertiesFilter()">
                  <select multiple v-model="leftSelectedUsers" @dblclick="moveRight" class="msl-searchable-list__items" id = "selectSearchProperties">
                    <option v-for="userLeft in availableProperties" :key="userLeft" class="multi-select-option msl-searchable-list__item" id = "optionSearchProperties">
                      {{ userLeft}}
                    </option>
                  </select>
                </div>
                <div class="listBoxButton">
                  <table>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td><input type = "button" value = "=>" id = "toListBox" class = "toListBox" @click="moveRight"></td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                  </table>

                  <table>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td><input type = "button" value = "<=" id = "fromListBox" class = "fromListBox" @click="moveLeft"></td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                  </table>
                </div>

                <div class="msl-searchable-list msl-multi-select__selected msl-multi-select__list">
                  <div class="col">
                    <label for="selectedProperties" id = "SelectProperties3" class = "UsedLabelRight">Used concept codes</label>
                    <br>
                    <label for="selectedProperties" id = "enteredCodeLabelRight" class = "UsedCodeRight">{{ this.tags }}</label>
                    <br>
                    <br>
                  </div>


                  <input placeholder="Search selected properties" class="msl-search-list-input custom-input-class"  id = "selectedProperties" @keyup = "searchSelectedPropertiesFilter()" >
                  <select multiple v-model="rightSelectedUsers" @dblclick="moveLeft" class="msl-searchable-list__items" id = "selectSelectedProperties">
                    <option v-for="userRight in rightUsers" :key="userRight" class="multi-select-option msl-searchable-list__item" id = "optionSelectedProperties">
                      {{ userRight }}
                    </option>
                  </select>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!--Vue 3 step 2 list boxes End -->


    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "clearButton" class="btn-delete" v-on:click="removeAllTags2(0)"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Clear </button>
      </span>

    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "backButton" class="btn-back" v-on:click="backStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Back </button>
      </span>

    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "nextOption" class="btn-next" v-on:click="validateFirstStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Select Next Option </button>
      </span>

    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "exportButton" class="btn-export" v-on:click="exportStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Export </button>
      </span>




    <!--Vue 3 Entity Text field  End-->



    <!-- Summary Information -->
    <div id="accordion" class="pb-3 pt-3">
      <div class="card">
        <div class="card-header" id="headingOne"  style="
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


        <div id="accordion" class="pb-3 pt-3">
          <div class="card">
            <div id="headingOne" class="card-header" style="padding: 1px;">



              <!--
            <center>
              <button data-toggle="collapse" data-target="#collapseSummary" aria-expanded="true" aria-controls="collapseSummary" class="btn btn-link"> Hide Selection Summary </button>
            </center>
            -->
              <!-- Vue3 Selection Summary List boxes Start  -->
            </div>
            <div id="collapseSummary" aria-labelledby="headingOne" data-parent="#accordion" class="collapse show">
              <div class="card-body pb-1">
                <div class="row p-1">
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">
                        Selected Concept Codes
                        <span class="badge badge-secondary" id = "selectConceptCodesCount">{{Object.keys(this.tags).length}}</span>
                      </div>


                      <div class="card-body">
                        <span class="list-group" id="selectedConceptCodesTags">{{ this.tags }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">
                        Selected Associations
                        <span class="badge badge-secondary">{{Object.keys(this.rightUsers).length}}</span>
                      </div>
                      <div class="card-body">
                        <span class="list-group" id="selectedPropertyList">{{ this.rightUsers }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">Selected Export Format</div>
                      <div class="card-body">
                        <ul class="list-group" id="selectedPropertyList">
                          <li>
                            {{ selectedExportListName }}
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

        <!-- Vue3 Selection Summary List boxes End  -->

      </div>
    </div>
  </div>
</template>

<script>

// Custom input tags

import api from '../api.js'
import axios from 'axios'
import 'form-wizard-vue3/dist/form-wizard-vue3.css'
import {ref} from "vue";


//vue 3 counter for (Select Next Option) button due to form-wizard not working
let selectNextOptionBTN_counter =  1;


export default {


  name: 'read-code-entry',
  props: {
    msg: String
  },
  components: {

  },
  metaInfo: {

    title: 'EVS Report Exporter - Read Code',
  },


  mounted() {
    this.hideObjectsOnScreen();  //function for when page loads certain objects like buttons or text boxes will be hidden
    this.selectedExportListName = "JSON (json) JavaScript Object Notation Format"

  },

  setup(){
    const tags = ref([]);
    //const loadBaseURL = ref([]);
    //var  url= ref();
    const newTag = ref('') //keep up with new tag
    var tagCounter1 = 0;
    //var newTagCounter = 0;
    //this.entityList = []'


    //Vue 3 Remotes a tag below text box
    const removeTag = (index) => {
      tags.value.splice(index, 1);
      tagCounter1 = tagCounter1  - 1;
    };

    return { tags, newTag, removeTag, tagCounter1 }
  },



  data(){
    return {
      selectedTags: [],
      userEnteredCodes: '',
      entityList: [],
      availableProperties: [],
      selectedProperty: [],
      userSelectedProperyNames: '',
      userSelectedFormat: '',
      fileFormat: '',
      selectedExportListName: '',
      selectedConceptCodes: [],
      selectedConceptCode: [],
      filename: 'associations',
      downloadReturnCode: null,
      invalidTag: '',
      showSummary: true,
      showSummaryText: '',
      tag: "[]",
      leftSelectedUsers:[],
      leftUsers: [],
      rightSelectedUsers:[],
      rightUsers:[],
      loadBaseURL: this.$baseURL,
      availableAssociations: [],
      selectedAssociations: [],
      tempListClear:[],
      tagCounter: 0,
      newTagCounter: 0
    };
  },

  methods: {

    removeAllTags2 (tagDeleteCounter) {
     // alert("REmove value " + tagDeleteCounter);
     // alert("Counter value " + this.tags.length);
      for (let i = 0; i <= this.tags.length; i++) {
        this.tags.splice(tagDeleteCounter, this.tags.length);
        tagDeleteCounter = tagDeleteCounter + 1;
      }
      this.tag = []
      this.newTag = []
      this.userEnteredCodes = []
      this.selectedTags = []
      this.entityList = []
      this.multipleEntitiesSplit = []
      this.invalidTag = ''
      this.userSelectedProperyNames = []
      this.tags2 = []
    },


    addTag1(tag) {
      var codeDescription = [];
      //var baseLink = document.getElementById('basURL').value;
      //    var this.tagCounter = 0;
      //    var this.newTagCounter = 0;
      var dupTagCheck = false;
      //const tags = ref([]);
      //const newTag = ref('') //keep up with new tag
      tag = tag.replace(/[\s/]/g, '')
      tag = tag.replace(',', '')

      this.setSelectedTags()

      for (let i = 0; i < this.userEnteredCodes.length; i++) {  //Vue 3 checks for duplicate codes
        if (this.userEnteredCodes[i] === tag) {
          dupTagCheck = true;
        }
      }
      //Vue 3 checks entity code entered and returns a description if on is available
      if (tag != "") {
        api.getCodes( this.$baseURL, tag, 'ENTITY')
            .then((data)=> {
              alert(data);
              if ((data != null) && (data!== undefined) && (data!== "")) {
                for (let x = data.length - 1; x >= 0; x--) {
                  alert(data[x].name);
                  if ((data[x].name != null) && (data[x].name!== undefined)) {
                    //  if ((data[x].name.length > 0) && (data[x].name!== undefined)) {

                    //  alert("Code: " + data[x].code + " is invalid: " + data[x].queryStatus + " roles: " + data[x].roles + " association: " + data[x].associations + " Description: " + data[x].name);
                    if (dupTagCheck === true) {
                      this.newTag = [];
                      dupTagCheck = false;
                    }else {
                      codeDescription = data[x].name;
                      this.tags.push(tag + ":" + codeDescription);
                      this.newTag = ""; // reset newTag
                      this.tagCounter = this.tagCounter + 1;
                      this.newTagCounter = this.newTagCounter + 1;
                    }
                  }else{
                    this.$notify({
                      //    this.tags.push(tag + ":" + "");   //take out after testing
                      //    this.newTag = ""                  //take out after testing
                      //     this.tagCounter = this.tagCounter + 1;  //take out after testing
                      group: 'app',
                      title: 'Validation Failure',
                      text: 'Could not verify concept code(s).  Possible network issue.',
                      type: 'error',
                      duration: 4000,
                      position: "left bottom"
                    });
                  }
                }
              }else {
                if (dupTagCheck === true) {
                  this.newTag = [];
                  dupTagCheck = false;
                }else{
                  //    this.tags.push(tag + ":" + "");   //take out after testing
                  //    this.newTag = ""                  //take out after testing
                  //     this.tagCounter = this.tagCounter + 1;  //take out after testing
                  this.$notify({
                    group: 'app',
                    title: 'Validation Failure',
                    text: 'Could not verify concept code(s).  Possible network issue.',
                    type: 'error',
                    duration: 4000,
                    position: "left bottom"
                  });
                }
              }
            })
      }
    },



    setSelectedTags () {
      var bottomTab = "";
      var indexBottomTab = 0;
      // clear the internal user codes that are entered
      this.userEnteredCodes = []
      for (let i = 0; i < Object.keys(this.tags).length; i++) {
        //  for (let i = 0; i < 1; i++) {
        // currated top nodes (from the server hava a value of "C12434:Blood")
        // so we need to strip off everything from the : to the right.
        if (this.tags[i] !== "undefined") {
          bottomTab = this.tags[i];
          indexBottomTab = bottomTab.indexOf(":");
          this.userEnteredCodes.push(bottomTab.slice(0,indexBottomTab));
        }
      }
    },
    testCall2(){
      alert("testCall2");
    },

    // clear all of the entitiy codes in the input selection
    clearSelection() {
      //alert (this.baseURL);
      //alert (this.rolesRequired);
      //alert (this.associationsRequired);
      //alert (this.queryEntitySelection);
      //alert("this is the data for clearSelection: " + this.selectedTags);

      this.tag = []
      this.newTag = []
      this.userEnteredCodes = []
      this.selectedTags = []
      this.entityList = []
      this.multipleEntitiesSplit = []
      this.invalidTag = ''
      this.userSelectedProperyNames = []
      this.tags2 = []
      //this.tags = []


      //document.getElementById("listOfTags").style.display = "none";  // remove tags
      //document.getElementById("listOfTags").innerHTML = "";
      document.getElementById("selectConceptCodesCount").innerText = 0;
      document.getElementById("selectedConceptCodesTags").innerText = "";
      // tagsLength = document.getElementById("tags").value.length;
      //document.getElementById("selectConceptCodesCount").value = 0;
      //this.selectConceptCodesCount = 0;
      // (tagCountTotal) => (this.tags.value.splice(tagCountTotal, 1));



      //  const removeAllTags = (tagDeleteCounter) => {

      //  }

      this.updateParent()
    },

    updateParent() {
      alert("this is the data for updateParent: " + this.selectedTags);
      this.setSelectedTags()

      // Notify users of this plugin that the user selected values changed.
      this.$emit('entitesUpdated',
          this.selectedTags,
          this.entityList,
          this.userSelectedProperyNames,
          this.userEnteredCodes)
    },

    removeAllTagsFunc(){
      alert("test0");
      this.removeAllTags(1);
      alert("test1");
    },
    moveLeft() {
      if(!this.rightSelectedUsers.length) return;
      for(let i=this.rightSelectedUsers.length;i>0;i--) {
        let idx = this.rightUsers.indexOf(this.rightSelectedUsers[i-1]);
        this.rightUsers.splice(idx, 1);
        this.availableProperties.push(this.rightSelectedUsers[i - 1])
        this.rightSelectedUsers.pop();
        document.getElementById("enteredCodeLabelLeft").style.display = "";
        document.getElementById("enteredCodeLabelRight").style.display = "none";
      }
    },

    moveRight(){
      if (!this.leftSelectedUsers.length) return;
      for (let i = this.leftSelectedUsers.length; i > 0; i--) {
        let idx = this.availableProperties.indexOf(this.leftSelectedUsers[i-1]);
        this.availableProperties.splice(idx, 1);
        this.rightUsers.push(this.leftSelectedUsers[i - 1]);
        this.tempListClear.push(this.leftSelectedUsers[i - 1]);
        this.leftSelectedUsers.pop();
        document.getElementById("enteredCodeLabelLeft").style.display = "none";
        document.getElementById("enteredCodeLabelRight").style.display = "";
    }
    },





/*
    moveRight(){
      for (let i = 0; i < this.availableProperties.length; i++) {
        this.rightUsers.push(this.leftSelectedUsers[i])
        this.selectedProperty.push(this.leftSelectedUsers[i]);
        this.leftSelectedUsers[i].pop();
        document.getElementById("enteredCodeLabelLeft").style.display = "none";
        document.getElementById("enteredCodeLabelRight").style.display = "";
      }
    },

 */
    /*
    moveRight() {
      if(!this.leftSelectedUsers.length) return;
      console.log('moveRight', this.leftSelectedUsers);
      for(let i=this.availableProperties.length;i>0;i--) {
        let idx = this.leftUsers.indexOf(this.leftSelectedUsers[i-1]);
        alert("ldx: " + idx);
        this.leftUsers.splice(idx, 1);
        alert("leftUsers: " + this.leftUsers);
        this.rightUsers.push(this.leftSelectedUsers[i-1]);
        alert(this.leftSelectedUsers[i-1]);
        //this.selectedProperty.push(this.leftSelectedUsers[i-1]);
        this.selectedProperty.push(this.leftSelectedUsers[i-1]);
        this.leftSelectedUsers.pop();
        document.getElementById("enteredCodeLabelLeft").style.display = "none";
        document.getElementById("enteredCodeLabelRight").style.display = "";
      }
    },

     */
/*
    /*
    moveRight() {
      this.userSelectedProperyNames = []
      for (let i = 0; i < this.leftSelectedUsers.length; i++) {
        this.rightUsers.push(this.leftSelectedUsers[i])
        this.selectedProperty.push(this.leftSelectedUsers[i]);
        document.getElementById("enteredCodeLabelLeft").style.display = "none";
        document.getElementById("enteredCodeLabelRight").style.display = "";
      }
    },

     */
    /*
        getEntities(){
          // clear the entry list

          alert("first getEntities");
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

          alert("test call 1");
          api.getCodes(this.$baseURL, this.userEnteredCodes, this.queryEntitySelection)
              .then((data)=>{
                if (data != null) {
                  // loop through all codes and verify data is returned for each
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
                        text: '<b>' +tempCode+'</b> is not valid. <br>Reason: ' +tempStatus+ '.',
                        type: 'error',
                        duration: 6000,
                        position: "left bottom"
                      });
                    }
                    // Check if the concept code must have roles to be valid
                    if (this.rolesRequired && data[x].roles.length < 1) {
                      //console.log("Code: " + data[x].code + " is invalid: NO Associations")
                      tempCode =  data[x].code
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
                        title: 'Warning',
                        text: '<b>'+tempCode+'</b> will not appear in the report. <br>Reason: No Roles for this concept code.',
                        type: 'error',
                        duration: 6000,
                        position: "left bottom"
                      });
                    }
                    // Check if the concept code must have associations to be valid
                    if (this.associationsRequired && data[x].associations.length < 1) {
                      //console.log("Code: " + data[x].code + " is invalid: NO ROLES")
                      tempCode =  data[x].code
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
                        title: 'Warning',
                        text: '<b>'+tempCode+'</b> will not appear in the report. <br>Reason: No Associations for this concept code.',
                        type: 'error',
                        duration: 6000,
                        position: "left bottom"
                      });
                    }
                  }

                  this.entityList = data;
                  this.updateSelectedConceptCodeDescriptions(data);
                  this.updateParent()
                }
                else {
                  // There was a failure making the REST call.
                  this.clearSelection()
                  this.$notify({
                    group: 'app',
                    title: 'Validation Failure',
                    text: 'Could not verify concept code(s).  Possible network issue.',
                    type: 'error',
                    duration: 4000,
                    position: "left bottom"
                  });
                }
              }).catch(function(error) {
            console.error("Error retrieving entities: " + error);
          }).finally(function() { loader.hide()});
        },
    */
    /*
        // called when an entity/code is added
        onTagAdded(newCode) {
          // Test if the string entered was pasted in - if it has a comma separated
          // list of values
          alert("onTagAdd Function");

          newCode.value.includes(',') ?
              this.multipleEntitiesSplit = this.cleanString(newCode.value).split(",") :
              this.multipleEntitiesSplit = []

          alert(this.multipleEntitiesSplit);
          // if the user entered multiple values, remove the last entry (which
          // is the comma separated string) and add each one individually.
          if (this.multipleEntitiesSplit.length > 0) {
            this.tags.splice(-1,1);


            for(let x = this.multipleEntitiesSplit.length; x >=0; x-- ) {
              // Make sure we don't add a duplicate.
              // Check if user entered two commas with no entitiy code inbetween them
              // example:  C101171,  ,C101173
              if ( (! this.isDuplicateTag(this.multipleEntitiesSplit[x])) &&
                  (this.multipleEntitiesSplit[x] !== undefined) &&
                  (this.multipleEntitiesSplit[x].length > 0)) {
                this.selectedTags.push({key: this.multipleEntitiesSplit[x], value: this.multipleEntitiesSplit[x]})
              }
            }
          }

          else {
            if (this.isDuplicateTag(newCode.value))
            {
              // remove the last entered entity code
              this.selectedTags.splice(-1,1);
            }
          }

          // When a top node is entered/selected, verify it.
          this.getEntities();
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
          api.getCodes(this.$baseURL, this.userEnteredCodes, this.queryEntitySelection)
              .then((data)=>{
                if (data != null) {
                  // loop through all codes and verify data is returned for each
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
                        text: '<b>' +tempCode+'</b> is not valid. <br>Reason: ' +tempStatus+ '.',
                        type: 'error',
                        duration: 6000,
                        position: "left bottom"
                      });
                    }
                    // Check if the concept code must have roles to be valid
                    if (this.rolesRequired && data[x].roles.length < 1) {
                      //console.log("Code: " + data[x].code + " is invalid: NO Associations")
                      tempCode =  data[x].code
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
                        title: 'Warning',
                        text: '<b>'+tempCode+'</b> will not appear in the report. <br>Reason: No Roles for this concept code.',
                        type: 'error',
                        duration: 6000,
                        position: "left bottom"
                      });
                    }
                    // Check if the concept code must have associations to be valid
                    if (this.associationsRequired && data[x].associations.length < 1) {
                      //console.log("Code: " + data[x].code + " is invalid: NO ROLES")
                      tempCode =  data[x].code
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
                        title: 'Warning',
                        text: '<b>'+tempCode+'</b> will not appear in the report. <br>Reason: No Associations for this concept code.',
                        type: 'error',
                        duration: 6000,
                        position: "left bottom"
                      });
                    }
                  }

                  this.entityList = data;
                  this.updateSelectedConceptCodeDescriptions(data);
                  this.updateParent()
                }
                else {
                  // There was a failure making the REST call.
                  this.clearSelection()
                  this.$notify({
                    group: 'app',
                    title: 'Validation Failure',
                    text: 'Could not verify concept code(s).  Possible network issue.',
                    type: 'error',
                    duration: 4000,
                    position: "left bottom"
                  });
                }
              }).catch(function(error) {
            console.error("Error retrieving entities: " + error);
          }).finally(function() { loader.hide()});
        },

        isDuplicateTag(newTag){
          for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
            //console.log ('Existing code: ' + this.selectedTags[i].key + ' newTag: ' + newTag);
            if (this.selectedTags[i].key == newTag) {
              //console.log ('Removing duplicate entity code: ' + this.selectedTags[i].key);
              return true;
            }
          }
          return false;
        },
        // removes forward slashes and all kinds of Unicode whitespace characters
    */



    //Vue 3 Start Step 2 left Search Function
    searchPropertiesFilter() {
      var input;
      var formattedInput;
      var listBoxValues;
      var selectedListBoxValue;
      var i;
      var txtValue;

      input = document.getElementById("searchProperties"); //text input
      formattedInput = input.value.toUpperCase();  //change to upper case

      listBoxValues = document.getElementById("selectSearchProperties");
      for (i = 0; i < listBoxValues.length; i++) {
        selectedListBoxValue = listBoxValues[i];
        txtValue = selectedListBoxValue.textContent || selectedListBoxValue.innerText;
        if (txtValue.toUpperCase().indexOf(formattedInput) > -1) {
          listBoxValues[i].style.display = "";
        } else {
          listBoxValues[i].style.display = "none";
        }
      }
    },
    //Vue 3 End



    //Vue 3 Start Step 2 Right Search Function
    searchSelectedPropertiesFilter() {
      var input;
      var formattedInput;
      var listBoxValues;
      var selectedListBoxValue;
      var i;
      var txtValue;

      input = document.getElementById("selectedProperties"); //text input
      formattedInput = input.value.toUpperCase();  //change to upper case

      listBoxValues = document.getElementById("selectSelectedProperties");
      for (i = 0; i < listBoxValues.length; i++) {
        selectedListBoxValue = listBoxValues[i];
        txtValue = selectedListBoxValue.textContent || selectedListBoxValue.innerText;
        if (txtValue.toUpperCase().indexOf(formattedInput) > -1) {
          listBoxValues[i].style.display = "";
        } else {
          listBoxValues[i].style.display = "none";
        }
      }
    },
    //Vue 3 End

    //When page loads certain objects like buttons or text boxes will be hidden
    hideObjectsOnScreen() {
      document.getElementById("SelectProperties1").style.display = "none";
      document.getElementById("backButton").style.display = "none";
      document.getElementById("exportStep").style.display = "none";
      document.getElementById("exportButton").style.display = "none";






    },

    gaTrackDownload () {
      // Send Google analytics download event
      this.$gtag.query('event', "Read Concept Code Download", {
        'event_category': "Download",
        'event_label': this.userSelectedFormat.name
      })
    },



    // Wizard methods
    validateFirstStep() {
      // make sure the user has a code entered

      //Vue 3 Select Next Option Counter.  This counter replaces the form-Wizard logic that is not working
      //correctly under vue 3.  If value is 1 then it implements validateFirstStep fucction.  If value is 2 then
      //it implements validatePropertyStep function.  If validateExportStep is 3 then it implements the validateExportSetup function
//alert (this.availableProperties.value);
      //    var obj = JSON.parse(this.availableProperties.value);
      //alert("JSON " + obj);
      //  document.getElementById("selectSearchProperties").innerHTML = obj.name;
      //alert("Test2");


      //Vue 3 STEP 1
      if (selectNextOptionBTN_counter === 3) {
        alert("counter 3 if statement")
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextButton").style.display = ""; //Hides next button
      }

      if (selectNextOptionBTN_counter === 2) {
        this.validatePropertyStep();
      }

      if (selectNextOptionBTN_counter === 1) {
        if (this.tags.length > 0) {  // checks to make sure that a code was entered before proceeding to next screen
          document.getElementById("clearButton").style.display = "none";    //Hides clear button
          document.getElementById("entityTextID").style.display = "none";   //Hides textbox on main screen
          document.getElementById("entityLabelId").style.display = "none";  //Hides label on main screen
          document.getElementById("SelectProperties1").style.display = "";  //Shows listboxs on second screen
          document.getElementById("backButton").style.display = "";     //Shows back button
          document.getElementById("enteredCodeLabelLeft").style.display = "";
          document.getElementById("enteredCodeLabelRight").style.display = "none";

          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1

          this.setSelectedTags();
          if (this.availableProperties.length <= 0) {
        //    alert("base URL " + this.$baseURL);
        //    alert("UserEntered Code " + this.userEnteredCodes);
            api.getAssociations(this.$baseURL, this.userEnteredCodes)
                .then((data) => {
                  for (let x = data.length - 1; x >= 0; x--) {
       //             alert("data " + data[x].type);
                    this.availableProperties.push(data[x].type);
                  }
                })
          }


          // reset what concept codes are used
          this.updateUsedConceptCodes()
          document.getElementById("enteredCodeLabelLeft").style.display = "";
          document.getElementById("enteredCodeLabelRight").style.display = "none";

          //Vue 3 removes enteries from right list box on next screen



/*
          if (tag != "") {
            api.getCodes( "https://evs-dev.cancer.gov/report-exporter/", tag, 'ENTITY')
                .then((data)=> {
                  alert("after call");
                  // data = "test";
                  if ((data != null) && (data!== undefined)) {
                    alert("after checks");
                    for (let x = data.length - 1; x >= 0; x--) {
                      alert("Code: " + data[x].code + " is invalid: " + data[x].queryStatus + " roles: " + data[x].roles + " association: " + data[x].associations + " Description: " + data[x].name);
                      codeDescription = data[x].name;
                      alert("before push");
                      tags.value.push(tag + ":" + codeDescription);

                      newTag.value = ""; // reset newTag
                      this.tagCounter = this.tagCounter + 1;
                      this.newTagCounter = this.newTagCounter + 1;
                      alert("Before getEntities Call()");
                      //getEntities();
                      alert("After getEntities Call()");
                    }
                  }else {
                    alert("Code entered was not found");
                    tags.value.push(tag + ":" + "");
                    newTag.value = ""
                    this.tagCounter = this.tagCounter + 1;
                    this.newTagCounter = this.newTagCounter + 1;
                    // selectedConceptCodes.value.push("test");
                    //tags.value.push(tag);
                    //alert(codeDescription);

                  }

                })
          }

*/









        }
      }


    },

    //Vue 3 STEP 2
    validatePropertyStep() {
      // make sure the user has selected at least one property
      //Hides objects on screen that shouldn't appear in step 2
      // document.getElementById("entityTextID").style.display = " ";
      // document.getElementById("entityLabelId").style.display = " ";

      if (this.rightUsers.length > 0) {
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextOption").style.display = "none"; //Hides next option button


        if (Object.keys(this.rightUsers).length > 0) {
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1;
          return Object.keys(this.rightUsers).length > 0
        }
      }


      // reset what concept codes are used
      this.updateUsedConceptCodes()
    },
    backStep(){
      //Shows screen for step 1
      if (selectNextOptionBTN_counter === 2) {
        document.getElementById("SelectProperties1").style.display = "none";  //shows listboxs on second screen
        document.getElementById("clearButton").style.display = "";    //Shows clear button
        document.getElementById("entityTextID").style.display = "";   //Shows textbox on main screen
        document.getElementById("entityLabelId").style.display = "";  //Shows label on main screen
        document.getElementById("backButton").style.display = "none"; //Hides back button on main screen
        document.getElementById("nextOption").style.display = "";     //Shows next button
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;

        if(!this.rightUsers.length) return;
        for(let i=this.rightUsers.length;i>0;i--) {
          let idx = this.rightUsers.indexOf(this.rightSelectedUsers[i-1]);
          this.rightUsers.splice(idx, 1);
          this.availableProperties.push(this.tempListClear[i - 1])
          this.rightSelectedUsers.pop();
          this.tempListClear.pop();
        }
        this.availableProperties.sort();
      }

      //Shows screen =for step 2
      if (selectNextOptionBTN_counter === 3) {
        document.getElementById("SelectProperties1").style.display = "";  //shows listboxs on second screen
        document.getElementById("backButton").style.display = "";     //Shows back button on main screen
        document.getElementById("exportButton").style.display = "none"; //Hides Export button
        document.getElementById("nextOption").style.display = ""; //Hides next button
        document.getElementById("exportStep").style.display = "none";  //Hides Export Step
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;
      }


    },

    exportStep() {
      this.downloadFile();
    },


    onEntitiesUpdated(updatedTags, updatedEntityCodes, userSelectedProperyNames,userEnteredCodes) {
      alert("this method onEntitiesUpdated invoked")

      this.selectedTags = updatedTags
      this.entityList = updatedEntityCodes
      this.userSelectedProperyNames = userSelectedProperyNames
      this.userEnteredCodes = userEnteredCodes
    },

    updateShowSummary() {
      // alert("updateShowSummary")
      this.showSummaryText = this.showSummary? 'Hide Selection Summary' : 'Show Selection Summary'
      this.showSummary = !this.showSummary;

    },

    setSelectedPropertyNames() {
      this.userSelectedProperyNames = []
      for (let i = 0; i < this.rightUsers.length; i++) {
        this.userSelectedProperyNames.push(this.rightUsers[i].name)
      }
    },



    //Vue 3 Function controls Select format Export dropdown on Step 3
    changeSelectedExportList(event){
      this.userSelectedFormat = event.target.value;
      if (event.target.value === "json") {
        this.fileFormat = "JSON";
        this.selectedExportListName = "JSON (json) JavaScript Object Notation Format"
      }

      if (event.target.value === "csv") {
        this.fileFormat = "CSV";
        this.selectedExportListName = "CSV (csv) Comma Separated Value Format"
      }

      if (event.target.value === "txt") {
        this.fileFormat = "TABD";
        this.selectedExportListName = "TABD (txt) Tab Delimited Value Format"
      }

      if (event.target.value === "xlsx") {
        this.fileFormat = "EXCEL";
        this.selectedExportListName = "EXCEL (xlsx) Microsoft Excel Format"
      }
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

      //alert("excel export called");

      // set the user selected tags and properties
      //this.setSelectedAssoc()

     // alert("setSelectedAssociationNames");
      this.gaTrackDownload();
      //this.setSelectedTags();
      this.setSelectedPropertyNames()

     // alert("gaTrackDownload");

      //Vue 3 Sets default value to JSON for Select format for export dropdown on Step 3
      if (this.fileFormat === ""){
        this.userSelectedFormat = 'json';
        this.fileFormat = 'JSON';
        this.selectedExportListName = 'JSON (json) JavaScript Object Notation Format';
      }

    //  alert("check 1")
    //  alert("base URL: " + this.$baseURL);
    //  alert("tags: " + this.userEnteredCodes);
    //  alert("selectedPropertyName: " + this.rightUsers);
    //  alert("SelectedFormat: " + this.fileFormat);
    //  alert("filename: " + this.filename);
    //  alert("selectedFormat Extension: " + this.userSelectedFormat);

      //Check Extension

      //alert (this.queryEntitySelection);
      axios({
        url: this.$baseURL + 'download/get-file-for-resolved-associations/'  +
            this.userEnteredCodes + '/' +
            this.rightUsers + '/' +
            this.fileFormat  + '/'+
            this.filename + '.' +
            this.userSelectedFormat,
        method: 'GET',
        responseType: 'blob',
      }).then((response) => {
        var fileURL = window.URL.createObjectURL(new Blob([response.data]));
        var fileLink = document.createElement('a');
        fileLink.href = fileURL;
        fileLink.setAttribute('download', this.filename + '.' + this.userSelectedFormat);
        document.body.appendChild(fileLink);
        fileLink.click();


      }).catch(function(error) {
        console.error("Download Error: " + error);
        //alert("Error Downloading file error message: " + error);
      })
    },

    // removes forward slashes and all kinds of Unicode whitespace characters
    cleanString(string) {
      return string.replace(/[\s/]/g, '')
    }
  },
  created() {

    // scroll to the top of the page
    window.scrollTo(0,0);

    this.updateShowSummary();

    // load properties after the page is loaded.

  },

  // Vue 3 Start
/*
  onTagRemoved(code) {
    this.removeTag(code)
    this.updateParent()
  },

  removeTag(code) {
    // find and remove the code from the entity list that is to be removed.
    for (let i = 0; i < Object.keys(this.entityList).length; i++) {
      if (code.key == this.entityList[i].code) {
        this.entityList.splice(i,1)
        break
      }
    }
  },
*/



  validateAssociationStep() {
    // make sure the user has selected at least one assocation
    return Object.keys(this.selectedAssociations).length>0
  },


  setSelectedAssoc() {
    alert("setassociation Function called");
    this.userSelectedAssociationNames = []
alert("selectedAssociationtest 1");
    for (let i = 0; i < Object.keys(this.rightUsers).length; i++) {
      alert("selectedAssociationtest 2");
      this.userSelectedAssociationNames.push(this.rightUsers[i].type)
      alert("selectedAssociationtest 3");
    }
  },

  /*
  getAssociations() {
    // load Associations for the selected codes
    alert("before Base URL");
    alert("baseURL " + this.$baseURL);
    alert("EnteredCodes " + this.userEnteredCodes);
    api.getAssociations(this.$baseURL, this.userEnteredCodes)
        .then((data)=>{this.availableProperties = data;
        })
  },
*/

  updateUsedConceptCodes() {
    this.usedCodes = [];
    this.unusedCodes = [];

    // loop through all concept codes
    for(let x = this.entityList.length; x >=0; x-- ) {
      if (this.entityList[x]) {
        if (this.selectedAssociations.length == 0) {
          this.unusedCodes.push(this.entityList[x].code);
        }

        else {
          // for each concept code, loop through its Associations
          for(let i = this.selectedAssociations.length; i >=0; i-- ) {
            if (this.selectedAssociations[i]) {
              // check if the selected assocation is associated to the concept code
              // if it is, add concept code
              const associations = this.entityList[x].associations;

              if (associations.some(item => item.type === this.selectedAssociations[i].type)) {
                this.usedCodes.push(this.entityList[x].code);
                break;
              }
              else if (i == 0) {
                this.unusedCodes.push(this.entityList[x].code);
                break;
              }
            }
          }
        }
      }
    }
  },




}

//Vue3 End-->

</script>

<!-- styling for the component -->
<style>
/* #read-codes-entry{
  top: 60;
} */

.container{
  padding: 5px 15px 5px 15px;
}
.wizard-tab-container {
  padding: 5px 5px 5px 5px;
}
/* Summary list box formatting */
.list-group{
  max-height: 150px;
  min-height: 150px;
  overflow-y:auto;
}

.exportButtons {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}

.wizard-btn {
  background-color: rgb(0, 125, 188) !important;
  border-color: rgb(0, 125, 188) !important;
  color: white;
}
.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open>.dropdown-toggle.btn-primary {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}
.btn-link {
  color: rgba(33, 37, 41, 1) !important;
}
.msl-multi-select {
  /* make the multi-select take up the entire width of the container */
  width: 100%
}

.vue-form-wizard, .category {
  color: #767676 !important;
}

a.disabled {
  color: #767676 !important;
}

.active {
  color: rgb(0, 125, 188) !important;
}

.wizard-header p {
  color: #767676;
}

.wizard-nav-pills>li>a{
  color: rgba(0,0,0,.8) !important;
}

.msl-searchable-list__no-item{
  color: #74767a;
  font-size: 16px
}


.entityCodeInput{
  width: 555px;
  position: absolute;
}

.entityText{
  padding-left: 277px;
}

.entityLabel{
  padding-left: 277px;
}

ul {
  list-style: none;
  display: flex;
  align-items: center;
  gap: 7px;
  margin: 0;
  padding: 0;
}
.tag {
  background-color: rgb(0, 125, 188);
  padding: 5px;
  border-radius: 4px;
  color: white;
  white-space: nowrap;
  transition: 0.1s ease background;
}

.delete {
  color: white;
  background: none;
  outline: none;
  border: none;
  cursor: pointer;
}

.toListBox {
  background-color: rgb(0, 125, 188);
  padding: 5px;
  border-radius: 4px;
  color: white;
  white-space: nowrap;
  transition: 0.1s ease background;
}

.fromListBox {
  background-color: rgb(0, 125, 188);
  padding: 5px;
  border-radius: 4px;
  color: white;
  white-space: nowrap;
  transition: 0.1s ease background;
}

.listBoxButton{
  margin-top : 110px;
  display: block;
}
.UsedLabelRight{
  margin-top : -111px;
  display: block;
  margin-left: -20px;
}

.UsedCodeRight{
  margin-top : -25px;
  display: block;
  margin-left: -20px;
}

.btn-delete{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 277px;
  width: 100px;
}

.btn-next{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 300px;
  width: 158px;
}

.btn-export{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 300px;
  width: 158px;
}

.btn-back{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 277px;
  width: 158px;
}
</style>