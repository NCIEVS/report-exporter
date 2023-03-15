//import Vue from 'vue'
import { createApp } from 'vue'
import App from './App.vue'
import router2 from './router'
import jQuery from 'jquery'
import 'popper.js'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'


//import  VueRouter  from "vue-router";
//import 'axios'


import './assets/app.scss'
/*
import About from './components/About'
import Exports from './components/Exports'
import Documentation from './components/Documentation'
//import ErrorPage from './components/ErrorPage.vue'
import ReportSelection from './components/ReportSelection.vue'
import ReadCodeEntry from './components/ReadCodeEntry.vue'
import SearchTermEntry from './components/SearchTermEntry.vue'
import ResolveBranchEntry from './components/ResolveBranchEntry.vue'
import Roles from './components/Roles.vue'
import Associations from './components/Associations.vue'
*/
import VoerroTagsInput from '@voerro/vue-tagsinput'
import vMultiselectListbox from 'vue-multiselect-listbox'
import VueFormWizard from 'form-wizard-vue3'  //Vue 3
import 'vue-form-wizard/dist/vue-form-wizard.min.css' // Vue 3


const app = createApp(App)

app.use(VueFormWizard)
app.use(router2)


import VueFormGenerator from 'vue-form-generator'
//import 'vue-form-generator/dist/vfg.css'
app.use(VueFormGenerator)

import Notifications from '@kyvg/vue3-notification'
import ThemifyIcon from "vue-themify-icons";

// busy/loading indicator
//import Loading from 'vue-loading-overlay'
import Loading from 'vue3-loading-overlay'
app.use(Loading)

import {Vue3Storage} from 'vue3-storage'
app.use(Vue3Storage, {
  prefix: 'app_',
  driver: 'local',  // localStorage
  ttl: 60 * 60 * 24 * 1000 // 24 hours
})

import VueCookies from 'vue3-cookies';
app.use(VueCookies);

//import { VueMeta } from 'vue-meta';
//import { createMetaManager } from 'vue-meta'

//createApp(App)
    //app.use(store)
   // app.use(VueRouter)
    //app.use(createMetaManager())


//app.use(VueMeta);

// google analytics tracking
import VueGtag from "vue-gtag-next";

// setup jquery
window.$ = window.jQuery = jQuery

app.config.productionTip = false

// tell vue to use the router
//app.use(VueRouter)
app.use(Notifications)


/* Vue 3 Change moved code to router/index.js file - Start
// define your routes


const routes = [

{ path: "/",
  name: "Report Selection",
  component: ReportSelection,
},

{ path: "/ReportSelection",
  name: "Report Selection",
  component: ReportSelection,
},

  { path: '/readCodeEntry', component: ReadCodeEntry, props: { msg: "this is it", selectedTags:[] }},
  { path: '/searchTermEntry', component: SearchTermEntry },



  { path: '/resolveBranchEntry', component: ResolveBranchEntry },
  { path: '/roles', component: Roles },
  { path: '/associations', component: Associations },
  { path: '/about', component: About },
  { path: '/documentation', component: Documentation },
  { path: "/exports", component: Exports },
  // if page is unknown, show main selection page.

  {
    path: '/:pathMatch(.*)*',
    redirect: "/ReportSelection",
  },
  //{ path: '*', component: ReportSelection }
]

 Vue 3 Change Code moved to router/index.js - End */

/* eslint-disable no-new */
// Create the router instance and pass the `routes` option
//const app = createApp(App)
//const router = VueRouter.createApp({
//const router = new VueRouter({


//Vue 3 way to create a router instance
const router = createApp({
 // routes: routes,
  mode: 'history',
  base: process.env.VUE_APP_ROOT_CONTEXT
  //base: '/reportexporter/'
})
//router.mount('#router')

app.use(VueGtag, {
  config: { id: process.env.VUE_APP_GA_CODE },
  appName: 'Report Exporter',
}, router);

// global variable visible to all Vue instances
// values come from the env.development or env.production files.
//app.prototype.$baseURL = process.env.VUE_APP_BASE_URL + process.env.VUE_APP_ROOT_CONTEXT
// Vue 3 way to write out the baseURL
app.config.globalProperties.$baseURL = process.env.VUE_APP_BASE_URL + process.env.VUE_APP_ROOT_CONTEXT


// instatiate the vue instance
//createApp(app);{
//createApp(App).mount('#app')({
//new app({
   createApp({

  // define the selector for the root component
  //el: '#app',
  render: h => h(App),
  // pass the template to the root component
  template: '<App/>',
  // declare components that the root component can access
  components: {
    App,
    'tags-input': VoerroTagsInput,
    vMultiselectListbox,
    ThemifyIcon,
    Vue3Storage
  },

  // pass in the router to the Vue instance
  router

})

export default

router;
    //.$mount('#app')

//install(app) {
  app.component('Wizard', VueFormWizard)
//}
app.mount('#app')
