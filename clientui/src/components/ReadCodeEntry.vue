<template>
  <div id="read-codes-entry" class="container">




    <!-- WIZARD DECLARATION -->
    <form-wizard
      @on-complete="onComplete"
      step-size="xs"
      title="Entity Export"
      subtitle="Steps to select concept codes, then their properties and export the results"
      finish-button-text="Export"
      nextButtonText="Select Next Option"
      color="#017ebe">

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
import vMultiselectListbox from 'vue-multiselect-listbox'
import api from '../api.js'
import axios from 'axios'
//import {FormWizard, TabContent} from 'vue-form-wizard'
import {FormWizard, TabContent} from 'vue3-form-wizard'
//import {FormWizard, TabContent} from 'form-wizard-vue3'
import 'form-wizard-vue3/dist/form-wizard-vue3.css'

//import 'vue-loading-overlay/dist/vue-loading.css'
import ExportFormat from './ExportFormat.vue'
import EntitySelection from './EntitySelection.vue'

export default {
 //install(app) {
 //   app.component('form-wizard', FormWizard)
 // },

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
      userEnteredCodes: [],
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

    onFormatUpdated (updatedFormat) {
      this.userSelectedFormat = updatedFormat
    },

    onEntitiesUpdated(updatedTags, updatedEntityCodes, userSelectedProperyNames,userEnteredCodes) {
      this.selectedTags = updatedTags
      this.entityList = updatedEntityCodes
      this.userSelectedProperyNames = userSelectedProperyNames
      this.userEnteredCodes = userEnteredCodes
    },

    updateShowSummary() {
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
    }
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
</style>
