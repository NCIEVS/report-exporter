<template>
  <div id="read-codes-entry" class="container">

    <!-- WIZARD DECLARATION -->
    <form-wizard
      @on-complete="onComplete"
      step-size="xs"
      title="Entity Export"
      subtitle="Steps to select concept codes, their properties and export the results"
      finish-button-text="Export"
      color="#017ebe">

      <!-- STEP 1: SELECT CODES -->
      <tab-content icon="ti-settings" title="Select Concept Codes for Entity Export"
        :before-change="validateFirstStep">
        <div class="container">
          <div class="container">
              <div class="row justify-content-center">
                 <div class="col-12 col-md-8">
                    <form>
                      <label for="tags">Enter NCI Thesaurus concept codes</label>
                      <div class="form-group">
                          <tags-input element-id="tags"
                            v-model="selectedTags" placeholder="Type entity code, then space"
                            :add-tags-on-comma="true"
                            :add-tags-on-space="true"
                            :case-sensitive-tags="true"
                            :typeahead="false"
                            @tag-added="value =>onTagAdded(value)">
                          </tags-input>
                      </div>
                      <button type="button"  v-on:click="clearSelection"
                        class="btn btn-primary mb-5 exportButtons">Clear</button>
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
      <tab-content icon="ti-download" title="Select Format and Export">
        <div class="container">
            <div class="row justify-content-center">
               <div class="col-12 col-md-6">
                <form ref="formContainer">
                  <div class="form-group">
                    <label for="downloadFormat">Select format for export</label>
                    <v-select element-id="downloadFormat" v-model="userSelectedFormat"
                      :options="this.availableFormats" @input="value =>updateFormat(value)">
                    </v-select>
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
        <div class="card-header" id="headingOne">

            <center>
            <button class="btn btn-link"  v-on:click="this.updateShowSummary" data-toggle="collapse" data-target="#collapseSummary" aria-expanded="true" aria-controls="collapseSummary">
              {{this.showSummaryText}}
            </button>
          </center>

        </div>

        <div id="collapseSummary" class="collapse show" aria-labelledby="headingOne" data-parent="#accordion">
          <div class="card-body">

            <div class="row">
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
                        {{userSelectedExtension}}
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
import vSelect from 'vue-select'
import api from '../api.js';
import axios from 'axios';
import {FormWizard, TabContent} from 'vue-form-wizard';
import 'vue-loading-overlay/dist/vue-loading.css';

export default {
  name: 'read-code-entry',
  props: {
    msg: String
  },
  components: {
    'tags-input': VoerroTagsInput,
    'vMultiselectListbox': vMultiselectListbox,
    'v-select': vSelect,
    FormWizard,
    TabContent
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
      userSelectedFormat: 'JSON',
      filename: 'entities',
      downloadReturnCode: null,
      // baseUrl: 'http://localhost:8080',
      baseUrl: '',
      userSelectedExtension: 'json',
      extensionMap:[
        { id: 'JSON', name: 'json' },
        { id: 'CSV', name: 'csv' },
        { id: 'TABD', name: 'txt' },
        { id: 'EXCEL', name: 'xlsx' }
      ],
      invalidTag: '',
      multipleEntitiesSplit: [],
      showSummary: true,
      showSummaryText: ''
    }
  },

  methods: {
    // Wizard methods
    validateFirstStep() {
      // make sure the user has a code entered
      return Object.keys(this.selectedTags).length>0
    },

    validatePropertyStep() {
      // make sure the user has selected at least one property
      return Object.keys(this.selectedProperties).length>0
    },

    onComplete: function() {
      this.downloadFile();
    },

    updateShowSummary() {
      this.showSummaryText = this.showSummary? 'Hide Selection Summary' : 'Show Selection Summary'
      //
      // if (showSummary) {
      //   showSummaryText = 'Hide Selection Summary'
      // }
      // else {}
      this.showSummary = !this.showSummary;
    },

    // clear all of the entitiy codes in the input selection
    clearSelection() {
      this.userEnteredCodes = []
      this.selectedTags = []
      this.entityList = []
      this.multipleEntitiesSplit = []
      this.invalidTag = ''
    },

    // called when an entity/code is added
    onTagAdded(newCode) {
      //console.log("Added tag: " + newCode.value),

      // Test if the string entered was pasted in - if it has a comma separated
      // list of values
      newCode.value.includes(',') ?
        this.multipleEntitiesSplit = this.cleanString(newCode.value).split(",") :
        this.multipleEntitiesSplit = []

      // if the user entered multiple values, remove the last entry (which
      // is the comma separated string) and add each one individually.
      if (this.multipleEntitiesSplit.length > 0) {
        this.selectedTags.splice(-1,1);

        for(let x=0; x <this.multipleEntitiesSplit.length; x++ ) {
          // make sure we don't add a duplicate.
          if (! this.isDuplicateTag(this.multipleEntitiesSplit[x])) {
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

      updateFormat( format) {
        this.userSelectedFormat = ''
        this.userSelectedFormat = format;

        // find the extension based off the key (user selected format)
        for (let i = 0; i < Object.keys(this.extensionMap).length; i++) {
          if (this.extensionMap[i].id == this.userSelectedFormat) {
            this.userSelectedExtension = this.extensionMap[i].name;
            break;
          }
        }
      },

      // Update the top node that was entered with the description.
      // User enters "C12434", the updated value displayed will be "C12434:Blood".
      // If entered value is not valid, remove it and display an error message.
      updateSelectedConceptCodeDescriptions(entities){
        // this.selectedTags[0].key = entities[0].code;
        // this.selectedTags[0].value = entities[0].code + ":" + entities[0].name;

        //console.log ("Updating entities: " + entities[0].code + "  " + entities[0].name)
        for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
          //console.log ("key " + this.selectedTags[i].key + "  value " + this.selectedTags[i].value)
          this.updateSelectedConceptCodeDescription(this.selectedTags[i], entities);
        }
      },

      updateSelectedConceptCodeDescription(selectedTag, entities) {
        for (let x = 0; x < Object.keys(entities).length; x++) {
            //console.log ("code " + entities[x].code + "  name " + entities[x].name)
            if (selectedTag.value == entities[x].code) {
              selectedTag.value = entities[x].code + ":" + entities[x].name;
              selectedTag.key = entities[x].code;
            }
        }
      },

      getEntities(){
        // clear the entry list
        this.entityList = []
        this.setSelectedTags()

        api.getCodes(this.baseUrl, this.userEnteredCodes)
          .then((data)=>{
            if (data != null) {
              this.entityList = data;
              this.updateSelectedConceptCodeDescriptions(data);
            }
            else {
              // update structure - remove invalid value that was entered
              this.invalidTag = this.selectedTags.splice(-1,1);
              this.userEnteredCodes.splice(-1,1);

              this.$notify({
                group: 'app',
                title: 'Invalid Concept Code',
                //text: 'The concept code <b>' + this.userEnteredCodes[0] +'</b> is not valid. It has been removed.',
                text: 'The concept code <b>' + this.invalidTag[0].value +'</b> is not valid. It has been removed.',
                type: 'error',
                duration: 4000,
                position: "left bottom"
              });
            }
          })
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
        this.setSelectedTags()
        this.setSelectedPropertyNames()

          axios({
                url: this.baseUrl + '/download/get-file-for-readCodes/'  +
                    this.userEnteredCodes + '/' +
                    this.userSelectedProperyNames + '/' +
                    this.userSelectedFormat + '/'+
                    this.filename + '.' +
                    this.userSelectedFormat + '.' + this.userSelectedExtension,
                method: 'GET',
                responseType: 'blob',
            }).then((response) => {
                  var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                  var fileLink = document.createElement('a');

                  fileLink.href = fileURL;
                  fileLink.setAttribute('download', this.filename + '.' + this.userSelectedExtension);
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
      this.updateShowSummary();

      // load properties after the page is loaded.
      api.getProperties(this.baseUrl)
          .then((data)=>{this.availableProperties = data;
        })

      api.getFormats(this.baseUrl)
          .then((data)=>{this.availableFormats = data;
       })
    }
  }

</script>

<!-- styling for the component -->
<style>
/* #read-codes-entry{
  top: 60;
} */

/* Summary list box formatting */
.list-group{
    max-height: 150px;
    min-height: 150px;
    overflow-y:auto;
}

.exportButtons {
  background-color: rgb(1, 126, 190);
  border-color: rgb(1, 126, 190);
  color: white;
}
.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open>.dropdown-toggle.btn-primary {
  background-color: rgb(1, 126, 190);
  border-color: rgb(1, 126, 190);
  color: white;
}
.msl-multi-select {
  /* make the multi-select take up the entire width of the container */
  width: 100%
}
</style>
