<template>
  <div id="resolve-branch-entry">
    Resolve Branch Entry 

    <div class="container px-lg-5">
      <div class="row row-cols-1 row justify-content-start row mx-lg-n5 py-5">
         <div class="col py-3 px-lg-5"> </div>
        <div class="col">
          <h5> NCI Thesaurus Top Node Codes</h5>
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

        <div>
          <h3> Select Format for Output </h3>
          <v-select
            :options="this.availableFormats" @input="value =>updateFormat(value)">
          </v-select>
        </div>

      </div>



      <div class="row mx-lg-n5 py-1 ">
          <button class="btn btn-primary" 
            :disabled='!(Object.keys(this.selectedTags).length>0 && Object.keys(this.selectedProperties).length>0 && this.userSelectedExtension.length > 0)' 
            v-on:click="downloadFile">Download</button>
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

export default {
  name: 'resolve-branch-entry',
  props: {
    msg: String
  },
  components: {
    'tags-input': VoerroTagsInput,
    'vMultiselectListbox': vMultiselectListbox,
    'v-select': vSelect
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
      filename: 'resolveBranch',
      downloadReturnCode: null,
      baseUrl: '',
      userSelectedExtension: '',
      extensionMap:[
        { id: 'JSON', name: 'json' },
        { id: 'CSV', name: 'csv' },
        { id: 'TABD', name: 'txt' },
        { id: 'EXCEL', name: 'xslx' }
      ]
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
                url: this.baseUrl + '/download/get-file-for-resolved-branch/'  + 
                    this.userEnteredCodes + '/' + 
                    this.userSelectedProperyNames + '/0/' + 
                    this.userSelectedFormat + '/' + 
                    this.filename + '.' + this.userSelectedExtension,
                method: 'GET',
                responseType: 'blob',
            }).then((response) => {
                  var fileURL = window.URL.createObjectURL(new Blob([response.data]));
                  var fileLink = document.createElement('a');

                  fileLink.href = fileURL;
                  fileLink.setAttribute('download', this.filename + '.' + this.userSelectedExtension);
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

     api.getFormats(this.baseUrl)
          .then((data)=>{this.availableFormats = data;
       })
    }
  }
</script>

<!-- styling for the component -->
<style>
/* #resolve-branch-entry {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
} */
</style>