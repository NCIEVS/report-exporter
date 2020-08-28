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
import Documentation from './components/Documentation'
// import ErrorPage from './components/ErrorPage.vue'
import ReportSelection from './components/ReportSelection.vue'
import ReadCodeEntry from './components/ReadCodeEntry.vue'
import SearchTermEntry from './components/SearchTermEntry.vue'
import ResolveBranchEntry from './components/ResolveBranchEntry.vue'

import VoerroTagsInput from '@voerro/vue-tagsinput'
import vMultiselectListbox from 'vue-multiselect-listbox'

import VueFormWizard from 'vue-form-wizard'
import 'vue-form-wizard/dist/vue-form-wizard.min.css'
Vue.use(VueFormWizard)

import Notifications from 'vue-notification'
import ThemifyIcon from "vue-themify-icons";

// busy/loading indicator
import Loading from 'vue-loading-overlay'
Vue.use(Loading)

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
  { path: '/about', component: About },
  { path: '/documentation', component: Documentation },
  // if page is unknown, show main selection page.
  { path: '*', component: ReportSelection }
]

/* eslint-disable no-new */
// Create the router instance and pass the `routes` option
const router = new VueRouter({
  routes: routes,
  mode: 'history',
  //base: '/reportExporter/'
})

// global variable visible to all Vue instances
// values come from the env.development or env.production files.
Vue.prototype.$baseURL = process.env.VUE_APP_BASE_URL + process.env.VUE_APP_ROOT_CONTEXT

console.log('APP BASE URL: ' + process.env.VUE_APP_BASE_URL)
console.log('ROOT CONTEXT: ' + process.env.VUE_APP_ROOT_CONTEXT)
console.log('baseURL: ' + Vue.prototype.$baseURL)

// instatinate the vue instance
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
     ThemifyIcon
  },
  // pass in the router to the Vue instance
  router
  // mount the router on the app
}).$mount('#app')
