<template>
  <div id="documentation">
    <div class="container documentation-container">
      <div class="row">
          <div class="col-lg-4">
              <div class="nav nav-pills faq-nav" id="faq-tabs" role="tablist" aria-orientation="vertical">
                  <a href="#tab1" class="nav-link active" data-toggle="pill" role="tab" aria-controls="tab1" aria-selected="true">
                      <i class="mdi mdi-help-circle"></i> NCI Report Exporter
                  </a>
                  <a href="#tab2" class="nav-link" data-toggle="pill" role="tab" aria-controls="tab2" aria-selected="false">
                      <i class="mdi mdi-account"></i> Export Entities
                  </a>
                  <a href="#tab3" class="nav-link" data-toggle="pill" role="tab" aria-controls="tab3" aria-selected="false">
                      <i class="mdi mdi-account-settings"></i> Export Resolved Branch
                  </a>

              </div>
          </div>
          <div class="col-lg-8">
              <div class="tab-content" id="faq-tab-content">
                  <div class="tab-pane show active" id="tab1" role="tabpanel" aria-labelledby="tab1">
                      <div class="accordion" id="accordion-tab-1">
                          <div class="card">
                              <div class="card-header" id="accordion-tab-1-heading-1">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-1-content-1" aria-expanded="false" aria-controls="accordion-tab-1-content-1">
                                        What is the NCI Report Exporter
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse show" id="accordion-tab-1-content-1" aria-labelledby="accordion-tab-1-heading-1" data-parent="#accordion-tab-1">
                                  <div class="card-body">
                                    <p>
                                      The NCIt Report Exporter provides curated, filtered, and formatted output from the NCI Thesaurus
                                      in file types designed for typical research and development purposes.
                                    </p>
                                    <p>
                                      The NCIt Report Exporter provides two different exports.
                                      They are presented in a wizard-like format that walks the user through the different steps to take.
                                      The two different exports are:
                                      <ul>
                                        <li>
                                          <strong>Export Entities</strong>: Enter one or more concept codes, select properties to be exported and the file format.
                                        </li>
                                        <li>
                                          <strong>Export Resolved Branch</strong>: Enter or select a top node, the numbers of levels to retrieve, the properties to be exported, and the file format.
                                        </li>
                                      </ul>
                                    </p>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>
                  <div class="tab-pane" id="tab2" role="tabpanel" aria-labelledby="tab2">
                      <div class="accordion" id="accordion-tab-2">
                          <div class="card">
                              <div class="card-header" id="accordion-tab-2-heading-1">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-2-content-1" aria-expanded="false" aria-controls="accordion-tab-2-content-1">
                                        How do I enter NCI Thesaurus concept codes?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse show" id="accordion-tab-2-content-1" aria-labelledby="accordion-tab-2-heading-1" data-parent="#accordion-tab-2">
                                  <div class="card-body">
                                    <p>
                                        Concept codes can be manually entered 1 at a time.
                                        After entering a concept code, hit the space bar or enter key.
                                        This will initiate a process to validate and update the concept code with its name.
                                    </p>
                                    <p>
                                        Concept codes can also be copied from a list of codes that are comma separated.
                                        <br>For example, <strong>C1909, C12434, C61410, C3909</strong>
                                    </p>
                                  </div>
                              </div>
                          </div>
                          <div class="card">
                              <div class="card-header" id="accordion-tab-2-heading-2">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-2-content-2" aria-expanded="false" aria-controls="accordion-tab-2-content-2">
                                        How do I remove concept codes?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse" id="accordion-tab-2-content-2" aria-labelledby="accordion-tab-2-heading-2" data-parent="#accordion-tab-2">
                                  <div class="card-body">
                                    <p>
                                      You can click on the "x" to the right of the concept code to remove individual concept codes.
                                    </p>
                                    <p>
                                      Alternatively, if you need to remove all concepts codes, click on the <strong>Clear</strong> button
                                      under the concept code input.
                                    </p>
                                  </div>
                              </div>
                          </div>
                          <div class="card">
                              <div class="card-header" id="accordion-tab-2-heading-3">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-2-content-3" aria-expanded="false" aria-controls="accordion-tab-2-content-3">
                                        Why is the "Select Concept Codes for Entity Export" step/icon in the upper right corner red?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse" id="accordion-tab-2-content-3" aria-labelledby="accordion-tab-2-heading-3" data-parent="#accordion-tab-2">
                                  <div class="card-body">
                                    <p>
                                      The "Select Concept Codes for Entity Export" step/icon will turn red if
                                      you do not enter a concept code and try to go to the next step using the "Select Next Option" button.
                                    </p>
                                  </div>
                              </div>
                          </div>
                          <div class="card">
                              <div class="card-header" id="accordion-tab-2-heading-4">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-2-content-4" aria-expanded="false" aria-controls="accordion-tab-2-content-4">
                                        How do you select properties to be included in the export?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse" id="accordion-tab-2-content-4" aria-labelledby="accordion-tab-2-heading-4" data-parent="#accordion-tab-2">
                                  <div class="card-body">
                                    <p>
                                      In the "Select Properties" (step 2), you can select individual properties by clicking on them in the list on the left.
                                    </p>
                                    <p>
                                      If you want to add all properties, selct the ">>" button between the two lists.
                                    </p>
                                    <p>
                                      You can also search the list by typing search criteria in the input text above the properties list.
                                    </p>
                                  </div>
                              </div>
                          </div>
                          <div class="card">
                              <div class="card-header" id="accordion-tab-2-heading-5">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-2-content-5" aria-expanded="false" aria-controls="accordion-tab-2-content-5">
                                        How do I know the export was successful?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse" id="accordion-tab-2-content-5" aria-labelledby="accordion-tab-2-heading-5" data-parent="#accordion-tab-2">
                                  <div class="card-body">
                                    <p>
                                      The export is successfull if there are no error dialogs and the file is downloaded to your system.
                                    </p>
                                    <p>
                                      There will be a file that is downloaded named entities.json, entities.csv, entities.txt, or entities.xlsx based on the file type selected.
                                    </p>
                                  </div>
                              </div>
                          </div>

                      </div>
                  </div>
                  <div class="tab-pane" id="tab3" role="tabpanel" aria-labelledby="tab3">
                      <div class="accordion" id="accordion-tab-3">
                          <div class="card">
                              <div class="card-header" id="accordion-tab-3-heading-1">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-3-content-1" aria-expanded="false" aria-controls="accordion-tab-3-content-1">
                                        What is a top node?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse show" id="accordion-tab-3-content-1" aria-labelledby="accordion-tab-3-heading-1" data-parent="#accordion-tab-3">
                                  <div class="card-body">
                                      A top node is a node that has at least one level of child concept codes.
                                  </div>
                              </div>
                          </div>
                          <div class="card">
                              <div class="card-header" id="accordion-tab-3-heading-2">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-3-content-2" aria-expanded="false" aria-controls="accordion-tab-3-content-2">
                                        How can I select a top node?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse" id="accordion-tab-3-content-2" aria-labelledby="accordion-tab-3-heading-2" data-parent="#accordion-tab-3">
                                  <div class="card-body">
                                      <p>
                                        If you know the top node concept code, you can enter it directly in the text field and click enter
                                      </p>
                                      <p>
                                        By selecting the top node text field, a drop down will appear with common top nodes you can select.
                                      </p>
                                      <p>
                                        You can also select the "Tree View" button.  This will open a new window with the hierarchical view of the NCIt.
                                        You can select a top node by clicking on the checkbox next to a concept code and then click the "Select" button on the dialog.
                                      </p>
                                  </div>
                              </div>
                          </div>
                          <div class="card">
                              <div class="card-header" id="accordion-tab-3-heading-3">
                                  <h5>
                                      <button class="btn btn-link" type="button" data-toggle="collapse" data-target="#accordion-tab-3-content-3" aria-expanded="false" aria-controls="accordion-tab-3-content-3">
                                        What are levels?
                                      </button>
                                  </h5>
                              </div>
                              <div class="collapse" id="accordion-tab-3-content-3" aria-labelledby="accordion-tab-3-heading-3" data-parent="#accordion-tab-3">
                                  <div class="card-body">
                                      <p>
                                        The level indicates how many sub levels of concept nodes to retrieve.
                                      </p>
                                      <p>
                                        The higher the number of levels selected, the more concept codes will be resolved.
                                        The higher the number of levels selected will also increase the time it takes to export.
                                      </p>
                                  </div>
                              </div>
                          </div>
                      </div>
                  </div>

              </div>
          </div>
      </div>
      </div>
  </div>
</template>

<script>
  export default {
    name: 'documentation',
    
  created() {
    // scroll to the top of the page
    window.scrollTo(0,0);
    }
}
</script>
<!-- styling for the component -->
<style>


</style>
