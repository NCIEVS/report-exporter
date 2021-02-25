<template>
  <div class="subheader">
    <nav class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-subheader">
       <a class="navbar-brand" href="#">
      <img  alt="EVS Exporter" src="../assets/ExpTile.png"  width="70" height="31px" border="0">
      </a>
      <router-link v-bind:to="'/'" >Home</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarSubHeader" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
      </button>
      <div class="navbar-collapse collapse" id="collapsingNavbarSubHeader">

          <ul class="nav navbar-nav ml-auto">
            <li>
              <div class="dropdown">
                <a class="btn dropdown-toggle" href="#"
                  role="button" id="dropdownMenuLink" data-toggle="dropdown"
                  aria-haspopup="true" aria-expanded="false">
                  EVS
                </a>
                <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                  <a class="dropdown-item" alt="EVS" href="http://evs.nci.nih.gov/" target="_blank">
                    <img  alt="EVS" src="../assets/EVSTile.png"
                    width="77" height="38px" border="0">
                    NCI Enterprise Vocabulary Services
                  </a>
                  <a class="dropdown-item" alt="NCIm" href="https://ncim.nci.nih.gov/ncimbrowser" target="_blank">
                    <img  alt="NCIm" src="../assets/NCImTile.png"
                      width="77" height="38px" border="0">
                    NCI Metathesaurus
                  </a>
                  <a class="dropdown-item" alt="NCI Term Browser" href="https://ncithesaurus.nci.nih.gov/ncitbrowser/" target="_blank">
                    <img alt="NCI Term Browser" src="../assets/EVSTermsBrowserTile.png"
                    width="77" height="38px" border="0">
                    NCI Term Browser
                  </a>
                  <a class="dropdown-item" alt="NCI Terminology Resources" href="http://www.cancer.gov/cancertopics/terminologyresources/" target="_blank">
                    <img alt="NCI Terminology Resources" src="../assets/Cancer_govTile.png"
                      width="77" height="38px" border="0">
                    NCI Terminology Resources
                  </a>
                  <a class="dropdown-item" alt="EVS Explore" href="https://evsexplore.nci.nih.gov/evsexplore/welcome" target="_blank">
                    <img alt="NCI Terminology Explorer" src="../assets/NCItTile.png"
                      width="77" height="38px" border="0">
                    EVS Explore
                  </a>
                </div>
              </div>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" v-bind:to="'/exports'" title="Link to Downloads">Downloads</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" v-bind:to="'/documentation'" title="Link to Documentation">Documentation</router-link>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="https://github.com/NCIEVS/report-exporter/issues" target="_blank" title="Link to Help">Help Forum</a>
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
.dropdown-menu
.dropdown-item  {
  color: black;
}

</style>
