<template>
  <div class="report-selection">

    <nciWarningModal v-show="isModalVisible" @close="closeModal">
      <template v-slot:header>
        <h4 class="modal-title">Warning</h4>
      </template>

      <template v-slot:body>
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
      </template>
    </nciWarningModal>

    <div class="title-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
      <h2 class="display-4">NCI Report Exporter</h2>
      <p class="lead">The NCI Thesaurus (NCIt) provides reference terminology services for the National Cancer Institute and other institutional users. The Report Exporter provides curated, filtered, and formatted output from the NCI Thesaurus in file types designed for typical research and development purposes.</p>
    </div>

    <div class="container">
      <div class="card-deck mb-3 text-center">
        <div class="card mb-4 box-shadow">
          <div class="card-header">
            <h5 class="my-0 font-weight-normal"><b>Export Entities</b></h5>
          </div>
          <div class="card-body d-flex flex-column">
            <h3 class="card-title pricing-card-title">Read Concept Codes</h3>
            <ul class="list-unstyled mt-3 mb-4">
              <li>Export entities and selected properties from concept code matches</li>
            </ul>
          <router-link v-bind:to="'/readCodeEntry'" class="mt-auto">
            <button type="button" class="align-self-end btn btn btn-block btn-primary selectButton">Start Selecting Concepts</button>
          </router-link>
        </div>
      </div>
      <div class="card mb-4 box-shadow">
        <div class="card-header">
          <h5 class="my-0 font-weight-normal"><b>Export Resolved Branch</b></h5>
        </div>
        <div class="card-body d-flex flex-column">
          <h3 class="card-title pricing-card-title">Branch Resolve</h3>
          <ul class="list-unstyled mt-3 mb-4">
            <li>Export resolved branch of entities from a root concept code match</li>
          </ul>
          <div class="mt-auto"></div>
          <div class="mt-auto"></div>
          <router-link v-bind:to="'/resolveBranchEntry'" >
            <button type="button" class="align-self-end btn btn btn-block btn-primary selectButton">Start Selecting Root Nodes</button>
          </router-link>
        </div>
      </div>

      <div class="card mb-4 box-shadow">
        <div class="card-header">
          <h5 class="my-0 font-weight-normal"><b>Export Roles</b></h5>
        </div>
        <div class="card-body d-flex flex-column">
          <h3 class="card-title pricing-card-title">Get Concept Roles</h3>
          <ul class="list-unstyled mt-3 mb-4">
            <li>Export entities with roles and targets</li>
          </ul>
          <div class="mt-auto"></div>
          <div class="mt-auto"></div>
          <div class="mt-auto"></div>
          <router-link v-bind:to="'/roles'" >
            <button type="button" class="align-self-end btn btn btn-block btn-primary selectButton">Start Selecting Concepts</button>
          </router-link>
        </div>
      </div>

      <div class="card mb-4 box-shadow">
        <div class="card-header">
          <h5 class="my-0 font-weight-normal"><b>Export Associations</b></h5>
        </div>
        <div class="card-body d-flex flex-column">
          <h3 class="card-title pricing-card-title">Get Concept Associations</h3>
          <ul class="list-unstyled mt-3 mb-4">
            <li>Export entities with assocations and targets</li>
          </ul>
          <div class="mt-auto"></div>
          <div class="mt-auto"></div>
          <router-link v-bind:to="'/associations'" >
            <button type="button" class="align-self-end btn btn btn-block btn-primary selectButton">Start Selecting Concepts</button>
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
  mounted(){

    this.$notify({
      group: 'download',
      title: 'Warning:',
      text: 'You are accessing a US Government web site which may contain information that must be protected under the US Privacy Act or other sensitive information and is intended for Government authorized use only Unauthorized attempts to upload information, change information, or use of this web site may result in disciplinary action, civil, and/or criminal penalties. Unauthorized users of this website should have no expectation of privacy regarding any communications or data processed by this website. Anyone accessing this website expressly consents to monitoring of their actions and all communications or data transiting or stored on related to this website and is advised that if such monitoring reveals possible evidence of criminal activity, NIH may provide that evidence to law enforcement officials.',
      type: 'success',
      duration: 8000,
      position: "Top Left"
    });

    /*
    alert ("       You are accessing a US Government web site which may\n" +
        "          contain information that must be protected under the US Privacy Act\n" +
        "          or other sensitive information and is intended for Government\n" +
        "          authorized use only\n" +
        "           Unauthorized attempts to upload information, change information,\n" +
        "          or use of this web site may result in disciplinary action, civil,\n" +
        "          and/or criminal penalties. Unauthorized users of this website\n" +
        "          should have no expectation of privacy regarding any communications\n" +
        "          or data processed by this website.\n" +
        "          Anyone accessing this website expressly consents to monitoring of\n" +
        "          their actions and all communications or data transiting or stored\n" +
        "          on related to this website and is advised that if such monitoring\n" +
        "          reveals possible evidence of criminal activity, NIH may provide\n" +
        "          that evidence to law enforcement officials.")

     */
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
     // alert(warningCookie);
      if (!warningCookie) {
        alert ("       You are accessing a US Government web site which may\n" +
            "          contain information that must be protected under the US Privacy Act\n" +
            "          or other sensitive information and is intended for Government\n" +
            "          authorized use only\n" +
            "           Unauthorized attempts to upload information, change information,\n" +
            "          or use of this web site may result in disciplinary action, civil,\n" +
            "          and/or criminal penalties. Unauthorized users of this website\n" +
            "          should have no expectation of privacy regarding any communications\n" +
            "          or data processed by this website.\n" +
            "          Anyone accessing this website expressly consents to monitoring of\n" +
            "          their actions and all communications or data transiting or stored\n" +
            "          on related to this website and is advised that if such monitoring\n" +
            "          reveals possible evidence of criminal activity, NIH may provide\n" +
            "          that evidence to law enforcement officials.")
      }
    },



    closeModal() {
      this.isModalVisible = false;
      this.setWarningCookie()
    },






  created() {
    // scroll to the top of the page
    window.scrollTo(0, 0);
    this.showModal()
  }
}}
</script>




<!--
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
-->
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

.title-header {
  max-width: 700px;
}

.card-deck .card {
  min-width: 200px;
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
