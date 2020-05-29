import Vue from 'vue'
import App from './App'
import jQuery from 'jquery'
import 'popper.js'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import VueRouter from 'vue-router'

import './assets/app.scss'
import About from './components/About'
import ReportSelection from './components/ReportSelection.vue'
import ReadCodeEntry from './components/ReadCodeEntry.vue'
import SearchTermEntry from './components/SearchTermEntry.vue'
import ResolveBranchEntry from './components/ResolveBranchEntry.vue'

import VoerroTagsInput from '@voerro/vue-tagsinput';

// setup jquery
window.$ = window.jQuery = jQuery

Vue.config.productionTip = false

// tell vue to use the router
Vue.use(VueRouter)

// define your routes
const routes = [
  { path: '/', component: ReportSelection },
  { path: '/readCodeEntry', component: ReadCodeEntry, props: { msg: "this is it", selectedTags:[] }},
  { path: '/searchTermEntry', component: SearchTermEntry },
  { path: '/resolveBranchEntry', component: ResolveBranchEntry },
  { path: '/about', component: About }
]

/* eslint-disable no-new */
// Create the router instance and pass the `routes` option
const router = new VueRouter({
  routes: routes,
  mode: 'history'
})

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
    'tags-input': VoerroTagsInput
  },
  // pass in the router to the Vue instance
  router
  // mount the router on the app
}).$mount('#app')
