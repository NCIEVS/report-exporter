<script>
  import axios from 'axios';
  import VueFormGenerator from 'vue-form-generator'
   //import 'vue-form-generator/dist/vfg.css'
  //vue 3 counter for (Next) button due to form-wizard not working
  let selectNextOptionBTN_counter =  1;

  export default {
    name: 'modal',

    data(){
       return{
         surveyCookie: "NCIReportExporterSurvey",

          survey:{
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
            placeholder: "suggest features",
            rows: 3,
            //required:true,
            validator:VueFormGenerator.validators.string,
         }
         ]
       },
       secondTabSchema:{
         fields:[
         {
            type: "textArea",
            label: "Would you like to tell us about your project to see if we have additional recommendations?",
            model: "recommendations",
            hint: "Max 500 characters",
            max: 500,
            placeholder: "suggest recommendations",
            rows: 3,
            //required:true,
            validator:VueFormGenerator.validators.string,
         },
         {
           type: "input",
           inputType: "text",
           label: "Please supply your <b>email</b> if you would like a personal response.",
           model: "email",
           //required:true,
           validator:VueFormGenerator.validators.email,
        }
         ]
       }
     }
   },

    // Vue 3 Method used for hiding options on first Survey Screen
    mounted() {
      document.getElementById("surveyBackButton").style.display = "none";
      document.getElementById("surveyQuestion1").style.display = "none";
      document.getElementById("surveyAnswer1").style.display = "none";
      document.getElementById("surveyQuestion2").style.display = "none";
      document.getElementById("surveyAnswer2").style.display = "none";
      document.getElementById("surveyMaxCharacters").style.display = "none"
      document.getElementById("surveyQuestion3").style.display = "none";
      document.getElementById("surveyAnswer3").style.display = "none";
      document.getElementById("surveyQuestion4").style.display = "none";
      document.getElementById("surveyAnswer4").style.display = "none";
      document.getElementById("surveyMaxCharacters2").style.display = "none"
      document.getElementById("surveySubmit").style.display = "none"
    },

    methods: {

      // Vue 3 Method used for Survey Next Button
      surveyValidateFirstStep() {
        if (selectNextOptionBTN_counter === 1) {
          document.getElementById("surveyQuestion1").style.display = "";
          document.getElementById("surveyAnswer1").style.display = "";
          document.getElementById("surveyQuestion2").style.display = "";
          document.getElementById("surveyAnswer2").style.display = "";
          document.getElementById("surveyMaxCharacters").style.display = "";
          document.getElementById("surveyBackButton").style.display = "";
          document.getElementById("surveyQuestion3").style.display = "none";
          document.getElementById("surveyAnswer3").style.display = "none";
          document.getElementById("surveyQuestion4").style.display = "none";
          document.getElementById("surveyAnswer4").style.display = "none";
          document.getElementById("surveyMaxCharacters2").style.display = "none";
          document.getElementById("surveySubmit").style.display = "none";
        }

        if (selectNextOptionBTN_counter === 2) {
          document.getElementById("surveyBackButton").style.display = "";
          document.getElementById("surveyQuestion3").style.display = "";
          document.getElementById("surveyAnswer3").style.display = "";
          document.getElementById("surveyQuestion4").style.display = "";
          document.getElementById("surveyAnswer4").style.display = "";
          document.getElementById("surveyMaxCharacters2").style.display = "";
          document.getElementById("surveyQuestion1").style.display = "none";
          document.getElementById("surveyAnswer1").style.display = "none";
          document.getElementById("surveyQuestion2").style.display = "none";
          document.getElementById("surveyAnswer2").style.display = "none";
          document.getElementById("surveyMaxCharacters").style.display = "none";
          document.getElementById("surveySubmit").style.display = "";
          document.getElementById("surveyNextOption").style.display = "none";
        }
        selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1
      },

      // Vue 3 Method used for Survey Back Button
      surveyBackStep(){
        if (selectNextOptionBTN_counter === 2) {
          document.getElementById("surveyBackButton").style.display = "none";
          document.getElementById("surveyQuestion1").style.display = "none";
          document.getElementById("surveyAnswer1").style.display = "none";
          document.getElementById("surveyQuestion2").style.display = "none";
          document.getElementById("surveyAnswer2").style.display = "none";
          document.getElementById("surveyMaxCharacters").style.display = "none";
          document.getElementById("surveyQuestion3").style.display = "none";
          document.getElementById("surveyAnswer3").style.display = "none";
          document.getElementById("surveyQuestion4").style.display = "none";
          document.getElementById("surveyAnswer4").style.display = "none";
          document.getElementById("surveyMaxCharacters2").style.display = "none";
          document.getElementById("surveySubmit").style.display = "none";
        }

        if (selectNextOptionBTN_counter === 3) {
          document.getElementById("surveyQuestion1").style.display = "";
          document.getElementById("surveyAnswer1").style.display = "";
          document.getElementById("surveyQuestion2").style.display = "";
          document.getElementById("surveyAnswer2").style.display = "";
          document.getElementById("surveyMaxCharacters").style.display = "";
          document.getElementById("surveyBackButton").style.display = "";
          document.getElementById("surveyQuestion3").style.display = "none";
          document.getElementById("surveyAnswer3").style.display = "none";
          document.getElementById("surveyQuestion4").style.display = "none";
          document.getElementById("surveyAnswer4").style.display = "none";
          document.getElementById("surveyMaxCharacters2").style.display = "none";
          document.getElementById("surveySubmit").style.display = "none";
          document.getElementById("surveyNextOption").style.display = "";
        }
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;
      },


      close() {
        this.clearValues();
        this.$emit('close');
      },
      clearValues: function() {
        this.survey.rating=null;
        this.survey.features='';
        this.survey.recommendations='';
        this.survey.email='';
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
         axios.post(
            this.$baseURL + "survey",
            this.survey,
          )
          .then(() => {
            this.close();
            alert('Thanks for taking the survey!');
            this.setSurveyCookie()
          })
          .catch(error => {
              alert('There was an error submitting the survey.  Please try again later.');
              console.log("error submitting survey..." + error)
              this.close();
          });
      },

      setSurveyCookie() {
        this.$cookies.set(this.surveyCookie,"true");
      },


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

        <section class="modal-body-survey" id="modalDescription">
          <slot name="body">
              <div>
                <div class="vue-form-wizard">
                  <div class="wizard-header">
                    <h4 class="wizard-title">Report Export Survey</h4>
                    <p class="category">Thank you for helping us improve your reporting experience.</p>
                  </div>
                </div>
                <p id = "surveyMessage">
                      EVS is committed to providing you with an elegant and robust mechanism
                        to retrieve the data of interest to you.
                      Thank you for taking this brief survey.
                </p>

                <span id = "surveyQuestion1">
                  How would you rate the ease of retrieving the desired data? 1 is difficult, 10 is easy
                </span>

                <select id="surveyAnswer1" class="form-control"><option disabled="disabled" value="">&lt;Nothing selected&gt;</option><!----><option value="1">1</option><!----><option value="2">2</option><!----><option value="3">3</option><!----><option value="4">4</option><!----><option value="5">5</option><!----><option value="6">6</option><!----><option value="7">7</option><!----><option value="8">8</option><!----><option value="9">9</option><!----><option value="10">10</option></select>
                <br>
                <span id = "surveyQuestion2">
                  What features would you like to see on this application?
                </span>

                <textarea id="surveyAnswer2" maxlength="500" placeholder="suggest features" rows="3" class="form-control"></textarea>

                <div class="hint" id = "surveyMaxCharacters">Max 500 characters</div>
                <br>


                <span id = "surveyQuestion3">
                  What features would you like to see on this application?
                </span>

                <textarea id="surveyAnswer3" maxlength="500" placeholder="suggest features" rows="3" class="form-control"></textarea>

                <div class="hint" id = "surveyMaxCharacters2">Max 500 characters</div>

                <br>


                <span id = "surveyQuestion4">
                  Please supply your email if you would like a personal response.
                </span>

                <input id="surveyAnswer4" type = "text" class="form-control">

                <br>
                <span role="button" tabindex="0">
                  <center>
                    <button tabindex="-1" type="button" id = "surveyBackButton" class="Survey-btn" v-on:click="surveyBackStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Back </button>

                    <button tabindex="-1" type="button" id = "surveyNextOption" class="Survey-btn" v-on:click="surveyValidateFirstStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Next </button>

                    <button tabindex="-1" type="button" id = "surveySubmit" class="Survey-btn" v-on:click="submitForm()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Submit </button>

                  </center>
                </span>


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
    height: 600px;
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
    height: 600px;
    overflow-y: auto;
    position: relative;
    padding: 20px 10px;
  }

  .Survey-btn{
    background-color: rgb(0, 125, 188);
    border-color: rgb(0, 125, 188);
    color: white;
    padding: 5px;
    border-radius: 4px;
    margin-left: 2px;
    width: 158px;
  }

  .Survey-btn1{
    background-color: rgb(0, 125, 188);
    border-color: rgb(0, 125, 188);
    color: white;
    padding: 5px;
    border-radius: 4px;
    margin-left: 600px;
    width: 158px;
  }
</style>
