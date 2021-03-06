<template>
  <div class="report-selection">

    <nciWarningModal v-show="isModalVisible" @close="closeModal">
        <div slot="header">
          <h4 class="modal-title">Warning</h4>
        </div>
        <div slot="body">
          <p>You are accessing a US Government web site which may
            contain information that must be protected under the US Privacy Act
            or other sensitive information and is intended for Government
            authorized use only.</p>
          <p>Unauthorized attempts to upload information, change information,
            or use of this web site may result in disciplinary action, civil,
            and/or criminal penalties. Unauthorized users of this website
            should have no expectation of privacy regarding any communications
            or data processed by this website.</p>
          <p>Anyone accessing this website expressly consents to monitoring of
            their actions and all communications or data transiting or stored
            on related to this website and is advised that if such monitoring
            reveals possible evidence of criminal activity, NIH may provide
            that evidence to law enforcement officials.</p>
        </div>
    </nciWarningModal>

    <div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
      <h1 class="display-4">NCI Report Exporter</h1>
      <p class="lead">The NCI Thesaurus (NCIt) provides reference terminology services for the National Cancer Institute and other institutional users. The Report Exporter provides curated, filtered, and formatted output from the NCI Thesaurus in file types designed for typical research and development purposes.</p>
    </div>

    <div class="container">
      <div class="card-deck mb-3 text-center">
        <div class="card mb-4 box-shadow">
          <div class="card-header">
            <h4 class="my-0 font-weight-normal">Export Entities</h4>
          </div>
          <div class="card-body">
            <h1 class="card-title pricing-card-title">Read Concept Codes</h1>
            <ul class="list-unstyled mt-3 mb-4">
              <li>Export entities and selected properties from concept code matches</li>
            </ul>
          <router-link v-bind:to="'/readCodeEntry'">
            <button type="button" class="btn btn-lg btn-block btn-primary selectButton">Start Selecting Concepts</button>
          </router-link>
        </div>
      </div>
    <!--div class="card mb-4 box-shadow">
      <div class="card-header">
        <h4 class="my-0 font-weight-normal">Search Terms</h4>
      </div>
      <div class="card-body">
        <h1 class="card-title pricing-card-title">SEARCH</h1>
        <ul class="list-unstyled mt-3 mb-4">
          <li>Enter one or more concept terms</li>
          <li>Select the properties to output</li>
          <li>Select report format</li>
        </ul>
        <router-link v-bind:to="'/searchTermEntry'">
          <button type="button" class="btn btn-lg btn-block btn-primary">Select</button>
        </router-link>
      </div>
    </div-->
        <div class="card mb-4 box-shadow">
          <div class="card-header">
            <h4 class="my-0 font-weight-normal">Export Resolved Branch</h4>
          </div>
          <div class="card-body">
            <h1 class="card-title pricing-card-title">Branch Resolve</h1>
            <ul class="list-unstyled mt-3 mb-4">
              <li>Export resolved branch of entities from a root concept code match</li>
            </ul>
            <router-link v-bind:to="'/resolveBranchEntry'">
              <button type="button" class="btn btn-lg btn-block btn-primary selectButton">Start Selecting Root Nodes</button>
            </router-link>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
 import nciWarningModal from './NCIWarning.vue';

 export default {
    name: 'ReportSelection',
    props: {
      msg: String
    },

    components: {
      nciWarningModal
    },
    metaInfo: {
      title: 'EVS Report Exporter - Report Selection',
    },
    data(){
      return {
        isModalVisible: false,
        warningCookie: "NCIWarningAcknowledgement",
      }
    },
    methods: {
      showModal() {
        var warningCookie = this.$cookies.get(this.warningCookie);
        if (!warningCookie) {
          this.isModalVisible = true;
        }
      },
      closeModal() {
        this.isModalVisible = false;
        this.setWarningCookie()
      },
      setWarningCookie() {
        this.$cookies.set(this.warningCookie,"true");
      }
    },

    created() {
      // scroll to the top of the page
      window.scrollTo(0,0);
      this.showModal()
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

html {
  font-size: 14px;
}
@media (min-width: 768px) {
  html {
    font-size: 16px;
  }
}

a:hover {
  text-decoration: none;
}


.container {
  max-width: 960px;
}

.pricing-header {
  max-width: 700px;
}

.card-deck .card {
  min-width: 220px;
}

.border-top { border-top: 1px solid #e5e5e5; }
.border-bottom { border-bottom: 1px solid #e5e5e5; }
.box-shadow { box-shadow: 0 .25rem .75rem rgba(0, 0, 0, .05); }

.selectButton {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}

.btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open>.dropdown-toggle.btn-primary {
  background-color: rgb(0, 125, 188) !important;
  border-color: rgb(0, 125, 188) !important;
  color: white;
}
.button {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}

</style>
