<template>

  <div class="header">
    <nav class="navbar navbar-expand-md navbar-dark fixed-top evs-header">
      <a class="navbar-brand active" href="https://www.cancer.gov/" target="_blank">NATIONAL CANCER INSTITUTE - CANCER.GOV </a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <!-- Leave empty for correct alignment -->
          </li>
        </ul>
        <span class="navbar-text">
          <ul class="navbar-nav mr-auto">
              <li class="nav-item">
                <a class="nav-link" href="#">About Cancer<span class="sr-only">(current)</span></a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#">Research</a>
              </li>
               <li class="nav-item">
                <a class="nav-link" href="#">Grants & Training</a>
              </li>
               <li class="nav-item">
                <a class="nav-link" href="#">News & Events</a>
              </li>
               <li class="nav-item">
                <a class="nav-link" href="#">About NCI</a>
              </li>
            </ul>
        </span>
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
  height: 30px; 
}

.navbar-brand {
   font-size: 14px;
}
a {
  font-size: 10px;
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


