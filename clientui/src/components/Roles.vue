<template>
  <div id="read-codes-entry" class="container">

    <!-- WIZARD DECLARATION -->
    <form-wizard
      @on-complete="onComplete"
      step-size="xs"
      title="Roles Export"
      subtitle="Steps to select concept codes, then their roles and export the results"
      finish-button-text="Export"
      nextButtonText="Select Next Option"
      color="#017ebe">

      <!-- STEP 1: SELECT CODES -->
      <tab-content icon="ti-settings" title="Select Concept Codes for Roles Export"
        :before-change="validateFirstStep">
        <div class="container">
          <div class="container">
              <div class="row justify-content-center">
                 <div class="col-12 col-md-8">
                    <form ref="formSelectCodes">
                      <label for="tags">Enter NCI Thesaurus concept codes</label>
                      <div class="form-group">
                          <tags-input element-id="tags"
                            v-model="selectedTags" placeholder="Type entity code, then click enter"
                            :add-tags-on-comma="true"
                            :add-tags-on-space="true"
                            :add-tags-on-blur="true"
                            :case-sensitive-tags="true"
                            :typeahead="false"
                            @tag-added="value =>onTagAdded(value)" title="remove selected tag">
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

      <!-- STEP 2: SELECT ROLES -->
      <tab-content icon="ti-view-list-alt" title="Select Roles"
        :before-change="validateRoleStep">

        <div class="container">
          <form>
            <div class="row border-bottom concept-container">
                <div class="col">
                  <label for="unusedCodes">No roles selected for thes concept codes</label>
                  <div class="input-group" id="unusedCodes">
                    <div v-for="code in unusedCodes" v-bind:key="code">
                      <span class="badge badge-pill badge-secondary">{{code}}</span>
                    </div>
                  </div>
                </div>
                <div class="col">
                  <label for="unusedCodes">Used Concept Codes</label>
                  <div class="input-group">
                    <div v-for="code in usedCodes" v-bind:key="code">
                      <span class="badge badge-pill badge-primary">{{code}}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="form-group role-select-container">
                <label for="selectedRoles">Select roles to include in the export</label>
              </div>
              <div class="form-group">
                <v-multiselect-listbox :key="componentKey" v-model="selectedRoles" :options="this.availableRoles"
                    v-on:change="updateUsedConceptCodes()"
                    :reduce-display-property="(option) => option.type"
                    :reduce-value-property="(option) => option.relatedCode"
                    search-input-class="custom-input-class"
                    search-options-placeholder="Search roles"
                    selected-options-placeholder="Search selected roles"
                    no-options-text="No roles"
                    selected-no-options-text="No roles selected"
                    no-options-found-text="No roles found"
                    no-selected-options-found-text="No selected roles found">
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
                  <div class="card-header">Selected Roles <span class="badge badge-secondary">{{Object.keys(this.selectedRoles).length}}</span></div>
                  <div class="card-body">

                    <ul class="list-group" id="selectedRoleList">
                      <li v-for="selectedRole in selectedRoles" :key="selectedRole.type">
                        {{ selectedRole.type }}
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <div class="col-sm-4">
                <div class="card bg-light border-dark mb-3">
                  <div class="card-header">Selected Export Format</div>
                  <div class="card-body">
                    <ul class="list-group" id="selectedRoleList">
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
import api from '../api.js';
import axios from 'axios';
import {FormWizard, TabContent} from 'vue-form-wizard';
import 'vue-loading-overlay/dist/vue-loading.css';

export default {
  name: 'report-code-entry',

  components: {
    'tags-input': VoerroTagsInput,
    'vMultiselectListbox': vMultiselectListbox,
    FormWizard,
    TabContent
  },
  metaInfo: {
    title: 'EVS Report Exporter - Roles',
  },
  data(){
    return {
      componentKey: 0,
      selectedTags: [],
      userEnteredCodes: [],
      usedCodes: [],
      unusedCodes: [],
      entityList: [],
      availableRoles: [],
      selectedRoles: [],
      userSelectedRoleNames: [],
      availableFormats: [],
      userSelectedFormat: {"name":"JSON","description":"JavaScript Object Notation Format","extension":"json"},
      filename: 'entities',
      downloadReturnCode: null,
      invalidTag: '',
      multipleEntitiesSplit: [],
      showSummary: true,
      showSummaryText: ''
    }
  },

  methods: {
    gaTrackDownload () {
      // Send Google analytics download event
      this.$gtag.query('event', "Roles Download", {
         'event_category': "Download",
         'event_label': this.userSelectedFormat.name
      })
    },
    forceRerender() {
      this.componentKey += 1;
    },
    // Wizard methods
    validateFirstStep() {
      // make sure the user has a code entered
      if (Object.keys(this.selectedTags).length>0) {
        this.forceRerender();

        this.selectedItems = []
        this.availableRoles = []
        this.selectedRoles = []
        this.userSelectedRoleNames = []
        this.usedCodes = [];
        this.unusedCodes = [];

        // get the roles based on the codes selected for the next wizard page
        this.getRoles()
        // reset what concept codes are used
        this.updateUsedConceptCodes()
      }
      return Object.keys(this.selectedTags).length>0
    },

    validateRoleStep() {
      // make sure the user has selected at least one role
      return Object.keys(this.selectedRoles).length>0
    },

    validateExportStep() {
      // make sure there is an export format selected.
      return this.userSelectedFormat !== null
    },

    onComplete: function() {
      this.downloadFile();
    },

    updateUsedConceptCodes() {
      this.usedCodes = [];
      this.unusedCodes = [];

      // loop through all concept codes
      for(let x = this.entityList.length; x >=0; x-- ) {
        if (this.entityList[x]) {

          if (this.selectedRoles.length == 0) {
            this.unusedCodes.push(this.entityList[x].code);
          }

          else {
            // for each concept code, loop through its roles
            for(let i = this.selectedRoles.length; i >=0; i-- ) {
              if (this.selectedRoles[i]) {

                // check if the selected role is associated to the concept code
                // if it is, add concept code
                const roles = this.entityList[x].roles;

                if (roles.some(item => item.type === this.selectedRoles[i].type)) {
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
      this.selectedRoles = []
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

    setSelectedTags() {
      // clear the internal user codes that are entered
      this.userEnteredCodes = []
      for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
        // currated top nodes (from the server hava a value of "C12434:Blood")
        // so we need to strip off everything from the : to the right.
        this.userEnteredCodes.push(this.selectedTags[i].value.split(":",1))
      }
    },

    setSelectedRoleNames() {
      this.userSelectedRoleNames = []

      for (let i = 0; i < Object.keys(this.selectedRoles).length; i++) {
        this.userSelectedRoleNames.push(this.selectedRoles[i].type)
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
        var tempCode = ''
        var tempStatus = ''

        // show the busy indicator
        let loader = this.$loading.show({
            container: this.$refs.formSelectCodes,
            loader: 'dots',
            isFullPage: false,
          });

        api.getCodes(this.$baseURL, this.userEnteredCodes)
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
                      text: '<b>' +tempCode+'</b> is not valid. Reason: ' +tempStatus+ '.',
                      type: 'error',
                      duration: 6000,
                      position: "left bottom"
                    });
                  }
                  // The concept code must have roles to be valid
                  else if (data[x].roles.length < 1) {
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
                      title: 'Invalid Concept Code',
                      text: '<b>' +tempCode+'</b> is not valid. Reason: No Roles for this concept code.',
                      type: 'error',
                      duration: 6000,
                      position: "left bottom"
                    });
                  }
              }

              this.entityList = data;
              this.updateSelectedConceptCodeDescriptions(data);
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

      getRoles() {
        // load roles for the selected codes
        api.getRoles(this.$baseURL, this.userEnteredCodes)
            .then((data)=>{this.availableRoles = data;
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

        // set the user selected tags and Roles
        this.setSelectedTags()
        this.setSelectedRoleNames()

        this.gaTrackDownload();

          axios({
                url: this.$baseURL + 'download/get-file-for-resolved-roles/'  +
                    this.userEnteredCodes + '/' +
                    this.userSelectedRoleNames + '/' +
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

      api.getFormats(this.$baseURL)
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

.concept-container{
  padding-bottom: 10px;
}
.role-select-container{
  padding-top: 10px;
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
