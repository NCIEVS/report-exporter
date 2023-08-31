<template>

  <div class="header">

    <nav class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-header">

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
      msg: String
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

</style>
