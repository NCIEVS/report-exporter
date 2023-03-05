<!--
Entity Selection plugin.  This plugin wraps the tags-input plugin
This plugin takes concept code input from the user and does the following
checks:
  - Trims the string(s)
  - Verifies there are no duplicates
  - Makes a call to the service to verify the code(s) are valid. If code is
  not valid, it will remove the code and output an error message.

  Plugin input:
    baseURL: String, URL to the service
    rolesRequired: default to false. Determine if concept codes are required to
      have roles.
    associationsRequired: default to false. Determine if concept codes are required to
      have associations.
    queryEntitySelection: default to "ENTITY". Determine the attribute to populate When
      calling the api.getCodes() method.
-->
//alert("this is the value intered: " + onTagAdded(value))";
alert("this is the value entered: " + baseURL);


<template>
  <div>
    <!--
    <label for="tags">Enter NCI Thesaurus concept codes</label>

    <div class="form-group">
      <vue3-tags-input element-id="tags"
                  v-model="selectedTags" placeholder="Type entity code, then click enter"
                  :add-tags-on-comma="true"
                  :add-tags-on-space="true"
                  :add-tags-on-blur="true"
                  :case-sensitive-tags="true"
                  :typeahead="false"
                  @tag-added="value =>onTagAdded(value)"
                  @tag-removed="value =>onTagRemoved(value)"
                  title="remove selected tag">
      </vue3-tags-input>


    </div>
  -->







    <div class="form-group">
      <tags-input element-id="tags"
                  v-model="selectedTags" placeholder="Type entity code, then click enter123"
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




    <!--<button type="button"
            v-on:click="clearSelection"
            class="btn btn-primary mb-5 exportButtons">
            Clear
    </button>-->



    <!--
    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" class="wizard-btn"  id = 'back button' style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Real Back </button>
    </span>
-->
  </div>
</template>

<script>
import api from '../api.js';
// Custom input tags
//import VoerroTagsInput from '@voerro/vue-tagsinput'
//import VoerroTagsInput from 'vue3-tags-input'




export default {



  props: {
    baseURL: { required: true, type: String },
    rolesRequired: { default: true, type: Boolean },
    associationsRequired: { default: true, type: Boolean },
    queryEntitySelection: { default: "ENTITY", type: String }
  },

 // components:
      //{
   // 'vue3-tags-input': VoerroTagsInput,
  //},
//,
  data() {
    return {
      value: ['apple', 'orange', 'grape'],
      selectedTags: [],
      userEnteredCodes: [],
      entityList: [],
      multipleEntitiesSplit: [],
      invalidTag: '',
      userSelectedProperyNames: []
    }
  },


  methods: {

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
      alert("this is the data for onTagAdded2222222: " + newCode.value);
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
        alert("this is the data for onTagAdded else statement: " + newCode.value);
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

    // Update the top node that was entered with the description.
    // User enters "C12434", the updated value displayed will be "C12434:Blood".
    // If entered value is not valid, remove it and display an error message.
    updateSelectedConceptCodeDescriptions(entities){
      // this.selectedTags[0].key = entities[0].code;
      // this.selectedTags[0].value = entities[0].code + ":" + entities[0].name;
      alert("this is the data for entities: " + entities.value);
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

      //alert("this is the data for get entity: " + entityList);
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

    // removes forward slashes and all kinds of Unicode whitespace characters
    cleanString(string) {
      return string.replace(/[\s/]/g, '')
    }
  },

};

</script>

<style>
.clearButton{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}

.btn btn-primary mb-5 exportButtons{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}
</style>
