<template>
  <div id="read-codes-entry" class="container">

    <!-- WIZARD DECLARATION -->
    <form-wizard
      @on-complete="onComplete"
      step-size="xs"
      title="Search Codes Download"
      subtitle="Steps to select concept codes, their properties and download the results"
      finish-button-text="Download"
      color="#017ebe">

      <!-- STEP 1: SELECT CODES -->
      <tab-content icon="ti-settings" title="Select Concept Codes"
        :before-change="validateFirstStep">
        <div class="container">
          <div class="container">
              <div class="row justify-content-center">
                 <div class="col-12 col-md-6">
                    <form>
                      <div class="form-group">
                        <label for="tags">Enter NCI Thesaurus concept codes</label>

                        <tags-input element-id="tags"
                          v-model="selectedTags" placeholder="Type entity code, then space"
                          :add-tags-on-comma="true"
                          :add-tags-on-space="true"
                          :case-sensitive-tags="true"
                          :typeahead="false"
                          @tag-added="value =>onTagAdded(value)">
                        </tags-input>

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
              <label for="selectedProperties">Select Properties to Output</label>
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
      <tab-content icon="ti-download" title="Select Format and Download">
        <div class="container">
            <div class="row justify-content-center">
               <div class="col-12 col-md-6">
                <form ref="formContainer">
                  <div class="form-group">
                    <label for="downloadFormat">Select Format for Output</label>
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
      filename: 'readCodes',
      downloadReturnCode: null,
      baseUrl: 'http://localhost:8080',
      //baseUrl: '',
      userSelectedExtension: 'json',
      extensionMap:[
        { id: 'JSON', name: 'json' },
        { id: 'CSV', name: 'csv' },
        { id: 'TABD', name: 'txt' },
        { id: 'EXCEL', name: 'xlsx' }
      ],
      curratedTopNodesUI:[],
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
      updateSelectedConceptCodeDescription(topNode){
        this.selectedTags[0].key = topNode[0].code;
        this.selectedTags[0].value = topNode[0].code + ":" + topNode[0].name;

        console.log ("Updating top node: " + topNode[0].code + "  " + topNode[0].name)
        for (let i = 0; i < Object.keys(this.curratedTopNodesUI).length; i++) {
          console.log ("key " + this.curratedTopNodesUI[i].key + "  value " + this.curratedTopNodesUI[i].value)
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
              this.updateSelectedConceptCodeDescription(data);
            }
            else {
              console.log("Error retrieving top node code");
              alert("Invalid Top Node");
              this.selectedTags = [];
              this.getPropertyError=true;
            }
          })
      },

      downloadFile() {
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
        }
    },
    created() {
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
.modal-active{
	display:block;
}
.msl-multi-select {
  /* make the multi-select take up the entire width of the container */
  width: 100%
}
</style>
