<template>
  <div class="subheader">
    <nav class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-subheader">
      <router-link v-bind:to="'/'" >Home</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarSubHeader" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
      </button>
      <div class="navbar-collapse collapse" id="collapsingNavbarSubHeader">

          <ul class="nav navbar-nav ml-auto">
            <li class="nav-item">
              <router-link class="nav-link" v-bind:to="'/documentation'" title="Link to Documentation">Documentation</router-link>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="https://github.com/NCIEVS/report-exporter/issues" target="_blank" title="Link to Help">Help</a>
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
    color: rgba(255,255,255, 1);
    background-color: #007dbd;
    padding: 0 30px 2px;
}
.navbar-dark .navbar-nav .nav-link {
   font-size: 16px;
   color: rgba(255,255,255, 1);
}
.navbar a  {
  font-size: 16px;
  color: rgba(255,255,255, 1);
  text-decoration: none;
}

</style>
