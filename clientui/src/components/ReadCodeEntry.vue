<template>
  <div id="read-codes-entry">

    <div class="container px-lg-5">
      <div class="row row-cols-1 row justify-content-start row mx-lg-n5 py-5">
         <div class="col py-3 px-lg-5"> </div>
        <div class="col">
          <h5> NCI Thesaurus Entity Codes</h5>
        </div>
        <div class="col">
            <tags-input element-id="tags"
              v-model="selectedTags"
              
              :add-tags-on-comma="true"
              :add-tags-on-space="true"
              :typeahead="false"></tags-input>
        </div>
        <div class="col py-5">
            <button class="btn btn-primary" :disabled='!(Object.keys(this.selectedTags).length>0)' v-on:click="getEntities">Search</button>
        </div>
      </div>

      <div v-if="this.entityList.length"  class="row row-cols-1 row justify-content-start row mx-lg-n5 py-1">
        <table class="table table-hover">
          <thead>
            <tr>
              <th scope="col">Code</th>
              <th scope="col">Preferred Name</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="code in entityList" v-bind:key="code.code">
              <td>{{code.code}}</td>
              <td>{{code.name}}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="row row-cols-1 row justify-content-start row mx-lg-n5 py-1">
        <h3> Select Property to Output </h3>

          <v-multiselect-listbox v-model="selectedProperties" :options="this.availableProperties"
                       :reduce-display-property="(option) => option.name"
                       :reduce-value-property="(option) => option.code"
                       search-input-class="custom-input-class"
                       search-options-placeholder="Search properties"
                       selected-options-placeholder="Search selected properties">
        </v-multiselect-listbox>

      </div>

      <div class="row mx-lg-n5 py-1 ">
          <button class="btn btn-primary" :disabled='!(Object.keys(this.selectedTags).length>0)' v-on:click="downloadFile">Download</button>
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


export default {
  name: 'read-code-entry',
  props: {
    msg: String
  },
  components: {
    'tags-input': VoerroTagsInput,
    'vMultiselectListbox': vMultiselectListbox
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
      filename: 'file.json',
      downloadReturnCode: null,
      baseUrl: ''
    }
  },
  
  methods: {
      setSelectedTags() {
        // clear the internal user codes that are entered
        this.userEnteredCodes = []
          
        for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
          this.userEnteredCodes.push(this.selectedTags[i].value)
        }
      },

      setSelectedPropertyNames() {
        this.userSelectedProperyNames = []

        for (let i = 0; i < Object.keys(this.selectedProperties).length; i++) {
          this.userSelectedProperyNames.push(this.selectedProperties[i].name)
        }
      },

      getEntities(){
        // clear the entry list
        this.entityList = []
        this.setSelectedTags()

        api.getCodes(this.baseUrl, this.userEnteredCodes)
          .then((data)=>{this.entityList = data;
        })
      },

      downloadFile() {

        // set the user selected tags and properties
        this.setSelectedTags()
        this.setSelectedPropertyNames()

          axios({
                url: this.baseUrl + '/download/get-file-for-props/'  + this.userEnteredCodes + '/' + this.userSelectedProperyNames + '/' + this.filename,
                method: 'GET',
                responseType: 'blob',
            }).then((response) => {
                  var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                  var fileLink = document.createElement('a');

                  fileLink.href = fileURL;
                  fileLink.setAttribute('download', this.filename);
                  document.body.appendChild(fileLink);

                  fileLink.click();
            });
          }

    },
    created() {
      // load properties after the page is loaded.
      api.getProperties(this.baseUrl)
          .then((data)=>{this.availableProperties = data;
        })
    }
  }

</script>

<!-- styling for the component -->
<style>


</style>