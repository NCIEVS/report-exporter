
<template>
  <div id="read-codes-entry" class="container">


    <div class="vue-form-wizard">
      <div class="wizard-header">
        <h4 class="wizard-title">Roles Export</h4>
        <p class="category">Steps to select concept codes, then their roles and export the results</p>
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
          <button class="delete" @click="removeTag(tags, tag, tags.length)">x</button>
        </a></li>
      </ul>
    </div>
    <!--Vue 3 Entity Text field  End-->

    <!--Vue 3 step 2 list boxes Start -->
    <div id="app" class="container">
      <div role="tabpanel" id="SelectProperties1" aria-labelledby="step-SelectProperties1" class="wizard-tab-container" style>
        <div class="container">
          <form>
            <div class="form-group">
              <div class="col">
                <label for="selectedProperties" id = "SelectProperties2">No roles selected for these concept codes</label>
                <br>
                <label for="selectedProperties" id = "enteredCodeLabelLeft">{{ this.userEnteredCodes }}</label>
                <br>
                <label for="selectedProperties" id = "SelectProperties2">Select roles to include in the export</label>
              </div>
            </div>
            <div class="form-group">
              <div class="msl-multi-select">
                <div class="msl-searchable-list msl-multi-select__list">
                  <input placeholder="Search properties" class="msl-search-list-input custom-input-class" id = "searchProperties" @keyup = "searchPropertiesFilter()">
                  <select multiple v-model="leftSelectedOptions" @dblclick="moveRight" class="msl-searchable-list__items" id = "selectSearchProperties">
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
                    <label for="selectedProperties" id = "enteredCodeLabelRight" class = "UsedCodeRight">{{ this.userEnteredCodes }}</label>
                    <br>
                    <br>
                  </div>
                  <input placeholder="Search selected properties" class="msl-search-list-input custom-input-class"  id = "selectedProperties" @keyup = "searchSelectedPropertiesFilter()" >
                  <select multiple v-model="rightSelectedOptions" @dblclick="moveLeft" class="msl-searchable-list__items" id = "selectSelectedProperties">
                    <option v-for="optionRight in rightOptions" :key="optionRight" class="multi-select-option msl-searchable-list__item" id = "optionSelectedProperties">
                      {{ optionRight }}
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


    <!--Vue 3 buttons start-->
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
    <!--Vue 3 buttons  End-->
    <center><VueSpinner id = "waitTimeIndicator" size="40" color="blue" /></center>
    <br>
    <br>


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
                        Selected Roles
                        <span class="badge badge-secondary">{{Object.keys(this.rightOptions).length}}</span>
                      </div>
                      <div class="card-body">
                        <span class="list-group" id="selectedPropertyList">{{ this.rightOptions }}</span>
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


import api from '../api.js'
import axios from 'axios'
import {ref} from 'vue'
import 'form-wizard-vue3/dist/form-wizard-vue3.css'
import {VueSpinner} from  'vue3-spinners';

//vue 3 counter for (Select Next Option) button due to form-wizard not working
let selectNextOptionBTN_counter =  1;


export default {


  name: 'read-code-entry',
  props: {
    msg: String
  },
  components: {
    VueSpinner
  },
  metaInfo: {

    title: 'EVS Report Exporter - Read Code',
  },


  mounted() {
    this.hideObjectsOnScreen();  //function for when page loads certain objects like buttons or text boxes will be hidden
    this.selectedExportListName = "JSON (json) JavaScript Object Notation Format"
    document.getElementById("waitTimeIndicator").style.display = "none"; // Hide Wait time indicator when system loads
  },


  setup() {
    const tags = ref([]);
    const newTag = ref('') //keep up with new tag
    var removeTagIndex = 0;

    //Vue 3 Remotes a tag below text box
    const removeTag = (allTags, selectedTag, tagLength) => {
      for (let x = 0; x < tagLength; x++) {
        if (selectedTag === allTags[x]) {
          removeTagIndex = x
        }
      }
      tags.value.splice(removeTagIndex, 1);
    };

    return {tags, newTag, removeTag}
  },


  data() {
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
      filename: 'roles',
      invalidTag: '',
      showSummary: true,
      showSummaryText: '',
      tag: "[]",
      leftSelectedOptions:[],
      rightSelectedOptions:[],
      rightOptions:[],
      tempListClear:[],
      tagCounter: 0,
      newTagCounter: 0,
      multipleEntitiesSplit: [],
      detectComma: '',
      tagsArray: []
    };
  },

  methods: {

    //Vue 3 Removes all blue tags under text box
    removeAllTags2(tagDeleteCounter) {
      for (let i = 0; i <= this.tags.length; i++) {
        this.tags.splice(tagDeleteCounter, this.tags.length);
        tagDeleteCounter = tagDeleteCounter + 1;
      }
      this.tag = []
      this.newTag = []
      this.userEnteredCodes = []
      this.selectedTags = []
      this.entityList = []
      this.invalidTag = ''
      this.userSelectedProperyNames = []
      this.tags2 = []
    },

    //Vue 3 Code registers what entity code was entered in the text box then calls a api to return the code and description combo
    //in a blue tag below the text box
    addTag1(tag) {
      //Detects if a comma was entered for the code search which would indicate multiple codes were entered.
      //Different logic would need to get used if that occurs
      this.detectComma = tag.search(',')


      if (this.detectComma > 0) {
        this.tagsArray = tag
        this.multipleEntitiesSplit = this.tagsArray.split(',');


        for (let i = 0; i < this.multipleEntitiesSplit.length; i++) {
          this.processTag(this.multipleEntitiesSplit[i])
        }
      } else {
        this.processTag(tag)
      }
    },

    processTag(tag) {
      var codeDescription = []; // Vue 3 temporary variable used for the entity code description
      var dupTagCheck = false;  // Vue 3 temporary variable used to make sure duplicate blue tags are not created
      var tempStatus = ''
      tag = tag.replace(/[\s/]/g, '')

      this.setSelectedTags()

      for (let i = 0; i < this.userEnteredCodes.length; i++) {  //Vue 3 checks for duplicate codes
        if (this.userEnteredCodes[i] === tag) {
          dupTagCheck = true;
        }
      }
      //Vue 3 checks entity code entered and returns a description if on is available
      if (tag != "") {
        document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
        this.WaitTimeIndicatorPause()

        api.getCodes(this.$baseURL, tag, 'ROLE')
            .then((data) => {

              if ((data !== null) && (data !== undefined) && (data !== "")) {  //Vue 3 check if rest api does not return any results
                for (let x = data.length - 1; x >= 0; x--) {  //Vue 3 loop through data returned from rest api
                  if ((data[x].name.length > 0) && (data[x].name != null)) {
                    if (dupTagCheck === true) {
                      this.newTag = [];
                      dupTagCheck = false;
                    } else {
                      if (data[x].roles.length < 1) {
                        this.$notify({
                          group: 'app',
                          title: 'Warning',
                          text: '"' + tag + '" will not appear in the report. Reason: "No Roles for this concept code".',
                          type: 'error',
                          duration: 6000,
                          position: "left bottom"
                        });
                      } else {
                        codeDescription = data[x].name;
                        this.tags.push(tag + ":" + codeDescription);   //Vue 3 adds entity code and description ex (C12219:Anatomic Structure System or Substance) in blue tag under text box
                        this.newTag = ""; // reset newTag
                        this.tagCounter = this.tagCounter + 1;
                        this.newTagCounter = this.newTagCounter + 1;
                        this.setSelectedTags()
                      }
                    }
                  } else {
                    tempStatus = data[x].queryStatus
                    //this.tags.push(tag + ":" + "");   //Vue 3 used for testing take out after testing
                    //this.newTag = "";                  //Vue 3 used for testing take out after testing
                    //this.tagCounter = this.tagCounter + 1;  //Vue 3 used for testing take out after testing

                    //Vue 3 error message if invalid entity code is entered
                    this.$notify({
                      group: 'app',
                      title: 'Invalid Concept Code',
                      text: '"' + tag + '" is not valid. Reason: "' + tempStatus + '".',
                      type: 'error',
                      duration: 6000,
                      position: "left bottom"
                    });
                  }
                }
              } else {
                if (dupTagCheck === true) {
                  this.newTag = [];
                  dupTagCheck = false;
                } else {
                  //this.tags.push(tag + ":" + "");   //Vue 3 used for testing take out after testing
                  //this.newTag = ""                  //Vue 3 used for testing take out after testing
                  //this.tagCounter = this.tagCounter + 1;  //Vue 3 used for testing take out after testing

                  //Vue 3 error message if invalid entity code is entered
                  this.$notify({
                    group: 'app',
                    title: 'Invalid Concept Code',
                    text: '"' + tag + '" is not valid. Reason: "' + tempStatus + '".',
                    type: 'error',
                    duration: 6000,
                    position: "left bottom"
                  });
                }
              }
            })
      }
    },


    // Vue 3 this method takes the code description combo ex. (C12219:Anatomic Structure System or Substance) and returns only the code ex (C12219)
    setSelectedTags() {
      var bottomTab = "";
      var indexBottomTab = 0;
      // clear the internal user codes that are entered
      this.userEnteredCodes = []
      for (let i = 0; i < Object.keys(this.tags).length; i++) {
        // currated top nodes (from the server hava a value of "C12434:Blood")
        // so we need to strip off everything from the : to the right.
        if (this.tags[i] !== "undefined") {
          bottomTab = this.tags[i];
          indexBottomTab = bottomTab.indexOf(":");
          this.userEnteredCodes.push(bottomTab.slice(0, indexBottomTab));
        }
      }
    },


    //Vue 3 move data from right list box on second screen to left list box on second screen
    moveLeft() {
      if(!this.rightSelectedOptions.length) return;
      for(let i=this.rightSelectedOptions.length;i>0;i--) {
        let idx = this.rightOptions.indexOf(this.rightSelectedOptions[i-1]);
        this.rightOptions.splice(idx, 1);
        this.availableProperties.push(this.rightSelectedOptions[i - 1])
        this.rightSelectedOptions.pop();
        document.getElementById("enteredCodeLabelLeft").style.display = "";
        document.getElementById("enteredCodeLabelRight").style.display = "none";
      }
    },

    //Vue 3 move data from left list box on second screen to right list box on second screen
    moveRight() {
      if (!this.leftSelectedOptions.length) return;
      for (let i = this.leftSelectedOptions.length; i > 0; i--) {
        let idx = this.availableProperties.indexOf(this.leftSelectedOptions[i-1]);
        this.availableProperties.splice(idx, 1);
        this.rightOptions.push(this.leftSelectedOptions[i - 1]);
        this.tempListClear.push(this.leftSelectedOptions[i - 1]);
        this.leftSelectedOptions.pop();
        document.getElementById("enteredCodeLabelLeft").style.display = "none";
        document.getElementById("enteredCodeLabelRight").style.display = "";
      }
    },


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

    gaTrackDownload() {
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



      //Vue 3 STEP 1
      if (selectNextOptionBTN_counter === 3) {
        document.getElementById("waitTimeIndicator").style.display = "";  //Show wait time indicator
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextButton").style.display = ""; //Hides next button
        this.WaitTimeIndicatorPause()
      }

      if (selectNextOptionBTN_counter === 2) {
        this.validatePropertyStep();
      }

      if (selectNextOptionBTN_counter === 1) {
        this.setSelectedTags()
        this.getRoles()
        if (this.tags.length > 0) {  // checks to make sure that a code was entered before proceeding to next screen
          document.getElementById("waitTimeIndicator").style.display = "";  //Show wait time indicator
          document.getElementById("clearButton").style.display = "none";    //Hides clear button
          document.getElementById("entityTextID").style.display = "none";   //Hides textbox on main screen
          document.getElementById("entityLabelId").style.display = "none";  //Hides label on main screen
          document.getElementById("SelectProperties1").style.display = "";  //Shows listboxs on second screen
          document.getElementById("backButton").style.display = "";     //Shows back button
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1
          this.WaitTimeIndicatorPause()

          if (this.rightOptions.length <= 0) {
            document.getElementById("enteredCodeLabelLeft").style.display = "";
            document.getElementById("enteredCodeLabelRight").style.display = "none";
          }
        }
      }
    },

    getRoles() {
      this.availableProperties = []

      api.getRoles(this.$baseURL, this.userEnteredCodes)
          .then((data) => {
            for (let x = data.length - 1; x >= 0; x--) {
              this.availableProperties.push(data[x].type);
            }
          })
    },

    //Vue 3 STEP 2
    validatePropertyStep() {
      // make sure the user has selected at least one property
      //Hides objects on screen that shouldn't appear in step 2

      if (this.rightOptions.length > 0) {
        document.getElementById("waitTimeIndicator").style.display = "";  //Show wait time indicator
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextOption").style.display = "none"; //Hides next option button
        this.WaitTimeIndicatorPause()

        if (Object.keys(this.rightOptions).length > 0) {
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1;
          return Object.keys(this.rightOptions).length > 0
        }
      }
    },

    //Vue 3 back button
    backStep(){
      //Shows screen for step 1
      if (selectNextOptionBTN_counter === 2) {
        this.rightOptions = []
        document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
        document.getElementById("SelectProperties1").style.display = "none";  //shows listboxs on second screen
        document.getElementById("clearButton").style.display = "";    //Shows clear button
        document.getElementById("entityTextID").style.display = "";   //Shows textbox on main screen
        document.getElementById("entityLabelId").style.display = "";  //Shows label on main screen
        document.getElementById("backButton").style.display = "none"; //Hides back button on main screen
        document.getElementById("nextOption").style.display = "";     //Shows next button
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;
        this.WaitTimeIndicatorPause()
      }

      //Shows screen for step 2
      if (selectNextOptionBTN_counter === 3) {
        document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
        document.getElementById("SelectProperties1").style.display = "";  //shows listboxs on second screen
        document.getElementById("backButton").style.display = "";     //Shows back button on main screen
        document.getElementById("exportButton").style.display = "none"; //Hides Export button
        document.getElementById("nextOption").style.display = ""; //Hides next button
        document.getElementById("exportStep").style.display = "none";  //Hides Export Step
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;
        this.WaitTimeIndicatorPause()
      }
    },

    //Code provides a small delay so that the spinner circle shown to the end user when processing a task could be visible
    async WaitTimeIndicatorPause () {
      setTimeout(() => {
        document.getElementById("waitTimeIndicator").style.display = "None";  //hides wait time indicator
      }, 500);
    },

    exportStep() {
      this.downloadFile();
    },

    // Toggle the Show/Hide Selection Summary title
    updateShowSummary() {
      this.showSummaryText = this.showSummary? 'Hide Selection Summary' : 'Show Selection Summary'
      this.showSummary = !this.showSummary;

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


    //Method is for the file download
    downloadFile() {
      document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
      this.$notify({
        group: 'download',
        title: 'Export in Progress',
        text: 'Your export is running.  Please wait.',
        type: 'success',
        duration: 2000,
        position: "bottom left"
      });


      // set the user selected tags and properties
      this.gaTrackDownload();


      //Vue 3 Sets default value to JSON for Select format for export dropdown on Step 3
      if (this.fileFormat === ""){
        this.userSelectedFormat = 'json';
        this.fileFormat = 'JSON';
        this.selectedExportListName = 'JSON (json) JavaScript Object Notation Format';
      }

      //alert("base URL: " + this.$baseURL);
      //alert("tags: " + this.userEnteredCodes);
      //alert("selectedPropertyName: " + this.rightOptions);
      //alert("SelectedFormat: " + this.fileFormat);
      //alert("filename: " + this.filename);
      //alert("selectedFormat Extension: " + this.userSelectedFormat);

      //alert (this.queryEntitySelection);
      axios({
        url: this.$baseURL + 'download/get-file-for-resolved-roles/'  +
            this.userEnteredCodes + '/' +
            this.rightOptions + '/' +
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
      })
      //.finally(function() { loader.hide()});
      this.WaitTimeIndicatorPause()
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
    api.getFormats(this.$baseURL)
        .then((data)=>{this.availableFormats = data;
        })
  },
}


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