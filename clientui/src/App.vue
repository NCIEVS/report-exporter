<template>
  <div id="app">
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
  // props: {
  //   selectedTags:[]
  // },
  components: {
    HeaderBar,
    SubHeaderBar,
    FooterBar,
    // 'tags-input': VoerroTagsInput
  },
  data(){
    return {
      message: 'Hello',
      selectedTags:[]
    }
  }
}
</script>

<style>
#app {
  font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";
  margin-top: 30px;
}
</style>
