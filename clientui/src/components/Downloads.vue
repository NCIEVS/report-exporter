<template>
  <div id="downloads">
    <div class="container">
      <div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
        <h1 class="display-6">Report Exporter Downloads</h1>
        <p class="lead">The table below has list of deferred downlods that have been requested.
          If the download is not here, it may have expired.</p>
      </div>

      <table class="table table-hover">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Format</th>
            <th scope="col">Submitted Timestamp</th>
            <th scope="col">Status</th>
            <th scope="col">Download</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="data in this.deferredData" v-bind:key="data.key">
            <td>{{ data.key}} </td>
            <td>{{ data.format}}</td>
            <td>{{ data.date}}</td>
            <td>{{ data.status==true ? "Complete" : "In Process"}}</td>
            <td>
              <div v-show="data.status==true">
                <button type="button" v-on:click="downloadDeferredResult(data.key, data.format)"
                    class="btn btn-primary btn-sm">Download
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="this.deferredData.length==0" class="table-secondary">
            <td colspan="5" class="text-center">
              <H6> No Deferred Exports Available</H6>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="container" style="text-align: right;">
      <span >
        <button type="button" v-on:click="refreshDataRequestedFromUser()"
            class="btn btn-primary">Refresh
        </button>
      </span>
    </div>

  </div>
</template>

<script>
  import api from '../api.js'
  import axios from 'axios'

  export default {
    name: 'downloads',

    data(){
      return {
        deferredKeys: [],
        deferredData: [],
        keyPrefix: "app_",
        filename: "branch"
      }
    },

    methods: {

      setLocalValues() {
        var keys = this.$storage.keys()
        // clear out the clearDeferredData
        this.deferredData = []

        // make local copy of deferred status Information
        // it is originally stored in localStorage, but that
        // is not reactive in vue, so need a local object
        for (var i=0; i< keys.length; i++) {
          if (keys[i].startsWith(this.keyPrefix)) {
              var row = {
                "key":keys[i].substring(this.keyPrefix.length),
                "format": this.getFormat(keys[i]),
                "date": this.getTimestamp(keys[i]),
                "status": this.getLocalStatus(keys[i])
              }
              this.deferredData.push(row)
          }
        }
      },

      getKeys() {
        var keys = this.$storage.keys()

        for (var i=0; i< keys.length; i++) {
          if (keys[i].startsWith("app_")) {
            this.deferredKeys.push(keys[i])
          }
        }
      },

      getData(key) {
        if(key.startsWith(this.keyPrefix)){
          var data = localStorage.getItem(key)
          var dataParsed = JSON.parse(data)
          return dataParsed
        }
        return null
      },

      getID(key) {
        var data = this.getData(key)
        if (data) {
          return data.value.key
        }
        else {
           return "Unknown"
        }
      },
      getFormat(key) {
        var data = this.getData(key)
        if (data) {
          return data.value.format
        }
        else {
           return "Unknown"
        }
      },
      getTimestamp(key) {
        var data = this.getData(key)
        if (data) {
          return data.value.date
        }
        else {
           return "Unknown"
        }
      },
      getLocalStatus(key) {
        var data = this.getData(key)
        if (data) {
          return data.value.status
        }
        else {
           return "Unknown"
        }
      },

      removeExpiredDownloads() {
        var keys = this.$storage.keys()

        for (var i=0; i< keys.length; i++) {
          if (keys[i].startsWith("app_")) {
            var status = this.getLocalStatus(keys[i])
            if (status=='EXPIRED') {
              localStorage.removeItem(keys[i])
            }
          }
        }

      },
      updateLocalStorageStatus(key, status) {
        // update the localStorage value for status based on the key.

        var data = this.getData(key)
        if (data) {
          //console.log("Updating status for " + key + " to " + status)
          data.value.status = status
          var updatedStatus = JSON.stringify(data)
          localStorage.setItem(key, updatedStatus)
        }
        else {
           console.log("Unable to update status for key: " + key)
        }
      },

      async getAllStatusUpdates(){
        for (var i=0; i< this.deferredKeys.length; i++) {
          //console.log("Updating key: " + this.deferredKeys[i])
          await this.getStatusFromServer(this.deferredKeys[i])
        }
      },

      async getStatusFromServer(id) {
        let promise1 = new Promise((resolve) => {
            api.pollDeferredDownloadStatus1(this.$baseURL, id.substring(this.keyPrefix.length))
            .then((data)=>{
              resolve(data);
            });
        });

        let result = await promise1; // wait until the promise resolves (*)
        this.updateLocalStorageStatus(id, result)

        //console.log("status returned (promise) = " + result)
        return result
      },

      refreshDataRequestedFromUser(){
        this.$notify({
          group: 'download',
          title: 'Export Status',
          text: 'Retrieving export status from the server',
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

        this.refreshData()

        // hide the busy indicator
        loader.hide()
      },

      async refreshData() {
        this.removeExpiredDownloads()
        this.getKeys()

        let promise1 = new Promise((resolve) => {
            this.getAllStatusUpdates()
            .then(()=>{
              resolve();
            });
        });

        // need to wait to get status from server, before we set the local
        // values
        await promise1;
        this.setLocalValues()
      },

      downloadDeferredResult(id, format) {
        let here = this
        var extension = this.getFileExtension(format)
        axios({
          url:this.$baseURL +
              'download/deferred/checkFileForHashFormatResponseEntity/'  +
              id + '/' +
              format + '/' +
              this.filename,
          method: 'GET',
          responseType: 'blob',
        }).then((response) => {
              var fileURL = window.URL.createObjectURL(new Blob([response.data]));
              var fileLink = document.createElement('a');

              fileLink.href = fileURL;
              fileLink.setAttribute('download', this.filename + '.' + extension);
              document.body.appendChild(fileLink);
              fileLink.click();
          }).catch(function(error) {
              console.error("Deferred Download Error: " + error);
              alert("Error Downloading file");
          }).finally(function() {
            here.removeDeferredDownload(id)
          });
      },

      getFileExtension(format) {
        var extension = ''
        switch (format) {
          case 'CSV':
            extension = 'csv'
            break;
          case 'JSON':
            extension = 'json'
            break;
          case 'TABD':
            extension = 'txt'
            break;
          case 'EXCEL':
            extension = 'xlsx'
            break;
          default:
            extension = 'json'
        }
        return extension
      },

      removeDeferredDownload(id) {
         localStorage.removeItem(this.keyPrefix + id)
         this.refreshData()
      }

    },

    created () {
      this.refreshData()
    }
}

</script>
<!-- styling for the component -->
<style>

  html {
    font-size: 14px;
  }
  @media (min-width: 768px) {
    html {
      font-size: 16px;
    }
  }
  .container {
    max-width: 960px;
    padding-bottom: 40px;
  }

</style>
