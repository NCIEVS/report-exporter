<template>

  <div class="subheader">
    <nav class="navbar navbar-expand-md navbar-dark fixed evs-subheader">
      <router-link v-bind:to="'/'">
      <a class="navbar-brand active" href="#" target="_blank">EVS Report Exporter</a>
      </router-link>
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
                <a class="nav-link" href="#">Documentation</a>
              </li>
               <li class="nav-item">
                <a class="nav-link" href="#">Help</a>
              </li>

            </ul>
        </span>
      </div>
    </nav>
  </div>

</template>

<script>
  export default {
    name: 'SubHeaderBar',
    props: {
      msg: String
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>


.evs-subheader {
  position: fixed;
  width: 100%;
  color:#fff;
  background-color: #017ebe;
  height: 40px; 
}

.navbar-brand {
   font-size: 16px;
}
a {
  font-size: 12px;
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


