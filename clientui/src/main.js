import Vue from 'vue'
import App from './App'
import jQuery from 'jquery'
import 'popper.js'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueRouter from 'vue-router'
//import 'axios'

import './assets/app.scss'
import About from './components/About'
import Exports from './components/Exports'
import Documentation from './components/Documentation'
// import ErrorPage from './components/ErrorPage.vue'
import ReportSelection from './components/ReportSelection.vue'
import ReadCodeEntry from './components/ReadCodeEntry.vue'
import SearchTermEntry from './components/SearchTermEntry.vue'
import ResolveBranchEntry from './components/ResolveBranchEntry.vue'
import Roles from './components/Roles.vue'
import Associations from './components/Associations.vue'

import VoerroTagsInput from '@voerro/vue-tagsinput'
import vMultiselectListbox from 'vue-multiselect-listbox'

import VueFormWizard from 'vue-form-wizard'
import 'vue-form-wizard/dist/vue-form-wizard.min.css'
Vue.use(VueFormWizard)

import VueFormGenerator from 'vue-form-generator'
//import 'vue-form-generator/dist/vfg.css'
Vue.use(VueFormGenerator)

import Notifications from 'vue-notification'
import ThemifyIcon from "vue-themify-icons";

// busy/loading indicator
import Loading from 'vue-loading-overlay'
Vue.use(Loading)

import {Vue2Storage} from 'vue2-storage'
Vue.use(Vue2Storage, {
  prefix: 'app_',
  driver: 'local',  // localStorage
  ttl: 60 * 60 * 24 * 1000 // 24 hours
})

import VueCookies from 'vue-cookies';
Vue.use(VueCookies);

import VueMeta from 'vue-meta';
Vue.use(VueMeta);

// google analytics tracking
import VueGtag from "vue-gtag";

// setup jquery
window.$ = window.jQuery = jQuery

Vue.config.productionTip = false

// tell vue to use the router
Vue.use(VueRouter)
Vue.use(Notifications)

// define your routes
const routes = [
  { path: '/', component: ReportSelection },
  { path: '/readCodeEntry', component: ReadCodeEntry, props: { msg: "this is it", selectedTags:[] }},
  { path: '/searchTermEntry', component: SearchTermEntry },
  { path: '/resolveBranchEntry', component: ResolveBranchEntry },
  { path: '/roles', component: Roles },
  { path: '/associations', component: Associations },
  { path: '/about', component: About },
  { path: '/documentation', component: Documentation },
  { path: '/exports', component: Exports },
  // if page is unknown, show main selection page.
  { path: '*', component: ReportSelection }
]

/* eslint-disable no-new */
// Create the router instance and pass the `routes` option
const router = new VueRouter({
  routes: routes,
  mode: 'history',
  base: process.env.VUE_APP_ROOT_CONTEXT
  //base: '/reportexporter/'
})

Vue.use(VueGtag, {
  config: { id: process.env.VUE_APP_GA_CODE },
  appName: 'Report Exporter',
}, router);

// global variable visible to all Vue instances
// values come from the env.development or env.production files.
Vue.prototype.$baseURL = process.env.VUE_APP_BASE_URL + process.env.VUE_APP_ROOT_CONTEXT

//console.log('App Base URL: ' + process.env.VUE_APP_BASE_URL)
//console.log('Root Context: ' + process.env.VUE_APP_ROOT_CONTEXT)
console.log('Base URL:     ' + Vue.prototype.$baseURL)
//console.log('VUE_APP_GA_CODE:     ' + process.env.VUE_APP_GA_CODE)

// instatiate the vue instance
new Vue({
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
     Vue2Storage
  },

  // pass in the router to the Vue instance
  router

}).$mount('#app')
