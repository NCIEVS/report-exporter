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
            <button class="btn btn-primary" v-on:click="alertMessage">Search</button>
        </div>
      </div>
    </div>
  
  </div>
</template>

<script>

// Custom input tags
import VoerroTagsInput from '@voerro/vue-tagsinput'

export default {
  name: 'read-code-entry',
  props: {
    msg: String,
    selectedTags: []
  },
  components: {
    'tags-input': VoerroTagsInput
  },
  data(){
    return {
      message: 'Hello World!'
    }
  },
  methods: {
      setSelectedTags() {
          this.selectedTags = [{ key: 'php', value: 'PHP' }];
      },
      alertMessage(){
        alert("Entity Codes" + JSON.stringify(this.selectedTags))
    }
  }
}
</script>

<!-- styling for the component -->
<style>

/* .col {
  background-color: lightblue;
} */

</style>