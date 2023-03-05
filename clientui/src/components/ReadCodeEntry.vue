
<template>
  <div id="read-codes-entry" class="container">

    <!--Vue 3 Start-->
    <div class="vue-form-wizard">
      <div class="wizard-header">
        <h4 class="wizard-title">Entity Export</h4>
        <p class="category">Steps to select concept codes, then their properties and export the results</p>
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
               @keydown.space="addTag(newTag)"
               @keydown.tab="addTag(newTag)">
        <br>
        <br>
        <div class = "tag-input"></div>
          <ul class="tags" id = "listOfTags">
            <li><a v-for="tag in tags" :key="tag" class="tag" id="tags2">
              {{ tag }}
              <button class="delete" @click="removeTag(index)">x</button>
            </a></li>
          </ul>
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
                    <option v-for="userLeft in leftUsers" :key="userLeft" class="multi-select-option msl-searchable-list__item" id = "optionSearchProperties">
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
                      <td><input type = "button" value = ">>" id = "toListBox" class = "toListBox" @click="moveRight"></td>
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
                      <td><input type = "button" value = "<<" id = "fromListBox" class = "fromListBox" @click="moveLeft"></td>
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
        <button tabindex="-1" type="button" id = "clearButton" class="wizard-btn-delete" v-on:click="removeAllTags(0)"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Clear </button>
      </span>

      <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "nextOption" class="wizard-btn-back" v-on:click="validateFirstStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Back </button>
      </span>

      <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "backButton" class="wizard-btn-next" v-on:click="validateFirstStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Select Next Option </button>
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
                  <ul class="list-group" id="selectedConceptCodesTags">
                    <li v-for="newTag in newTag" :key="newTag.key">
                      {{ tags }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
            <div class="col-sm-4">
              <div class="card bg-light border-dark mb-3">
                <div class="card-header">
                  Selected Properties
                  <span class="badge badge-secondary">{{Object.keys(this.selectedProperties).length}}</span>
                </div>
                <div class="card-body">
                  <ul id="selectedPropertyList" class="list-group">
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
    this.hideListBoxsStepTwo();
  },


  setup(){
    const tags = ref([]);
    const newTag = ref('') //keep up with new tag
    var tagCounter = 0;
    var newTagCounter = 0;


    const addTag = (tag) => {
      tags.value.push(tag);
      newTag.value = ""; // reset newTag
      tagCounter = tagCounter + 1;
      newTagCounter = newTagCounter + 1
    };

    //Vue 3 Remotes a tag below text box
    const removeTag = (index) => {
      tags.value.splice(index, 1);
      tagCounter = tagCounter  - 1;
    };


    //Vue 3 Removes all tags below text box
    const removeAllTags = (tagDeleteCounter) => {
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
      return { tags, newTag, addTag,  removeTag, tagCounter, removeAllTags }
    };

    return { tags, newTag, addTag,  removeTag, tagCounter, removeAllTags }
  },



  data(){
    return {
      selectedTags: [],
      userEnteredCodes: "[]",
      entityList: [],
      availableProperties: [],
      selectedProperties: [],
      userSelectedProperyNames: [],
      userSelectedFormat: {"name":"JSON","description":"JavaScript Object Notation Format", "extension":"json" },
      filename: 'entities',
      downloadReturnCode: null,
      invalidTag: '',
      showSummary: true,
      showSummaryText: '',



      leftSelectedUsers:[],
      leftUsers: [
        "C12219",
        "C12508",
        "C1909",
        "C22187",
        "C2916",
        "C3262",
        "C61410",
      ],
      rightSelectedUsers:[],
      rightUsers:[]

    };
  },

  methods: {

    testCall(){
      alert("test Call");
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
        console.log('moveLeft',this.rightUsers);
        for(let i=this.rightSelectedUsers.length;i>0;i--) {
          let idx = this.rightUsers.indexOf(this.rightSelectedUsers[i-1]);
          this.rightUsers.splice(idx, 1);
          this.leftUsers.push(this.rightSelectedUsers[i-1]);
          this.rightSelectedUsers.pop();
        }
      },
      moveRight() {
        if(!this.leftSelectedUsers.length) return;
        console.log('moveRight', this.leftSelectedUsers);
        for(let i=this.leftSelectedUsers.length;i>0;i--) {
          let idx = this.leftUsers.indexOf(this.leftSelectedUsers[i-1]);
          this.leftUsers.splice(idx, 1);
          this.rightUsers.push(this.leftSelectedUsers[i-1]);
          this.leftSelectedUsers.pop();
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

    hideListBoxsStepTwo() {
      return document.getElementById("SelectProperties1").style.display = "none";
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

      //Vue 3 STEP 1
      if (selectNextOptionBTN_counter === 1) {
        if (this.newTag.length > 0) {  // checks to make sure that a code was entered before proceeding to next screen
          document.getElementById("clearButton").style.display = "none";
          document.getElementById("entityTextID").style.display = "none";
          document.getElementById("entityLabelId").style.display = "none";
          document.getElementById("SelectProperties1").style.display = "";
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1
        }
      }
      if (selectNextOptionBTN_counter === 2) {
        alert("call for step 2");
        this.validatePropertyStep();
      }
      if (selectNextOptionBTN_counter === 3) {
        alert("counter 3 if statement")
        document.getElementById("SelectProperties1").style.display = "none";
        document.getElementById("selectNextOption").style.display = "none";
        this.validateExportStep();
      }
    },

    //Vue 3 STEP 2
    validatePropertyStep() {



      // make sure the user has selected at least one property
     //Hides objects on screen that shouldn't appear in step 2
     // document.getElementById("entityTextID").style.display = " ";
     // document.getElementById("entityLabelId").style.display = " ";


      if (this.rightUsers.length > 0) {
        document.getElementById("entityTextID").style.display = "none";
        document.getElementById("entityLabelId").style.display = "none";
        document.getElementById("SelectProperties1").style.display = "none";
        this.validateExportStep();
        return Object.keys(this.rightUsers).length > 0
      }
    },

    validateExportStep() {
      // make sure there is an export format selected.
      alert("step 3");
      if (this.rightUsers !== null) {
        return this.rightUsers !== null
      }
    },
/*
    onComplete: function() {
      alert("Valid oncomplete test");
      this.downloadFile();
    },
*/
    onComplete() {
      alert("Valid oncomplete test");
      this.downloadFile();
    },



    onFormatUpdated (updatedFormat) {
      this.userSelectedFormat = updatedFormat
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

      for (let i = 0; i < Object.keys(this.selectedProperties).length; i++) {
        this.userSelectedProperyNames.push(this.selectedProperties[i].name)
      }
    },

      // Determine if the user entered entity code is unique
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
        //this.setSelectedTags()
        this.setSelectedPropertyNames()
        this.gaTrackDownload();

          axios({
                url: this.$baseURL + 'download/get-file-for-readCodes/'  +
                    this.userEnteredCodes + '/' +
                    this.userSelectedProperyNames + '/' +
                    this.userSelectedFormat.name  + '/'+
                    this.filename + '.' +
                    this.userSelectedFormat.extension,
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
      api.getProperties(this.$baseURL)
          .then((data)=>{this.availableProperties = data;
      })
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

  // called when an entity/code is added
  onTagAdded(newCode) {
    // Test if the string entered was pasted in - if it has a comma separated
    // list of values
    newCode.value.includes(',') ?
        this.multipleEntitiesSplit = this.cleanString(newCode.value).split(",") :
        this.multipleEntitiesSplit = []

    // if the user entered multiple values, remove the last entry (which
    // is the comma separated string) and add each one individually.
    if (this.multipleEntitiesSplit.length > 0) {
      this.selectedTags.splice(-1,1);


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
  }

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

.wizard-btn-delete{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 277px;
  width: 100px;
}

.wizard-btn-next{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 300px;
  width: 158px;
}

.wizard-btn-back{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 277px;
  width: 158px;
}
</style>
