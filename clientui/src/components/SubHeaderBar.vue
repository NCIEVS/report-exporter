<template>
  <div class="subheader">
    <nav class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-subheader">
      <router-link v-bind:to="'/'">
        <a class="navbar-brand active" href="#">Home</a>
      </router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarSubHeader" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
      </button>
      <div class="navbar-collapse collapse" id="collapsingNavbarSubHeader">

          <ul class="nav navbar-nav ml-auto">
            <li class="nav-item">
              <a class="nav-link" href="#">Documentation</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#">Help</a>
            </li>
          </ul>
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
    background-color: #017ebe;
    padding: 0 30px 2px;
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
