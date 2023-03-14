
<template>
  <div id="read-codes-entry" class="container">


    <div class="vue-form-wizard">
      <div class="wizard-header">
        <h4 class="wizard-title">Entity Export</h4>
        <p class="category">Steps to select concept codes, then their properties and export the results</p>
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
              <label for="selectedProperties" id = "SelectProperties2">Select properties to include in the export</label>
            </div>
            <div class="form-group">
              <div class="msl-multi-select">
                <div class="msl-searchable-list msl-multi-select__list">
                  <input placeholder="Search properties" class="msl-search-list-input custom-input-class" id = "searchProperties" @keyup = "searchPropertiesFilter()">
                  <select multiple v-model="leftSelectedUsers" @dblclick="moveRight" class="msl-searchable-list__items" id = "selectSearchProperties">
                    <option v-for="userLeft in availableProperties" :key="userLeft" class="multi-select-option msl-searchable-list__item" id = "optionSearchProperties">
                      {{ userLeft }}
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
                        Selected Properties
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
import {ref} from 'vue'
import 'form-wizard-vue3/dist/form-wizard-vue3.css'


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
    var tagCounter = 0;
    //var newTagCounter = 0;
    //this.entityList = []'


    //
    const addTag = (tag) => {
      var codeDescription = [];
      //var baseLink = document.getElementById('basURL').value;
      tag = tag.replace(/[\s/]/g, '')
      tag = tag.replace(',', '')
      var newTagCounter = 0;
     // alert(baseLink);
     // baseLink = document.getElementById('basURL').value;
      if (tag != "") {
        api.getCodes( "https://evs-dev.cancer.gov/report-exporter/", tag, 'ENTITY')
            .then((data)=> {
             // alert("after call");
              // data = "test";
              if ((data != null) && (data!== undefined)) {
                //alert("after checks");
                for (let x = data.length - 1; x >= 0; x--) {
                //  alert("Code: " + data[x].code + " is invalid: " + data[x].queryStatus + " roles: " + data[x].roles + " association: " + data[x].associations + " Description: " + data[x].name);
                  codeDescription = data[x].name;
                //  alert("before push");
                  tags.value.push(tag + ":" + codeDescription);
                  newTag.value = ""; // reset newTag
                  tagCounter = tagCounter + 1;
                  newTagCounter = newTagCounter + 1;
                  //getEntities();
                //  alert("After getEntities Call()");
                }
              }else {
                alert("Code entered was not found");
                tags.value.push(tag + ":" + "");
                newTag.value = ""
                tagCounter = tagCounter + 1;
                newTagCounter = newTagCounter + 1;
               // selectedConceptCodes.value.push("test");
                //tags.value.push(tag);
                //alert(codeDescription);

              }

            })
      }
    }


    //Vue 3 Remotes a tag below text box
    const removeTag = (index) => {
      tags.value.splice(index, 1);
      tagCounter = tagCounter  - 1;
    };

    /*
      const setSelectedTags = () =>{
        // clear the internal user codes that are entered
        this.userEnteredCodes = []
        for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
          // currated top nodes (from the server hava a value of "C12434:Blood")
          // so we need to strip off everything from the : to the right.
          this.userEnteredCodes.push(this.tags[i].value.split(":",1))
        }
      };
  */


    // called when an entity/code is added
    const onTagAdded = (newTag) =>{
      // Test if the string entered was pasted in - if it has a comma separated
      // list of values
      alert("onTagAdded Called " + newTag);

      //newTag.value.includes(',') ? newTag = this.cleanString(newTag.value).split(",") : newTag = []

      alert("test1 " + newTag.length);
      // if the user entered multiple values, remove the last entry (which
      // is the comma separated string) and add each one individually.

      if (newTag.length > 0) {
        alert("test2a")
        // this.tags.splice(-1,1);  add back
        alert("test2");

        for(let x = newTag.length; x >=0; x-- ) {
          // Make sure we don't add a duplicate.
          // Check if user entered two commas with no entitiy code inbetween them
          // example:  C101171,  ,C101173
          alert("test3");
          if ( (! this.isDuplicateTag(newTag[x])) && (newTag[x] !== undefined) && (newTag[x].length > 0)) {
            // this.tags.value.push({key: newTag[x], value: newTag[x]})
            this.tags.value.push(newTag);
          }
        }
      }
      else {
        if (this.isDuplicateTag(newTag.value))
        {
          alert("test4");
          // remove the last entered entity code
          this.tags.splice(-1,1);
        }
      }

      // When a top node is entered/selected, verify it.
      this.getEntities();
    };




    //Vue 3 Removes all tags below text box
    const removeAllTags = (tagDeleteCounter) => {
      var newTagCounter = 0;
     // alert("REmove value " + tagDeleteCounter);
      for(let i = 0; i<=newTagCounter; i++) {
        tags.value.splice(tagDeleteCounter, newTagCounter);
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




      //document.getElementById("listOfTags").style.display = "none";  // remove tags
      //document.getElementById("listOfTags").innerHTML = "";


      // document.getElementById("selectConceptCodesCount").innerText = 0;
      //  document.getElementById("selectedConceptCodesTags").innerText = "";
      return { tags, newTag, addTag, onTagAdded, removeTag, tagCounter, removeAllTags }
    };





    return { tags, newTag, addTag, onTagAdded, removeTag, tagCounter, removeAllTags }



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
      filename: 'entities',
      downloadReturnCode: null,
      invalidTag: '',
      showSummary: true,
      showSummaryText: '',
      tag: "[]",
      leftSelectedUsers:[],
      leftUsers: [],
      rightSelectedUsers:[],
      rightUsers:[],
      tempListClear:[]
    };
  },

  methods: {
    removeAllTags2 (tagDeleteCounter) {
      // alert("REmove value " + tagDeleteCounter);
      //alert("Counter value " + this.tags.length);
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
      var tagCounter = 0;
      var newTagCounter = 0;
      //const tags = ref([]);
      //const newTag = ref('') //keep up with new tag
      tag = tag.replace(/[\s/]/g, '')
      tag = tag.replace(',', '')


      if (tag != "") {
        api.getCodes( this.$baseURL, tag, 'ENTITY')
            .then((data)=> {
              // alert("after call");
              // data = "test";
              if ((data != null) && (data!== undefined)) {
                for (let x = data.length - 1; x >= 0; x--) {
                  //  alert("Code: " + data[x].code + " is invalid: " + data[x].queryStatus + " roles: " + data[x].roles + " association: " + data[x].associations + " Description: " + data[x].name);
                  codeDescription = data[x].name;
                  this.tags.push(tag + ":" + codeDescription);
                  this.newTag = ""; // reset newTag
                  tagCounter = tagCounter + 1;
                  newTagCounter = newTagCounter + 1;
                  //   alert("Before getEntities Call()");
                  //getEntities();
                  //  alert("After getEntities Call()");
                }
              }else {
                this.tags.push(tag + ":" + "");
                this.newTag = ""
                tagCounter = tagCounter + 1;
                this.$notify({
                  group: 'app',
                  title: 'Validation Failure',
                  text: 'Could not verify concept code(s).  Possible network issue.',
                  type: 'error',
                  duration: 4000,
                  position: "left bottom"
                });

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
      }
    },


    moveRight() {
      if (!this.leftSelectedUsers.length) return;
      for (let i = this.leftSelectedUsers.length; i > 0; i--) {
        let idx = this.availableProperties.indexOf(this.leftSelectedUsers[i-1]);
        this.availableProperties.splice(idx, 1);
        this.rightUsers.push(this.leftSelectedUsers[i - 1]);
        this.tempListClear.push(this.leftSelectedUsers[i - 1]);
        this.leftSelectedUsers.pop();
      }
    },


       // Check for duplicates

    /*
    moveRight() {
      if(!this.leftSelectedUsers.length) return;
      //console.log('moveRight', this.leftSelectedUsers);
      for(let i=this.leftSelectedUsers.length;i>0;i--) {
        let idx = this.leftUsers.indexOf(this.leftSelectedUsers[i - 1]);
        alert("leftUsers Indexof " + idx);
         //this.leftUsers.splice(idx, 1);
        this.leftUsers.splice(0, 1);

        //this.availableProperties.splice(idx, 1);


        this.rightUsers.push(this.leftSelectedUsers[i - 1]);
       // this.selectedProperty.splice(idx, 1);
       // this.leftSelectedUsers.splice(idx, 1);
        //this.selectedProperty.splice(i, 1);
      //  this.leftSelectedUsers.pop();
        this.selectedProperty.pop();
        //this.leftSelectedUsers.refreshData();

      }
      },

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

      api.getProperties(this.$baseURL)
          .then((data)=> {
            for (let x = 0 ; x < data.length; x++) {
              this.availableProperties.push(data[x].name);
            }
          })
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

      //Vue 3 (Builds screen on step 2)
      if (selectNextOptionBTN_counter === 1) {
        if (this.tags.length > 0) {  // checks to make sure that a code was entered before proceeding to next screen
          document.getElementById("clearButton").style.display = "none";    //Hides clear button
          document.getElementById("entityTextID").style.display = "none";   //Hides textbox on main screen
          document.getElementById("entityLabelId").style.display = "none";  //Hides label on main screen
          document.getElementById("SelectProperties1").style.display = "";  //Shows listboxs on second screen
          document.getElementById("backButton").style.display = "";     //Shows back button
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1  // Counter controls navigating between steps 1 -3

          // load properties after the page is loaded.
/*
          api.getProperties(this.$baseURL)
              .then((data)=>{this.availableProperties = data[-1];
              })
*/
         // this.leftSelectedUsers.value = null;

          //this.leftSelectedUsers.pop();
          //this.rightUsers.pop();
          //this.rightUsers.deleteAll();








          //for (let i = 0; i <= this.rightUsers.length + 2; i++) {
          //  this.rightUsers.pop();
         // }


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


      // set the user selected tags and properties
      this.setSelectedPropertyNames()
      this.gaTrackDownload();
      this.setSelectedTags();


      //Vue 3 Sets default value to JSON for Select format for export dropdown on Step 3
      if (this.fileFormat === ""){
        this.userSelectedFormat = 'json';
        this.fileFormat = 'JSON';
        this.selectedExportListName = 'JSON (json) JavaScript Object Notation Format';
      }

      //alert("check 1")
      //alert("base URL: " + this.$baseURL);
      //alert("tags: " + this.userEnteredCodes);
      //alert("selectedPropertyName: " + this.rightUsers);
      //alert("SelectedFormat: " + this.fileFormat);
      //alert("filename: " + this.filename);
      //alert("selectedFormat Extension: " + this.userSelectedFormat);

      //alert (this.queryEntitySelection);
      axios({
        url: this.$baseURL + 'download/get-file-for-readCodes/'  +
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
      //.finally(function() { loader.hide()});
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



  },

  // Vue 3 Start

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