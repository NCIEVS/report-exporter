<!--
Export Format plugin.  This plugin displays a list of valid formats for
exporting reports.  These formats are retrieved from the service.

  Plugin input:
    baseURL: String, URL to the service

-->

<template>
  <div>
    <label for="downloadFormat">Select format for export</label>
    <select v-model="userSelectedFormat"
         id="downloadFormat"
         class="form-control"
         v-on:change="$emit('formatUpdated', userSelectedFormat)">
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
</template>

<script>
  import api from '../api.js';

  export default {
    props: {
      baseURL: { required: true, type: String }
    },

    data() {
      return {
        selected: "selected",
        availableFormats: [],

        userSelectedFormat: {
          "name":"JSON",
          "description":"JavaScript Object Notation Format",
          "extension":"json"},
        };
    },

    methods: {
      getDownloadFormats: function () {
        api.getFormats(this.baseURL)
           .then((data)=>{this.availableFormats = data;
        })
      },
    },

    beforeMount() {
      this.getDownloadFormats()
    },

  };
</script>
