<template>

  <div class="subheader">

    <!-- use the componentKey to reset the wizard in the dialog  -->
    <surveyModal :key="componentKey" v-show="isSurveyModalVisible" @close="closeModal">
      <!--div slot="header">
        <h4 class="modal-title">Survey</h4>
      </div-->

      <!--
        <template v-slot:body v-if="getSurveyCookie()">
          You recently took the survey.  Thank you for your feedback.

-->
    </surveyModal>

    <nav class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-subheader">
      <a  href="/report-exporter/" aria-current="page" class="router-link-exact-active router-link-active">Home</a>
      <button  type="button" data-toggle="collapse" data-target="#collapsingNavbarSubHeader" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation" class="navbar-toggler"><span data-v-f31782d2="" class="navbar-toggler-icon"></span></button>

      <!--Vue 3 update Start-->
      <!--   <router-link type="button" v-bind:to="'/'" >Home</router-link>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#collapsingNavbarSubHeader" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>-->
      <div class="navbar-collapse collapse" id="collapsingNavbarSubHeader">

        <ul class="nav navbar-nav ml-auto">
          <li>
            <div class="dropdown">
              <!--<a class="btn dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                EVS
              </a>-->

              <a class="btn dropdown-toggle" href="#"
                 role="button" id="dropdownMenuLink" data-toggle="dropdown"
                 aria-haspopup="true" aria-expanded="false">
                EVS
              </a>
              <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">

                <a class="dropdown-item" alt="EVS" href="http://evs.nci.nih.gov/" target="_blank">
                  <img  src="../assets/EVSTile.png" alt="EVS">
                  NCI Enterprise Vocabulary Services
                </a>
                <a class="dropdown-item" alt="NCI Terminology Resources" href="http://www.cancer.gov/cancertopics/terminologyresources/" target="_blank">
                  <img alt="NCI Terminology Resources" src="../assets/Cancer_govTile.png">
                  NCI Terminology Resources
                </a>
                <a class="dropdown-item" alt="EVS Explore" href="https://evsexplore.nci.nih.gov/evsexplore/welcome" target="_blank">
                  <img alt="NCI Terminology Explorer" src="../assets/NCItTile.png">
                  EVS Explore
                </a>
              </div>

            </div>
          </li>
          <li class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-subheader">
            <router-link class="nav-link" v-bind:to="'/report-exporter/exports'" title="Link to Downloads">Downloads</router-link>
          </li>
          <li class="navbar navbar-light navbar-expand-md navbar-dark justify-content-left evs-subheader">
            <router-link class="nav-link" to="/report-exporter/documentation">Documentation</router-link>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="https://github.com/NCIEVS/report-exporter/issues" target="_blank" title="Link to Help">Help Forum</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#" v-on:click="showModal">Feedback</a>
          </li>

        </ul>
      </div>
    </nav>
  </div>
</template>


<script>
import surveyModal from './Survey.vue';
export default {
  name: 'SubHeaderBar',
  props: {
    msg: String
  },
  components: {
    surveyModal
  },
  data(){
    return {
      isSurveyModalVisible: false,
      componentKey: 0,
      surveyCookie: "NCIReportExporterSurvey",
    }
  },
  methods: {
    forceRerender() {
      this.componentKey += 1;
    },
    showModal() {
      this.isSurveyModalVisible = true;
    },
    closeModal() {
      // reset the survey wizard
      this.forceRerender();
      this.isSurveyModalVisible = false;
    },
    getSurveyCookie() {
      var surveyCookie = this.$cookies.get(this.surveyCookie);
      return surveyCookie;
    }
  },
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

