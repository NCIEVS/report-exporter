<template>


  <meta http-equiv="Content-Security-Policy" content = "script-scc 'self' 'unsafe-inline'">
  <include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html"></include-html>



  <div v-if="open">
    <slot />
  </div>




  <div>
    This input is used in the header of the opened window. <br />
    Try updating it while the window is open!
    <br />
    <input v-model="msg" style="width: 25%" />
    <br />
    <input type="checkbox" v-model="open" /> Check me to open a portal! FInal
  </div>


  <WindowPortal v-model="open">
    <HelloWorld :msg="msg" />
  </WindowPortal>















  <div className="header">
    <nav className="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-header">
      <a className="navbar-brand" href="https://www.cancer.gov/" target="_blank">NATIONAL CANCER INSTITUTE -
        CANCER.GOV </a>
      <a className="navbar-brand" href="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html" target="_blank">PLEASE CLICK HERE FOR THE LATEST UPDATES ON IMPORTANT MESSAGES</a>
      <a className="navbar-brand" href="../components/GovernmentBanner.vue" target="_blank">PLEASE CLICK HERE FOR THE LATEST UPDATES ON IMPORTANT MESSAGES</a>
      <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarHeader"
              aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="navbar-collapse collapse" id="collapsingNavbarHeader">
        <ul className="nav navbar-nav ml-auto">
          <li className="nav-item">
            <a className="nav-link" href="https://www.cancer.gov/about-cancer" target="_blank">About Cancer<span
                className="sr-only">(current)</span></a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="https://www.cancer.gov/research" target="_blank">Research</a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="https://www.cancer.gov/grants-training" target="_blank">Grants & Training</a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="https://www.cancer.gov/news-events" target="_blank">News & Events</a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="https://www.cancer.gov/about-nci" target="_blank">About NCI</a>
          </li>
        </ul>
      </div>
    </nav>
  </div>

  <!--div class="header">
    <h3>Header message: {{ msg }}</h3>
    <p class="intro">Test Text</p>
  </div-->
</template>

<script>
import { useRouter } from 'vue-router'
import HelloWorld from "./GovernmentBanner";
import WindowPortal from "./WindowPortal";

export default {
  name: 'HeaderBar',
  components:{HelloWorld,
    WindowPortal},
  props: {
   // open: {
     // msg: String,
      type: Boolean,
    //  default: false,
},

  data(){
    const plugin = document.createElement("script");
    plugin.setAttribute(
        "src",
        "https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"
    );
    plugin.async = true;
    document.head.appendChild(plugin);
    this.windowRef = window.open("/report-exporter/GovernmentBanner", "https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html", "width=600,height=400,left=200,top=200");
    return {
      msg: 'Hello from another window!',
      open: false,


    }
  },




  watch: {
    open(newOpen) {
      if(newOpen) {
        this.openPortal();
      } else {
        this.closePortal();
      }
    }
  },

  methods: {

    data2() {

    const plugin = document.createElement("script");
    plugin.setAttribute(
        "src",
        "https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"
    );
  },
    openPortal() {
      this.data2()
      this.windowRef = window.open("/report-exporter/GovernmentBanner", "./components/GovernmentBanner.vue", "width=600,height=400,left=200,top=200");
      this.windowRef.addEventListener('beforeunload', this.closePortal);
      // magic!
      this.windowRef.document.body.appendChild(this.$el);
    },
    closePortal() {
      if(this.windowRef) {
        this.windowRef.close();
        this.windowRef = null;
        this.$emit('close');
      }
    }
  },




  //Government Shutdown Banner. https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js checks the
  //enabled/disabled flag in file https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html.  If enabled
  //the banner would show on the report exporter main screen, if disabled the banner wouldn't show on the report exporter main screen.
  mounted() {

   // if(this.open) {
      this.openPortal();
   // }

    const plugin = document.createElement("script");
    plugin.setAttribute(
        "src",
        "https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"
        //      "../components/GovernmentBanner.vue"
    );
    plugin.async = true;
    document.head.appendChild(plugin);

    const router = useRouter();
    const routeData = router.resolve({name: 'routeName', query: {data: "someData"}});
    //window.open(routeData.href, 'https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html');
    window.open(routeData.href, 'https://google.com');
  },

/*
  beforeDestroy() {
    if (this.windowRef) {
      this.closePortal();
    }
  },
*/
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.evs-header {
  color: #fff;
  background-color: #1c5e86;
  padding: 0 30px 2px;
}

.navbar-brand {
  font-size: 14px;
}

/*.navbar */
a {
  font-size: 10px;
}

.navbar-dark .navbar-nav .nav-link {
  color: rgba(255, 255, 255, 1);
}

.button {
  padding: 0;
}

/*  To control link and mouse over links
.navbar a  {
  color: yellow;
}

.navbar a:hover {

  color: pink;
}

.navbar-text .navbar-nav .nav-link {
  color: greenyellow;
}
*/

</style>
