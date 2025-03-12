
<template>


  <teleport to="head">
    <component
        :is="'script'"
        src="https://assets.adobedtm.com/6a4249cd0a2c/785de09de161/launch-70d67a6a40a8.min.js"
        async
    />
  </teleport>
  <div id="app">

    <notifications group="app"/>
    <notifications group="download"/>

    <div className="header-div">
      <HeaderBar msg="Top"/>
      <SubHeaderBar msg="SubTop"/>
    </div>
    <div className="main-content">
      <!-- the router outlet, where all matched components would be viewed -->
      <!--<router-view></router-view>-->
      <router-view/>
    </div>
    <!--<ReportSelection  msg="Bottom"/>-->
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
  },
  components: {
    HeaderBar,
    SubHeaderBar,
    FooterBar,
  },

  metaInfo: {
    title: 'EVS Report Exporter',
    headAttrs: {
      "X-XSS-Protection": 1,
      "mode": "block"
    },
    meta: [
      {name: 'Referrer-Policy', value: 'no-referrer | same-origin'},
    ]
  },
  data() {
    return {
      selectedTags: []
    }
  }
}

</script>

