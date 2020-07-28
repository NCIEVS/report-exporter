<template>
  <div id="app">
    <notifications group="app"/>
    <notifications group="download"/>

     <div class="header-div">
        <HeaderBar msg="Top"/>
        <SubHeaderBar msg="SubTop"/>
      </div>
       <div class="main-content">
          <!-- the router outlet, where all matched components would be viewed -->
          <router-view></router-view>
      </div>
    <FooterBar msg="Bottom"/>
  </div>
</template>

<script>
import HeaderBar from './components/HeaderBar.vue'
import SubHeaderBar from './components/SubHeaderBar.vue'
import FooterBar from './components/FooterBar.vue'
// Custom input tags
// import VoerroTagsInput from '@voerro/vue-tagsinput'

export default {
  name: 'app',
  props: {
  //   selectedTags:[]
  },
  components: {
    HeaderBar,
    SubHeaderBar,
    FooterBar,
  },
  data(){
    return {
      message: 'Hello',
      selectedTags:[]
    }
  },
  created () {
    // Set the title in browser tab
    document.title = "EVS Report Exporter";
  }
}
</script>

<style>
#app {
  }
</style>
