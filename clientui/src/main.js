

import { createApp } from 'vue'
import App from './App.vue'
import router2 from './router'
import jQuery from 'jquery'
import 'popper.js'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import './assets/app.scss'
import VoerroTagsInput from '@voerro/vue-tagsinput'
import vMultiselectListbox from 'vue-multiselect-listbox'
import VueFormWizard from 'form-wizard-vue3'  //Vue 3
import 'vue-form-wizard/dist/vue-form-wizard.min.css' // Vue 3


const app = createApp(App)

app.use(VueFormWizard)
app.use(router2)


import VueFormGenerator from 'vue-form-generator'
app.use(VueFormGenerator)

import Notifications from '@kyvg/vue3-notification'
import ThemifyIcon from "vue-themify-icons";

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

// google analytics tracking
import VueGtag from "vue-gtag-next";

// setup jquery
window.$ = window.jQuery = jQuery

app.config.productionTip = false

// tell vue to use the router
app.use(Notifications)

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

   createApp({

  // define the selector for the root component

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

  app.component('Wizard', VueFormWizard)
  app.mount('#app')
