<head>
...
<!-- Import web component -->
<script src="https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"></script>
</head>

<template>
  <include-html
      src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html"></include-html>
  Real test
  <GovernmentBanner msg="Top"/>


  second test
  <img src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html" width="304"
       height="228" alt="The Pulpit Rock">


  Test Banner Vue.js
  <a className="dropdown-item" alt="EVS" href="http://evs.nci.nih.gov/" target="_blank">
    <img src="https://vuejs.org/images/logo.png" alt="EVS">
  </a>

  NCI
  <a className="dropdown-item" alt="EVS" href="http://evs.nci.nih.gov/" target="_blank">
    <img src="https://ncim.nci.nih.gov/ncimbrowser/images/banner-red.png" alt="EVS">
  </a>

  Href
  <!--<a class="dropdown-item" alt="EVS" href="http://evs.nci.nih.gov/" target="_blank">-->
  <a href="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html">
  </a>

  <a href="https://en.wikipedia.org/wiki/Boracay" target="_blank"><img
      src="//www.html.am/images/html-codes/links/boracay-white-beach-sunset-300x225.jpg" width="300" height="225"
      alt="Photo of White Beach in Boracay, Philippines"/></a>
  <a href="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html" target="_blank"><img
      src="//https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html" width="300"
      height="225" alt="Banner"/></a>

  Test Tutorialspoint Image
  <img
      style="display: block;-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;"
      src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html">slot

  Test banner prod
  <img
      style="display: block;-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;"
      src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown.html">slot

  Test banner test
  <img
      style="display: block;-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;"
      src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html">slot

  <img alt="A Lesson on:"
       href="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html" width=300
       height=259>
  Test banner test
  <!--<img style="display: block;-webkit-user-select: none;margin: auto;background-color: hsl(0, 0%, 90%);transition: background-color 300ms;" src="../assets/index.html">-->


  <include-html id="governmentShutdownAlert"
                src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html"></include-html>


  <include-html src="../assets/index.html" alt="EVS"></include-html>


  <div className="header">
    <nav className="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-header">
      <a className="navbar-brand" href="https://www.cancer.gov/" target="_blank">NATIONAL CANCER INSTITUTE -
        CANCER.GOV </a>
      <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarHeader"
              aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
      </button>
      <br>
      <a className="navbar-brand" href="../assets/index.html" onClick=processBanner() target="_blank">MESSAGE CENTER
        2 </a>
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

<!--<script src="../assets/include-html.js">-->
<script>
//setInterval(() => window.governmentShutdownAlert.refresh(), 30000)
import GovernmentBanner from '../../../../../../report-exporter/clientui/src/assets/Banner.vue';
//import GovernmentBanner from './GovernmentBanner.vue';

export default {
  name: 'HeaderBar',
  props: {
    msg: String
  },


  components: {
    GovernmentBanner
  },


  /*
      //Government Shutdown Banner. https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js checks the
      //enabled/disabled flag in file https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html.  If enabled
      //the banner would show on the report exporter main screen, if disabled the banner wouldn't show on the report exporter main screen.
      mounted() {
        const plugin = document.createElement("script");
        plugin.setAttribute(
            "src",
            "https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"
        );
        plugin.async = true;
        document.head.appendChild(plugin);
      },
  */


  /*
  mounted() {
    const plugin = document.createElement("script");
    plugin.src = 'https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js';
    document.getElementsByTagName('head')[0].appendChild(plugin);
  }
}
*/
  /*
  mounted() {
    const ga = document.createElement('script');
    ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ?
            'https://ssl' : 'https://cbiit') +
        '.github.io/nci-softwaresolutions-elements/components/include-html.js';
    const s = document.getElementsByTagName('script')[0];
    s.parentNode.insertBefore(ga, s);
  }
}
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
