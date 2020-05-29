<template>
  <div class="report-selection">

    <div class="pricing-header px-3 py-3 pt-md-5 pb-md-4 mx-auto text-center">
      <h1 class="display-4">NCI Thesaurus</h1>
      <p class="lead">NCI Thesaurus (NCIt) provides reference terminology for many NCI and other systems. It covers vocabulary for clinical care, translational and basic research, and public information and administrative activities.</p>
    </div>

    <div class="container">
      <div class="card-deck mb-3 text-center">
        <div class="card mb-4 box-shadow">
          <div class="card-header">
            <h4 class="my-0 font-weight-normal">Read Codes</h4>
          </div>
          <div class="card-body">
            <h1 class="card-title pricing-card-title">title</h1>
            <ul class="list-unstyled mt-3 mb-4">
              <li>Enter one or many entity codes</li>
              <li>Select the properties to output</li>
              <li>Select report format</li>
            </ul>
			<router-link v-bind:to="'/readCodeEntry'">
        <button type="button" class="btn btn-lg btn-block btn-primary">Select</button>
			</router-link>
          </div>
        </div>
        <div class="card mb-4 box-shadow">
          <div class="card-header">
            <h4 class="my-0 font-weight-normal">Search Terms</h4>
          </div>
          <div class="card-body">
            <h1 class="card-title pricing-card-title">title</h1>
            <ul class="list-unstyled mt-3 mb-4">
              <li>Enter one or more entity terms</li>
              <li>Select the properties to output</li>
              <li>Select report format</li>
            </ul>
            <router-link v-bind:to="'/searchTermEntry'">
              <button type="button" class="btn btn-lg btn-block btn-primary">Select</button>
			</router-link>
          </div>
        </div>
        <div class="card mb-4 box-shadow">
          <div class="card-header">
            <h4 class="my-0 font-weight-normal">Resolve Branch</h4>
          </div>
          <div class="card-body">
            <h1 class="card-title pricing-card-title">title</h1>
            <ul class="list-unstyled mt-3 mb-4">
              <li>Select root of branch </li>
              <li>Select the properties to output</li>
              <li>Select report format</li>
            </ul>
            <router-link v-bind:to="'/resolveBranchEntry'">
              <button type="button" class="btn btn-lg btn-block btn-primary">Select</button>
			</router-link>
          </div>
        </div>
      </div>
</div>


  </div>
</template>

<script>
export default {
  name: 'ReportSelection',
  props: {
    msg: String
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

</style>
