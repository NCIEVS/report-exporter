<script>

  import axios from 'axios';
  import VueFormGenerator from 'vue-form-generator'
   //import 'vue-form-generator/dist/vfg.css'

  export default {
    name: 'modal',

    data(){
      return{
        Survey:{
          rating:undefined,
          email:'',
          recommendations:'',
          features:'',
        },
       formOptions: {
          validationErrorClass: "has-error",
          validationSuccessClass: "has-success",
          validateAfterChanged: true
       },
       firstTabSchema:{
         fields:[{
            type: "select",
            label: "How would you rate the ease of retrieving the desired data? 1 is difficult, 10 is easy",
            model: "rating",
            values: [
              1,2,3,4,5,6,7,8,9,10
            ],
            required:true,
            validator:VueFormGenerator.validators.required,
         },
         {
            type: "textArea",
            label: "What features would you like to see on this application?",
            model: "features",
            hint: "Max 500 characters",
            max: 500,
            placeholder: "comments",
            rows: 4,
            //required:true,
            validator:VueFormGenerator.validators.string,
            //styleClasses:'col-xs-9'
         }
         ]
       },
       secondTabSchema:{
         fields:[
         {
            type: "textArea",
            label: "Would you like to tell us about your project to see if we have additional recommendations? Please supply your email if you wish a personal response.",
            model: "recommendations",
            hint: "Max 500 characters",
            max: 500,
            placeholder: "comments",
            rows: 4,
            //required:true,
            validator:VueFormGenerator.validators.string,
         },
         {
           type: "input",
           inputType: "text",
           label: "Email",
           model: "email",
           //required:true,
           validator:VueFormGenerator.validators.email,
        }
         ]
       }
     }
   },
    methods: {
      close() {
        this.$emit('close');
        this.clearValues();
      },
      clearValues: function() {
        this.Survey.rating=null;
        this.Survey.features='';
        this.Survey.recommendations='';
        this.Survey.email='';

        //this.$refs.reset;
      },

      onComplete: function(){
        this.submitForm();
       },
       validateFirstTab: function(){
         return this.$refs.firstTabForm.validate();
       },
       validateSecondTab: function(){
         return this.$refs.secondTabForm.validate();
       },

       submitForm() {
         console.log("SURVEY URL: " + this.$baseURL + "survey", )
         //console.log("FORM DATA:" + this.Survey)
         console.log("FORM DATA - JSON:" + JSON.stringify(this.Survey))
         // const headers = {
         //   "content-type": "application/json",
         // };
         axios.post(
            this.$baseURL + "survey",
            //this.Survey,
            JSON.stringify(this.Survey),
            // {headers}
          )
          .then(() => {
              alert('Thanks for taking the survey!');
              this.close();
          })
          .catch(error => {
              console.log("error submitting survey..." + error)
          });
          //res.data.headers['Content-Type']; // text/json



         // axios({
         //      method: 'post',
         //      url: this.$baseURL + "survey",
         //      data: JSON.stringify(this.Survey),
         //  })
         //  .then(() => {
         //      alert('Thanks for taking the survey!');
         //      this.close();
         //  })
         //  .catch(error => {
         //      console.log("error submitting survey..." + error)
         //  });
      },

       // prettyJSON: function(json) {
       //      if (json) {
       //          json = JSON.stringify(json, undefined, 4);
       //          json = json.replace(/&/g, '&').replace(/</g, '<').replace(/>/g, '>');
       //          return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+-]?\d+)?)/g, function(match) {
       //              var cls = 'number';
       //              if (/^"/.test(match)) {
       //                  if (/:$/.test(match)) {
       //                      cls = 'key';
       //                  } else {
       //                      cls = 'string';
       //                  }
       //              } else if (/true|false/.test(match)) {
       //                  cls = 'boolean';
       //              } else if (/null/.test(match)) {
       //                  cls = 'null';
       //              }
       //              return '<span class="' + cls + '">' + match + '</span>';
       //          });
       //      }
       //  }
    },
    mounted() {
        console.log("mounted")
    },
  }
</script>

<template>
  <transition name="modal-fade">
    <div class="modal-backdrop">
      <div class="modal-survey"
        role="dialog"
        aria-labelledby="modalTitle"
        aria-describedby="modalDescription">
        <!--header class="modal-header-survey" id="modalTitle">
          <slot name="header">
            This is the default tile!
            <button
              type="button"
              class="btn-close"
              @click="close"
              aria-label="Close modal">x</button>
          </slot>
        </header-->
        <section class="modal-body-survey" id="modalDescription">
          <slot name="body">
              <div>
                <form-wizard @on-complete="onComplete"
                        start-index:="0"
                        step-size="xs"
                        title="Report Export Survey"
                        subtitle="Thank you for helping us improve your reporting experience."
                        finish-button-text="Submit"
                        color="gray"
                        error-color="#a94442"
                     >
                     <tab-content title="Overview"
                                  icon="ti-info-alt">
                      <p>EVS is committed to providing you with an elegant and robust mechanism
                        to retrieve the data of interest to you.</p>

                      <p>Thank you for taking this brief survey.</p>

                     </tab-content>
                    <tab-content title="Features"
                                 icon="ti-stats-up" :before-change="validateFirstTab">
                       <vue-form-generator :model="Survey"
                                           :schema="firstTabSchema"
                                           :options="formOptions"
                                           ref="firstTabForm"
                                           >
                       </vue-form-generator>
                    </tab-content>
                    <tab-content title="Additional Info"
                                 icon="ti-comment-alt" :before-change="validateSecondTab">
                     <vue-form-generator :model="Survey"
                                           :schema="secondTabSchema"
                                           :options="formOptions"
                                           ref="secondTabForm"
                                           >
                       </vue-form-generator>

                    </tab-content>
                    <!--tab-content title="Last step"
                                 icon="ti-check">
                      <h4>Your json is ready!</h4>
                      <div class="panel-body">
                        <pre v-if="model" v-html="prettyJSON(model)"></pre>
                      </div>
                    </tab-content-->
                </form-wizard>
              </div>
          </slot>
        </section>
        <footer class="modal-footer-survey">
          <slot name="footer">
            <button type="button" class="btn btn-default" @click="close" aria-label="Close modal">
              Close
            </button>
          </slot>
        </footer>
      </div>
    </div>
  </transition>
</template>

<style scoped>

  pre {
    overflow: auto;
  }
	pre .string { color: #885800; }
	pre .number { color: blue; }
	pre .boolean { color: magenta; }
	pre .null { color: red; }
	pre .key { color: green; }

  .modal-backdrop{
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    background-color: rgba(0, 0, 0, 0.3);
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .modal-survey {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 800px;
    padding: 0px 0px 0px 15px;
    max-width: 90%;
    height: 650px;
    max-height: 90%;
    background: white;
    box-shadow: 2px 2px 20px 1px;
    overflow-x: auto;
    display: flex;
    flex-direction: column;
  }
  /*.modal-header-survey,*/
  .modal-footer-survey {
    padding: 10px;
    display: flex;
  }
  .modal-header-survey {
    border-bottom: 1px solid #eeeeee;
    justify-content: space-between;
  }
  .modal-footer-survey {
    border-top: 1px solid #eeeeee;
    justify-content: flex-end;
  }
  .modal-body-survey {
    height: 650px;
    overflow-y: auto;
    position: relative;
    padding: 20px 10px;
  }
</style>
