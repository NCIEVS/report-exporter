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


    <!-- WIZARD DECLARATION -->
    <form-wizard
      @on-complete="onComplete"
      step-size="xs"
      title=""
      subtitle=""
      finish-button-text="Export"
      nextButtonText="Select Next OptionNNN"
      color="#017ebe">




      <!--Vue 3 Entity Label field  Start-->
      <div class="entityLabel" id = "entityLabelId">
      <label for="tags" >Enter NCI Thesaurus concept codes</label>
      </div>
      <!--Vue 3 Entity Label field  End-->

      <!--Vue 3 Entity Text field  Start-->
      <div class="entityText" id = "entityTextID" element-id="tags">
        <input placeholder="Type entity code, then click enter Other"
               :add-tags-on-comma="true"
               :add-tags-on-space="true"
               :add-tags-on-blur="true"
               :case-sensitive-tags="true"
               :typeahead="false"
               @tag-added="value =>onTagAdded(value)"
               @tag-removed="value =>onTagRemoved(value)"
               class="entityCodeInput" v-model="selectedTags">
      </div>
      <!--Vue 3 Entity Text field  End-->





      <div>
        <div class="tags-input-wrapper-default tags-input">
          <label for="tags-pills">Enter tags Other</label>
          <tags-input element-id="tags"
                      input-id="tags-pills"
                      v-model="selectedTags"
                      tag-variant="primary"
                      tag-pills
                      size="lg"
                      separator=" "
                      placeholder="Enter new tags separated by space 456"
          ></tags-input>

        </div>
      </div>






      <div class="entityText" id = "entityTextID2" element-id="tags">
      <tags-input element-id="tags"
                  placeholder="Type entity code, then click enter123"
                  :add-tags-on-comma="true"
                  :add-tags-on-space="true"
                  :add-tags-on-blur="true"
                  :case-sensitive-tags="true"
                  :typeahead="false"

                  @tag-added="value =>onTagAdded(value)"
                  @tag-removed="value =>onTagRemoved(value)"
                  title="remove selected tag">
      </tags-input>
      </div>


      <v-text-field
          value="Select the configuration:"
          color="grey lighten-43"
          class="text--darken-3 mt-3 text-xs-center"
          outline
          readonly
          single-line
      ></v-text-field>

      <!-- STEP 1: SELECT CODES -->

      <tab-content icon="ti-settings" title="Select Concept Codes for Entity Export"
        :before-change="validateFirstStep">
        <div class="container">
          <div class="container">
              <div class="row justify-content-center">
                 <div class="col-12 col-md-8">
                    <form ref="formSelectCodes">


                      <div class="form-group">
                        <entity-selection

                           :baseURL=this.$baseURL
                           :rolesRequired=false
                           queryEntitySelection="ENTITY"
                          @entitesUpdated= "onEntitiesUpdated">
                       </entity-selection>

                      </div>

                    </form>

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
        <div ref="formContainer" class="container">
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
        </div>
      </tab-content>



    </form-wizard>



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
                  <span class="badge badge-secondary">{{Object.keys(this.selectedTags).length}}</span>
                </div>


                <div class="card-body">
                  <ul class="list-group" id="selectedTagList">
                    <li v-for="selectedTag in selectedTags" :key="selectedTag.key">
                      {{ selectedTag.value }}
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








        <div id="collapseSummary" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
          <div class="card-body pb-1">

            <div class="row p-1">
              <div class="col-sm-4">
                <div class="card bg-light border-dark mb-3">
                  <div class="card-header">Selected Concept Codes <span class="badge badge-secondary">{{Object.keys(this.selectedTags).length}}</span></div>
                  <div class="card-body">
                    <ul class="list-group" id="selectedTagList">
                    <li v-for="selectedTag in selectedTags" :key="selectedTag.key">
                      {{ selectedTag.value }}
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
//import vMultiselectListbox from 'vue-multiselect-listbox'
import vMultiselectListbox from 'vue-multiselect-listbox-v2'
import api from '../api.js'
import axios from 'axios'
//import {FormWizard, TabContent} from 'vue-form-wizard'
import {FormWizard, TabContent} from 'vue3-form-wizard'
//import FormWizard from 'form-wizard-vue3'
//import {FormWizard, TabContent} from 'form-wizard-vue3'
//import TabContent from 'vue3-form-wizard'
//import TabContent from 'form-wizard-vue3'


//import FormWizard from 'form-wizard-vue3'
//mport FormWizard from 'vue3-form-wizard'
//import TabContent from 'vue3-form-wizard'

import 'form-wizard-vue3/dist/form-wizard-vue3.css'

//import 'vue-loading-overlay/dist/vue-loading.css'
import ExportFormat from './ExportFormat.vue'
import EntitySelection from './EntitySelection.vue'

//vue 3 counter for (Select Next Option) button due to form-wizard not working
let selectNextOptionBTN_counter =  0;

export default {


  name: 'read-code-entry',
  props: {
    msg: String
  },
  components: {
    'vMultiselectListbox': vMultiselectListbox,
    FormWizard,
    TabContent,
    ExportFormat,
    EntitySelection
  },
  metaInfo: {

    title: 'EVS Report Exporter - Read Code',
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
      showSummaryText: ''
    }
  },

  methods: {
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


      selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1;

      if (selectNextOptionBTN_counter === 1) {
        alert("Validation test and check string length: " + Object.keys(this.selectedTags).values());
        alert("button Counter " + selectNextOptionBTN_counter);
        //Object.keys(this.selectedTags).values() = 'test345';
        alert(Object.keys(this.selectedTags).length);
        //return Object.keys(this.selectedTags).length>0
        return true
      }
      if (selectNextOptionBTN_counter === 2) {
        alert("call for step 2");
        this.validatePropertyStep();
      }
      if (selectNextOptionBTN_counter === 3) {
        alert("counter 3 if statement")
        this.validateExportStep();
      }
    },


    validatePropertyStep() {



      // make sure the user has selected at least one property
      alert("step 2");
      //Object.defineProperties("entityTest").hide();
      document.getElementById("entityTextID").style.display = "none";
      document.getElementById("entityLabelId").style.display = "none";
      alert("step 2vvv");
     // document.getElementById("entityTextID").style.display = " ";
     // document.getElementById("entityLabelId").style.display = " ";


      alert("step 2WWW");


      return Object.keys(this.selectedProperties).length>0
    },

    validateExportStep() {
      // make sure there is an export format selected.
      alert("step 3");
      return this.userSelectedFormat !== null
    },

    onComplete: function() {
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
  padding-left: 207px;
}

.entityLabel{
  padding-left: 207px;
}



</style>
