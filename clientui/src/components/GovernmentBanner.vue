test start



<template>


  <include-html src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html"></include-html>


  try1 end

  <WindowPortal v-model="open">
    <HelloWorld :msg="msg" />
  </WindowPortal>



  <div v-html="Desc"> </div>
  Desc:
  <header>
    <!-- Set component's src attribute to url of html snippet -->
    <!--<include-html src="https://vuejs.org/images/logo.png"></include-html>-->
    <img src="https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.html" >
    <!-- <img v-html="Desc2" v-bind:src="'https://cbiit.github.io/nci-softwaresolutions-elements/banners/government-shutdown-test.htm'" >-->
    <img v-html="Desc3" v-bind:src='profilePic' >
    ...
  </header>
  test 4
  test5
  <a class="dropdown-item" alt="EVS" href="http://evs.nci.nih.gov/" target="_blank">
    <img  src="../assets/EVSTile.png" alt="EVS">
    NCI Enterprise Vocabulary Services
  </a>
  test6



  <div class="header">
    <nav class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-header">
      <a class="navbar-brand" href="https://www.cancer.gov/" target="_blank">NATIONAL CANCER INSTITUTE - CANCER.GOV </a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarHeader" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="navbar-collapse collapse" id="collapsingNavbarHeader">
        <ul class="nav navbar-nav ml-auto">
          <li class="nav-item">
            <a class="nav-link" href="https://www.cancer.gov/about-cancer" target="_blank">About Cancer<span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="https://www.cancer.gov/research" target="_blank">Research</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="https://www.cancer.gov/grants-training" target="_blank">Grants & Training</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="https://www.cancer.gov/news-events" target="_blank">News & Events</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="https://www.cancer.gov/about-nci" target="_blank">About NCI</a>
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

export default {
  name: 'HeaderBar',
  props: {
    msg: String,


    data: {
      image: 'https://vuejs.org/images/logo.png'

    }
  },

  mounted() {
    alert("test Govt Load")
    const plugin = document.createElement("script");
    plugin.setAttribute(
        "src",
        "https://cbiit.github.io/nci-softwaresolutions-elements/components/include-html.js"
    );
    plugin.async = true;
    document.head.appendChild(plugin);
  }







}


</script>



<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

.evs-header {
  color:#fff;
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
  color: rgba(255,255,255, 1);
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

.nci-shutdown-banner {
  font-family: sans-serif;
  background: #bb0e3d;
  padding: 15px;
}

.nci-shutdown-banner__body {
  max-width: 1024px;
  margin: 0 auto;
  color: white;
  font-size: 17px;
  line-height: 1.6;
  position: relative;
  padding: 0 15px 0 40px;
}

.nci-shutdown-banner__body:before {
  content: "";
  display: block;
  position: absolute;
  height: 26px;
  width: 26px;
  top: 0;
  left: 0;
  background: none;
  background-color: #fff;
  -webkit-mask: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTIgMkM2LjQ4IDIgMiA2LjQ4IDIgMTJzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwUzE3LjUyIDIgMTIgMnptMSAxNWgtMnYtMmgydjJ6bTAtNGgtMlY3aDJ2NnoiLz48L3N2Zz4=) no-repeat center/contain;
  mask: url(data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0Ij48cGF0aCBkPSJNMTIgMkM2LjQ4IDIgMiA2LjQ4IDIgMTJzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwUzE3LjUyIDIgMTIgMnptMSAxNWgtMnYtMmgydjJ6bTAtNGgtMlY3aDJ2NnoiLz48L3N2Zz4=) no-repeat center/contain;
}

.nci-shutdown-banner__body h2 {
  font-size: 18px;
  margin: 0;
}
.nci-shutdown-banner__body a,
.nci-shutdown-banner__body a:visited {
  color: white;
}
</style>
