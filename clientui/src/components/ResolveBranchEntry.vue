<template>
  <div id="read-codes-entry" class="container">


    <div class="vue-form-wizard">
      <div class="wizard-header">
        <h4 class="wizard-title">Resolved Branch Export</h4>
        <p class="category">Steps to select a top node, then its properties and export the results. Resolutions in the thousands and more will take some time.</p>
      </div>
    </div>

    <!--Vue 3 Start-->
    <div class="container" id = "exportStep">
      <div class="row justify-content-center">
        <div class="col-12 col-md-6">
          <form>
            <div class="form-group">
              <div>
                <label for="downloadFormatLabel">Select format for export</label>
                <select id="downloadFormat" class="form-control"  @change="changeSelectedExportList($event)">
                  <option value="json"> JSON (json) JavaScript Object Notation Format </option>
                  <option value="csv"> CSV (csv) Comma Separated Value Format </option>
                  <option value="txt"> TABD (txt) Tab Delimited Value Format </option>
                  <option value="xlsx"> EXCEL (xlsx) Microsoft Excel Format </option>
                </select>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!--Vue 3 End-->



    <!--Vue 3 Entity Label field  Start-->
    <div class="entityLabel" id = "entityLabelId">
      <label for="tags" >Enter NCI Thesaurus concept codes</label>
    </div>
    <!--Vue 3 Entity Label field  End-->

    <!--Vue 3 Entity Text field  Start-->
    <div class="entityText" id = "entityTextID" element-id="tag-input">
      <input placeholder="Type entity code, then click enter"
             class="entityCodeInput" v-model="newTag"
             @keyup.enter.exact="addTag1(newTag)"
             @keyup.space.exact="addTag1(newTag)">
      <br>
      <br>
      <div class = "tag-input"></div>
      <ul class="tags" id = "listOfTags">
        <li><a v-for="tag in tags" :key="tag" class="tag" id="tags2">
          {{ tag }}
          <button class="delete" @click="removeTag(index)">x</button>
        </a></li>
      </ul>


      <label for="levelSelection" class = "levels-label">Select how many levels to retrieve</label>
      <br>
      <select v-model="selectedLevel" id="levelSelection" class="levelsDropdown" v-on:change="onLevelChange()">
        <option v-for="level in levels"
                :value="level.id"
                :key="level.name">
          {{ level.name }}
        </option>
      </select>
    </div>




<br>
    <br>
    <div id="app"  class = "tree-display">
      <Tree
          id="my-tree-id"
          ref="my-tree"
          :custom-options="myCustomOptions"
          :custom-styles="myCustomStyles"
          :nodes="treeDisplayData"
          :@dblclick="getNodeValue"
      ></Tree>
    </div>
    <br>

    <!--Vue 3 step 2 list boxes Start -->
    <div id="app" class="container">
      <div role="tabpanel" id="SelectProperties1" aria-labelledby="step-SelectProperties1" class="wizard-tab-container" style>
        <div class="container">
          <form>
            <div class="form-group">
              <label for="selectedProperties" id = "SelectProperties2">Select properties to include in the export</label>
            </div>
            <div class="form-group">
              <div class="msl-multi-select">
                <div class="msl-searchable-list msl-multi-select__list">
                  <input placeholder="Search properties" class="msl-search-list-input custom-input-class" id = "searchProperties" @keyup = "searchPropertiesFilter()">
                  <select multiple v-model="leftSelectedUsers" @dblclick="moveRight" class="msl-searchable-list__items" id = "selectSearchProperties">
                    <option v-for="userLeft in availableProperties" :key="userLeft" class="multi-select-option msl-searchable-list__item" id = "optionSearchProperties">
                      {{ userLeft }}
                    </option>
                  </select>
                </div>
                <div class="listBoxButton">
                  <table>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td><input type = "button" value = "=>" id = "toListBox" class = "toListBox" @click="moveRight"></td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                  </table>

                  <table>
                    <tr>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td><input type = "button" value = "<=" id = "fromListBox" class = "fromListBox" @click="moveLeft"></td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                      <td>&nbsp;</td>
                    </tr>
                  </table>
                </div>
                <div class="msl-searchable-list msl-multi-select__selected msl-multi-select__list">
                  <input placeholder="Search selected properties" class="msl-search-list-input custom-input-class"  id = "selectedProperties" @keyup = "searchSelectedPropertiesFilter()" >
                  <select multiple v-model="rightSelectedUsers" @dblclick="moveLeft" class="msl-searchable-list__items" id = "selectSelectedProperties">
                    <option v-for="userRight in rightUsers" :key="userRight" class="multi-select-option msl-searchable-list__item" id = "optionSelectedProperties">
                      {{ userRight }}
                    </option>
                  </select>
                </div>
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
    <!--Vue 3 step 2 list boxes End -->


    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "clearButton" class="btn-delete" v-on:click="removeAllTags2(0)"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Clear </button>
      </span>

    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "backButton" class="btn-back" v-on:click="backStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Back </button>
      </span>

    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "nextOption" class="btn-next" v-on:click="validateFirstStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Select Next Option </button>
      </span>

    <span role="button" tabindex="0">
        <button tabindex="-1" type="button" id = "exportButton" class="btn-export" v-on:click="exportStep()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Export </button>
      </span>





    <!--Vue 3 Entity Text field  End-->



    <!-- Summary Information -->
    <div id="accordion" class="pb-3 pt-3">
      <div class="card">
        <div class="card-header" id="headingOne"  style="
              padding-left: 1px;
              padding-right: 1px;
              padding-bottom: 1px;
              padding-top: 1px;
          ">
          <center>
            <button class="btn btn-link"  v-on:click="this.updateShowSummary" data-toggle="collapse" data-target="#collapseSummary" aria-expanded="true" aria-controls="collapseSummary">
              {{this.showSummaryText}}
            </button>
          </center>

        </div>


        <div id="accordion" class="pb-3 pt-3">
          <div class="card">
            <div id="headingOne" class="card-header" style="padding: 1px;">



              <!--
            <center>
              <button data-toggle="collapse" data-target="#collapseSummary" aria-expanded="true" aria-controls="collapseSummary" class="btn btn-link"> Hide Selection Summary </button>
            </center>
            -->
              <!-- Vue3 Selection Summary List boxes Start  -->
            </div>
            <div id="collapseSummary" aria-labelledby="headingOne" data-parent="#accordion" class="collapse show">
              <div class="card-body pb-1">
                <div class="row p-1">
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">
                        Selected Concept Codes
                        <span class="badge badge-secondary" id = "selectConceptCodesCount">{{Object.keys(this.tags).length}}</span>
                      </div>


                      <div class="card-body">
                        <span class="list-group" id="selectedConceptCodesTags">{{ this.tags }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">
                        Selected Properties
                        <span class="badge badge-secondary">{{Object.keys(this.rightUsers).length}}</span>
                      </div>
                      <div class="card-body">
                        <span class="list-group" id="selectedPropertyList">{{ this.rightUsers }}</span>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">Selected Export Format</div>
                      <div class="card-body">
                        <ul class="list-group" id="selectedPropertyList">
                          <li>
                            {{ selectedExportListName }}
                          </li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- Vue3 Selection Summary List boxes End  -->

      </div>
    </div>
  </div>
</template>

<script>

// Custom input tags
import api from '../api.js'
import axios from 'axios'
import 'vue-loading-overlay/dist/vue-loading.css'
import Tree from 'vuejs-tree'
import {ref} from "vue";



//vue 3 counter for (Select Next Option) button due to form-wizard not working
let selectNextOptionBTN_counter =  1;


export default {
  name: 'resolve-branch-entry',
  props: {
    msg: String
  },
  components: {
    Tree,
  },
  metaInfo: {
    title: 'EVS Report Exporter - Branch Resolve',
  },

  mounted() {
    this.hideObjectsOnScreen();  //function for when page loads certain objects like buttons or text boxes will be hidden
    this.selectedExportListName = "JSON (json) JavaScript Object Notation Format"
    this.$refs["my-tree"].expandNode(1);
  },
  setup(){
    const tags = ref([]);
    //const loadBaseURL = ref([]);
    //var  url= ref();
    const newTag = ref('') //keep up with new tag
    var tagCounter1 = 0;
    //var newTagCounter = 0;
    //this.entityList = []'


    //Vue 3 Remotes a tag below text box
    const removeTag = (index) => {
      alert(index.value);
      tags.value.splice(index, 1);
      tagCounter1 = tagCounter1  - 1;
    };

    return { tags, newTag, removeTag, tagCounter1 }
  },


  computed: {
    myCustomStyles() {
      return {
        tree: {
          style: {
            height: "auto",
            maxHeight: "300px",
            overflowY: "visible",
            display: "inline-block",
            textAlign: "left",
          },
        },
        row: {
          style: {
            width: "500px",
            cursor: "pointer",
          },
          child: {
            class: "",
            style: {
              height: "35px",
            },
            active: {
              style: {
                height: "35px",
              },
            },
          },
        },
        rowIndent: {
          paddingLeft: "20px",
        },
        text: {
          // class: "" // uncomment this line to overwrite the 'capitalize' class, or add a custom class
          style: {},
          active: {
            style: {
              "font-weight": "bold",
              color: "#2ECC71",
            },
          },
        },
      };
    }
  },


  data() {
    return {
      selectedTags: [],
      message: 'Hello World!',
      userEnteredCodes: [],
      entityList: [],
      availableProperties: [],
      selectedProperties: [],
      userSelectedProperyNames: [],
      userSelectedFormat: {"name": "JSON", "description": "JavaScript Object Notation Format", "extension": "json"},
      curratedTopNodes: [],
      userSelectedTopNode: '',
      filename: 'branch',
      downloadReturnCode: null,
      curratedTopNodesUI: [],
      getPropertyError: false,
      selectedLevel: 0,
      childrenToResolve: 0,
      tempListClear:[],
      leftSelectedUsers:[],
      leftUsers: [],
      rightSelectedUsers:[],
      rightUsers:[],
      levels: [
        {id: 1, name: '1 Level'},
        {id: 2, name: '2 Levels'},
        {id: 3, name: '3 Levels'},
        {id: 4, name: '4 Levels'},
        {id: 5, name: '5 Levels'},
        {id: 6, name: '6 Levels'},
        {id: 7, name: '7 Levels'},
        {id: 8, name: '8 Levels'},
        {id: 9, name: '9 Levels'},
        {id: 10, name: '10 Levels'},
        {id: 11, name: '11 Levels'},
        {id: 12, name: '12 Levels'},
        {id: 13, name: '13 Levels'},
        {id: 14, name: '14 Levels'},
        {id: 15, name: '15 Levels'},
        {id: 16, name: '16 Levels'},
        {id: 17, name: '17 Levels'},
        {id: 18, name: '18 Levels'},
        {id: 19, name: '19 Levels'},
        {id: 20, name: '20 Levels'},
      ],
      childrenToResolveObj: {
        selectedLevel: 0,
        selectedTag: "",
        childrenCount: 0
      },
      deferredStatusUrl: '',
      deferredStatusHash: '',
      deferredStatus: false,
      showTree: true,
      asyncData: [],
      treeSelectedCode: null,
      showSummary: true,
      showSummaryText: '',
      exportType: 'exportNow',
      treeLevel: 'LEVEL 1',

      treeDisplayData: [
        {
          text: this.treeLevel,
          state: {checked: false, selected: true, expanded: false},
          id: 1,
          checkable: true,
          nodes: [
              {
            text: "Level 2",
            state: {checked: false, selected: true, expanded: false},
            id: 2,
            checkable: true,
            nodes: [           {
              text: "level 3",
              state: {checked: false, selected: true, expanded: false},
              checkable: true,
              id: 5,
              nodes: [],

            },],
          },
          ],
        },
            {
              text: "Level 2",
              state: {checked: false, selected: true, expanded: false},
              id: 2,
              checkable: true,
              nodes: [],
            },
           {
                  text: "level 3",
                  state: {checked: false, selected: true, expanded: false},
                  checkable: true,
                  id: 5,
                  nodes: [],

            },
            {
              text: "Child 2",
              state: { checked: false, selected: false, expanded: false },
              checkable: true,
              id: 4,
            },
        ] ,



      // function to get tree data
      loadData: function (oriNode, resolve) {
        // set id to the node to retrieve children for.
        // set to null to indicate this is the root.
        var id = oriNode.data.id ? oriNode.data.id : null
        var data = []
        //console.log('id: ' + id)

        alert("test1");
        // if id is null, this is the root.  get all root children
        if (id == null) {
          alert("test2");
          api.getRoots(this.$baseURL)
              .then((children) => {
                if (children != null) {
                  alert("test3");
                  for (let x = 0; x < children.length; x++) {
                    alert("test5");
                    //console.log(children[x].code + '  :  ' + children[x].name)
                    data.push(
                        {

                          //   "id": children[x].code,
                          //   "text": children[x].code + ' : ' + children[x].name,
                          //    "isLeaf": false,
                          //    "disabled": children[x].leaf,

                          "id": children[x].code,
                          "text": children[x].code + ' : ' + children[x].name,
                          "checkable": false,
                          "state": children[x].leaf,
                        },
                    )
                  }alert("test6");
                  resolve(data)
                } else {
                  console.log("Error retrieving roots");
                  data.push(
                      {
                        "id": '0',
                        "text": 'Error retrieving tree',
                        "isLeaf": true,
                      },
                  )
                  resolve(data)
                }
              })
        }

        // Id was not null, get the children
        else {
          api.getChildren(this.$baseURL, id, 1)
              .then((children) => {
                if (children != null) {
                  for (let x = 0; x < children.length; x++) {
                    //console.log(children[x].code + '  :  ' + children[x].name)
                    data.push(
                        {
                          "id": children[x].code,
                          "text": children[x].code + ' : ' + children[x].name,
                          "isLeaf": children[x].leaf,
                          "disabled": children[x].leaf,
                        },
                    )
                  }
                  resolve(data)
                } else {
                  console.log("Error retrieving children");
                  data.push(
                      {
                        "id": '0',
                        "text": 'Error retrieving tree',
                        "isLeaf": true,
                      },
                  )
                  resolve(data)
                }
              })
        }
      },

    }
  },

  methods: {

    getNodeValue(){
      this.treeLevel = "TEST"
      alert("test");
    },
    myCustomOptions() {

      return {
        treeEvents: {
          expanded: {
            state: false,
          },
          collapsed: {
            state: false,
          },
          selected: {
            state: true,
            fn: this.mySelectedFunction,
          },
          checked: {
            state: true,
            fn: this.myCheckedFunction,
          },
        },
        events: {
          expanded: {
            state: true,
          },
          selected: {
            state: true,
            fn: this.mySelectedFunction,
          },
          checked: {
            state: true,
            fn: this.mySelectedFunction,
          },
          editableName: {
            state: false,
            calledEvent: "expanded",
          },
        },
        addNode: {
          state: true,
          fn: this.addNodeFunction,
          appearOnHover: false,
        },
        editNode: { state: false, fn: null, appearOnHover: false },
        deleteNode: {
          state: true,
          fn: this.deleteNodeFunction,
          appearOnHover: true,
        },
        showTags: true,
      };
    },


    myCheckedFunction (nodeId, disabled) {
      alert("myCHeckedFunction");
      this.loadData(nodeId, disabled);
      console.log(`is ${nodeId} checked ? ${disabled}`);
      alert(this.$refs["my-tree"].getCheckedNodes("id"));
      alert(this.$refs["my-tree"].getCheckedNodes("text"));
    },
    mySelectedFunction: function (nodeId, disabled) {
      this.loadData(nodeId, disabled);
      console.log(`is ${nodeId} selected ? ${disabled}`);
      console.log(this.$refs["my-tree"].getSelectedNode());
    },
    deleteNodeFunction: function (node) {
      const nodePath = this.$refs["my-tree"].findNodePath(node.id);
      const parentNodeId = nodePath.slice(-2, -1)[0];
      if (parentNodeId === undefined) {
        // 'root' node
        const nodeIndex =
            this.$refs["my-tree"].nodes.findIndex((x) => x.id !== node.id) - 1;
        this.$refs["my-tree"].nodes.splice(nodeIndex, 1);
      } else {
        // child node
        const parentNode = this.$refs["my-tree"].findNode(parentNodeId);
        const nodeIndex =
            parentNode.nodes.findIndex((x) => x.id !== node.id) - 1;
        parentNode.nodes.splice(nodeIndex, 1);
      }
      console.log("example: remove node", node.id);
    },
    addNodeFunction: function (node) {
      const newNode = {
        text: "Grandchild 2",
        id: Math.floor(Math.random() * 100),
        disabled: { checked: false, selected: false, expanded: false },
      };
      console.log("example: add node", newNode);
      if (node.nodes === undefined) {
        node.nodes = [newNode];
      } else {
        node.nodes.push(newNode);
      }
    },







    removeAllTags2 (tagDeleteCounter) {
      alert("REmove value " + tagDeleteCounter);
      alert("Counter value " + this.tags.length);
      for (let i = 0; i <= this.tags.length; i++) {
        this.tags.splice(tagDeleteCounter, this.tags.length);
        tagDeleteCounter = tagDeleteCounter + 1;
      }
      this.tag = []
      this.newTag = []
      this.userEnteredCodes = []
      this.selectedTags = []
      this.entityList = []
      this.multipleEntitiesSplit = []
      this.invalidTag = ''
      this.userSelectedProperyNames = []
      this.tags2 = []
    },

    addTag1(tag) {
      var codeDescription = [];
      //var baseLink = document.getElementById('basURL').value;
      var tagCounter = 0;
      var newTagCounter = 0;
      //const tags = ref([]);
      //const newTag = ref('') //keep up with new tag
      tag = tag.replace(/[\s/]/g, '')
      tag = tag.replace(',', '')


      if (tag != "") {
        api.getCodes( this.$baseURL, tag, 'ENTITY')
            .then((data)=> {
              // alert("after call");
              // data = "test";
              if ((data != null) && (data!== undefined)) {
                alert("after checks");
                for (let x = data.length - 1; x >= 0; x--) {
                  //  alert("Code: " + data[x].code + " is invalid: " + data[x].queryStatus + " roles: " + data[x].roles + " association: " + data[x].associations + " Description: " + data[x].name);
                  codeDescription = data[x].name;
                  alert("before push");
                  this.tags.push(tag + ":" + codeDescription);
                  this.newTag = ""; // reset newTag
                  tagCounter = tagCounter + 1;
                  newTagCounter = newTagCounter + 1;
                  //   alert("Before getEntities Call()");
                  //getEntities();
                  //  alert("After getEntities Call()");
                }
              }else {
                this.tags.push(tag + ":" + "");
                this.newTag = ""
                tagCounter = tagCounter + 1;
                newTagCounter = newTagCounter + 1;
                alert("tag counter " + newTagCounter);
                this.$notify({
                  group: 'app',
                  title: 'Validation Failure',
                  text: 'Could not verify concept code(s).  Possible network issue.',
                  type: 'error',
                  duration: 4000,
                  position: "left bottom"
                });

              }

            })
      }
    },

    moveLeft() {
      if(!this.rightSelectedUsers.length) return;
      for(let i=this.rightSelectedUsers.length;i>0;i--) {
        let idx = this.rightUsers.indexOf(this.rightSelectedUsers[i-1]);
        this.rightUsers.splice(idx, 1);
        this.availableProperties.push(this.rightSelectedUsers[i - 1])
        this.rightSelectedUsers.pop();
      }
    },


    moveRight() {
      if (!this.leftSelectedUsers.length) return;
      for (let i = this.leftSelectedUsers.length; i > 0; i--) {
        let idx = this.availableProperties.indexOf(this.leftSelectedUsers[i-1]);
        this.availableProperties.splice(idx, 1);
        this.rightUsers.push(this.leftSelectedUsers[i - 1]);
        this.tempListClear.push(this.leftSelectedUsers[i - 1]);
        this.leftSelectedUsers.pop();
      }
    },

    //Vue 3 Start Step 2 left Search Function
    searchPropertiesFilter() {
      var input;
      var formattedInput;
      var listBoxValues;
      var selectedListBoxValue;
      var i;
      var txtValue;

      input = document.getElementById("searchProperties"); //text input
      formattedInput = input.value.toUpperCase();  //change to upper case

      listBoxValues = document.getElementById("selectSearchProperties");
      for (i = 0; i < listBoxValues.length; i++) {
        selectedListBoxValue = listBoxValues[i];
        txtValue = selectedListBoxValue.textContent || selectedListBoxValue.innerText;
        if (txtValue.toUpperCase().indexOf(formattedInput) > -1) {
          listBoxValues[i].style.display = "";
        } else {
          listBoxValues[i].style.display = "none";
        }
      }
    },
    //Vue 3 End



    //Vue 3 Start Step 2 Right Search Function
    searchSelectedPropertiesFilter() {
      var input;
      var formattedInput;
      var listBoxValues;
      var selectedListBoxValue;
      var i;
      var txtValue;

      input = document.getElementById("selectedProperties"); //text input
      formattedInput = input.value.toUpperCase();  //change to upper case

      listBoxValues = document.getElementById("selectSelectedProperties");
      for (i = 0; i < listBoxValues.length; i++) {
        selectedListBoxValue = listBoxValues[i];
        txtValue = selectedListBoxValue.textContent || selectedListBoxValue.innerText;
        if (txtValue.toUpperCase().indexOf(formattedInput) > -1) {
          listBoxValues[i].style.display = "";
        } else {
          listBoxValues[i].style.display = "none";
        }
      }
    },
    //Vue 3 End

    //When page loads certain objects like buttons or text boxes will be hidden
    hideObjectsOnScreen() {
      document.getElementById("SelectProperties1").style.display = "none";
      document.getElementById("backButton").style.display = "none";
      document.getElementById("exportStep").style.display = "none";
      document.getElementById("exportButton").style.display = "none";


    },
    validateFirstStep() {
      // make sure the user has a code entered

      //Vue 3 Select Next Option Counter.  This counter replaces the form-Wizard logic that is not working
      //correctly under vue 3.  If value is 1 then it implements validateFirstStep fucction.  If value is 2 then
      //it implements validatePropertyStep function.  If validateExportStep is 3 then it implements the validateExportSetup function
//alert (this.availableProperties.value);
      //    var obj = JSON.parse(this.availableProperties.value);
      //alert("JSON " + obj);
      //  document.getElementById("selectSearchProperties").innerHTML = obj.name;
      //alert("Test2");

      //Vue 3 STEP 1
      if (selectNextOptionBTN_counter === 3) {
        alert("counter 3 if disabledment")
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextButton").style.display = ""; //Hides next button
      }

      if (selectNextOptionBTN_counter === 2) {
        this.validatePropertyStep();
      }


      alert(selectNextOptionBTN_counter);
      alert("test c");
      alert(Object.keys(this.selectedTags).length);

      //Vue 3 (Builds screen on step 2)
      if (selectNextOptionBTN_counter === 1) {
        //  if (Object.keys(this.tags).length > 0) {  // checks to make sure that a code was entered before proceeding to next screen
        document.getElementById("clearButton").style.display = "none";    //Hides clear button
        document.getElementById("entityTextID").style.display = "none";   //Hides textbox on main screen
        document.getElementById("entityLabelId").style.display = "none";  //Hides label on main screen
        document.getElementById("SelectProperties1").style.display = "";  //Shows listboxs on second screen
        document.getElementById("backButton").style.display = "";     //Shows back button
        selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1  // Counter controls navigating between steps 1 -3

        // load properties after the page is loaded.
        /*
                  api.getProperties(this.$baseURL)
                      .then((data)=>{this.availableProperties = data[-1];
                      })
        */
        //
        api.getProperties(this.$baseURL)
            .then((data)=> {
              for (let x = 0 ; x < data.length; x++) {
                this.availableProperties.push(data[x].name);
              }
            })



        for (let i = 0; i <= this.rightUsers.length + 2; i++) {
          this.rightUsers.pop();
        }


        //}
      }


    },

    //Vue 3 STEP 2
    validatePropertyStep() {
      // make sure the user has selected at least one property
      //Hides objects on screen that shouldn't appear in step 2
      // document.getElementById("entityTextID").style.display = " ";
      // document.getElementById("entityLabelId").style.display = " ";

      if (this.rightUsers.length > 0) {
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextOption").style.display = "none"; //Hides next option button

        if (Object.keys(this.rightUsers).length > 0) {
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1;
          return Object.keys(this.rightUsers).length > 0
        }
      }
    },
    backStep(){
      //Shows screen for step 1
      if (selectNextOptionBTN_counter === 2) {
        document.getElementById("SelectProperties1").style.display = "none";  //shows listboxs on second screen
        document.getElementById("clearButton").style.display = "";    //Shows clear button
        document.getElementById("entityTextID").style.display = "";   //Shows textbox on main screen
        document.getElementById("entityLabelId").style.display = "";  //Shows label on main screen
        document.getElementById("backButton").style.display = "none"; //Hides back button on main screen
        document.getElementById("nextOption").style.display = "";     //Shows next button
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;

        if(!this.rightUsers.length) return;
        for(let i=this.rightUsers.length;i>0;i--) {
          let idx = this.rightUsers.indexOf(this.rightSelectedUsers[i-1]);
          this.rightUsers.splice(idx, 1);
          this.availableProperties.push(this.tempListClear[i - 1])
          this.rightSelectedUsers.pop();
          this.tempListClear.pop();
        }
        this.availableProperties.sort();

      }

      //Shows screen =for step 2
      if (selectNextOptionBTN_counter === 3) {
        document.getElementById("SelectProperties1").style.display = "";  //shows listboxs on second screen
        document.getElementById("backButton").style.display = "";     //Shows back button on main screen
        document.getElementById("exportButton").style.display = "none"; //Hides Export button
        document.getElementById("nextOption").style.display = ""; //Hides next button
        document.getElementById("exportStep").style.display = "none";  //Hides Export Step
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;
      }
    },

    setSelectedPropertyNames() {
      this.userSelectedProperyNames = []
      for (let i = 0; i < this.rightUsers.length; i++) {
        this.userSelectedProperyNames.push(this.rightUsers[i].name)
      }
    },



    //Vue 3 Function controls Select format Export dropdown on Step 3
    changeSelectedExportList(event){
      this.userSelectedFormat = event.target.value;
      if (event.target.value === "json") {
        this.fileFormat = "JSON";
        this.selectedExportListName = "JSON (json) JavaScript Object Notation Format"
      }

      if (event.target.value === "csv") {
        this.fileFormat = "CSV";
        this.selectedExportListName = "CSV (csv) Comma Separated Value Format"
      }

      if (event.target.value === "txt") {
        this.fileFormat = "TABD";
        this.selectedExportListName = "TABD (txt) Tab Delimited Value Format"
      }

      if (event.target.value === "xlsx") {
        this.fileFormat = "EXCEL";
        this.selectedExportListName = "EXCEL (xlsx) Microsoft Excel Format"
      }
    },


    exportStep() {
      this.downloadFile();
    },






    gaTrackDownload () {
      // Send Google analytics download event
      this.$gtag.query('event', "Branch Resolve Download", {
        'event_category': "Download",
        'event_label': this.userSelectedFormat.name
      })
    },
    gaTrackDeferredDownload () {
      // Send Google analytics deferred download event
      this.$gtag.query('event', "Branch Resolve Deferred Download", {
        'event_category': "Download",
        'event_label': this.userSelectedFormat.name
      })
    },
    // Tree dialog user chose a tree node
    userSelectTreeBranchNode() {
      //console.log('userSelectTreeBranchNode - user selected:' + this.treeSelectedCode)
      if (this.treeSelectedCode !== null) {
        this.selectedTags = [
          { key: this.treeSelectedCode, value: this.treeSelectedCode },
        ]
      }
    },

    // tree item clicked
    itemClick (node) {
      //console.log(node.model.id + ' clicked !')
      this.treeSelectedCode = node.model.id;
    },

    validateExportStep() {
      // make sure there is an export format selected.
      return this.userSelectedFormat !== null
    },

    onFormatUpdated (updatedFormat) {
      this.userSelectedFormat = updatedFormat
    },

    onComplete: function() {
      //this.downloadFile();

      // set the user selected tags and properties
      this.setSelectedTags()
      this.setSelectedPropertyNames()

      if (this.exportType == 'exportNow') {
        // export and wait for it to complete
        this.initiateDeferredDownloadAndWait()
      }
      else {
        // export and get a URL to go to later
        this.initiateDeferredDownloadAndReturn()
      }
    },

    async pollForStatus(hashId) {

      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });

      // check if a polling url was returned.
      if (this.deferredStatusUrl != null && this.deferredStatusUrl.length >0) {

        // loop and wait until the status comes back as true
        while (this.deferredStatus != null &&
        this.deferredStatus != "ERROR" &&
        this.deferredStatus != 'TRUE') {
          this.pollDeferredStatus()
          await this.sleep(500);
        }
        loader.hide()

        if (this.deferredStatus === "ERROR") {
          alert("Error downloading deferred file");
        }
        else {
          //this.clearDeferredData()
          // verify status is good and we can download
          this.downloadDeferredResult(hashId)
        }
        this.clearDeferredData()
      }
      else {
        console.log("deferredStatusUrl not good")
      }
    },

    // Toggle the Show/Hide Selection Summary title
    updateShowSummary() {
      this.showSummaryText = this.showSummary? 'Hide Selection Summary' : 'Show Selection Summary'
      this.showSummary = !this.showSummary;
    },

    // clear the entitiy code in the input selection
    clearSelection() {
      this.userEnteredCodes = []
      this.selectedTags = []
      this.entityList = []
    },

    setCurratedTags() {
      this.curratedTopNodesUI = []
      //console.log ("length: " + Object.keys(this.curratedTopNodes).length);

      // loop through the curratedTopNodes and create another object array
      // in the form the the tags input widget requires:
      // [{ key: 'someKey', value: 'someValue' }];
      for (let i = 0; i < Object.keys(this.curratedTopNodes).length; i++) {
        //console.log ("key " + this.curratedTopNodes[i].code + "  value " + this.curratedTopNodes[i].name)
        this.curratedTopNodesUI.push({"key":this.curratedTopNodes[i].code, "value":this.curratedTopNodes[i].code + ":" +this.curratedTopNodes[i].name})
      }
    },

    onTagAdded() {
      //console.log("Added tag: " + newCode)
      // When a top node is entered/selected, verify it.
      this.getEntities();
      this.updateChildrenToResolve()
    },

    onLevelChange() {
      this.updateChildrenToResolve()
    },

    updateChildrenToResolve() {
      // if the selectedTag and selectLevel have changed, set them in the
      // object and get the NEW childrenCount
      if ((this.childrenToResolveObj.selectedTag != this.selectedTags[0].key) ||
          (this.childrenToResolveObj.selectedLevel != this.selectedLevel))
      {
        this.childrenToResolveObj.selectedTag = this.selectedTags[0].key
        this.childrenToResolveObj.selectedLevel = this.selectedLevel

        // show the busy indicator
        let loader = this.$loading.show({
          container: this.$refs.formSelectCodes,
          loader: 'dots',
          isFullPage: false,
        });

        api.getChildren(this.$baseURL, this.selectedTags[0].key, this.selectedLevel)
            .then((children)=>{
              if (children != null) {
                this.childrenToResolveObj.childrenCount = children.length
              }
              else {
                this.childrenToResolveObj.childrenCount = 0
              }
            }).catch(function(error) {
          console.error("Error retrieving children to resolve: " + error);
        }).finally(function() { loader.hide()});
      }
    },

    setSelectedTags() {
      // clear the internal user codes that are entered
      this.userEnteredCodes = []

      for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
        // currated top nodes (from the server hava a value of "C12434:Blood")
        // so we need to strip off everything from the : to the right.
        this.userEnteredCodes.push(this.selectedTags[i].value.split(":",1))
      }
    },


    // Update the top node that was entered with the description.
    // User enters "C12434", the updated value displayed will be "C12434:Blood".
    // If entered value is not valid, remove it and display an error message.
    updateSelectedTopNodeDescription(topNode){
      if (topNode.length >0) {
        this.selectedTags[0].key = topNode[0].code;
        this.selectedTags[0].value = topNode[0].code + ":" + topNode[0].name;
      }

      //console.log ("Updating top node: " + topNode[0].code + "  " + topNode[0].name)
      //for (let i = 0; i < Object.keys(this.curratedTopNodesUI).length; i++) {
      //console.log ("key " + this.curratedTopNodesUI[i].key + "  value " + this.curratedTopNodesUI[i].value)
      //}
    },

    updateCurratedTopNodes (topNode) {
      this.userSelectedTopNode = topNode;
    },

    getEntities(){
      // clear the entry list
      this.entityList = []
      this.setSelectedTags()
      var tempCode = ''
      var tempStatus = ''

      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formSelectCodes,
        loader: 'dots',
        isFullPage: false,
      });

      //console.log(this.selectedTags[0].key +" --- " + this.selectedTags[0].value)
      api.getCodes(this.$baseURL, this.userEnteredCodes, "ENTITY")
          .then((data)=>{

            if (data != null) {
              // If a code is retired, the object may be empty.
              for (let x = data.length -1; x >=0; x--) {
                if (data[x].queryCode < 0) {
                  //console.log("Code: " + data[x].code + " is invalid: " + data[x].queryStatus)
                  tempCode =  data[x].code
                  tempStatus = data[x].queryStatus
                  data.splice(x,1)

                  // need to remove from selectedTags
                  for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
                    if (tempCode == this.selectedTags[i].value) {
                      this.selectedTags.splice(i,1)
                    }
                  }
                  // Display error message for this code
                  this.$notify({
                    group: 'app',
                    title: 'Invalid Concept Code',
                    text: '<b>' +tempCode+'</b> is not valid. Reason: ' +tempStatus+ '.',
                    type: 'error',
                    duration: 6000,
                    position: "left bottom"
                  });
                }
              }

              this.entityList = data;
              this.updateSelectedTopNodeDescription(data);
            }
            else {
              // There was a failure making the REST call.
              this.clearSelection()
              this.$notify({
                group: 'app',
                title: 'Validation Failure',
                text: 'Could not verify top node.  Possible network issue.',
                type: 'error',
                duration: 4000,
                position: "left bottom"
              });

              this.selectedTags = [];
              //this.getPropertyError=true;
            }
          }).catch(function(error) {
        console.error("Error retrieving branch: " + error);
      }).finally(function() { loader.hide()});
    },

    downloadFile() {
      this.$notify({
        group: 'download',
        title: 'Export in Progress',
        text: 'Your export is running.  Please wait.',
        type: 'success',
        duration: 2000,
        position: "bottom left"
      });

      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });

      // set the user selected tags and properties
      this.setSelectedTags()
      this.setSelectedPropertyNames()

      axios({
        url: this.$baseURL + 'download/get-file-for-resolved-branch/'  +
            this.userEnteredCodes + '/' +
            this.userSelectedProperyNames + '/' +
            this.selectedLevel + '/' +
            this.userSelectedFormat.name + '/' +
            this.filename + '.' + this.userSelectedFormat.extension,
        method: 'GET',
        responseType: 'blob',
      }).then((response) => {
        var fileURL = window.URL.createObjectURL(new Blob([response.data]));
        var fileLink = document.createElement('a');

        fileLink.href = fileURL;
        fileLink.setAttribute('download', this.filename + '.' + this.userSelectedFormat.extension);
        document.body.appendChild(fileLink);
        fileLink.click();
      }).catch(function(error) {
        console.error("Download Error: " + error);
        alert("Error Downloading file");
      }).finally(function() { loader.hide()});
    },

    async initiateDeferredDownloadAndWait() {
      this.$notify({
        group: 'download',
        title: 'Export in Progress',
        text: 'Your export is running.  Please wait.',
        type: 'success',
        duration: 2000,
        position: "bottom left"
      });
      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });

      this.gaTrackDownload();

      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes,
          this.userSelectedProperyNames, this.selectedLevel,
          this.userSelectedFormat.name)
          .then((data)=>{
            if (data != null) {
              this.deferredStatusUrl = data
              //const hashId = this.getHashFromURL(this.deferredStatusUrl)
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              this.pollForStatus(this.deferredStatusHash)
            }
            else {
              this.deferredStatusUrl = null
              console.log("Error making Deferred call");
              alert("Error downloading deferred file");
            }
          }).finally(function() { loader.hide()});
    },

    async initiateDeferredDownloadAndReturn() {
      this.$notify({
        group: 'download',
        title: 'Export ID',
        text: 'Retrieving your Export ID.',
        type: 'success',
        duration: 2000,
        position: "bottom left"
      });
      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });

      this.gaTrackDeferredDownload();

      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes,
          this.userSelectedProperyNames, this.selectedLevel,
          this.userSelectedFormat.name)
          .then((data)=>{
            if (data != null) {
              this.deferredStatusUrl = data
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              //console.log("Deferred Call made.  return: " + data);
              //console.log("Deferred Call - Hash " + this.deferredStatusHash);

              this.addHashToLocalStorage(this.deferredStatusHash)
            }
            else {
              this.deferredStatusUrl = null
              console.log("Error making Deferred call");
            }
          }).finally(function() { loader.hide()});
    },

    pollDeferredStatus: function() {
      api.pollDeferredDownloadStatus(this.$baseURL, this.deferredStatusUrl)
          .then((data)=>{
            if (data != null) {
              this.deferredStatus = data.status
            }
            else {
              this.deferredStatus = "ERROR"
            }
          }).catch(function(error) {
        this.clearDeferredData()
        console.error("Polling Deferred Status Error: " + error)
      });
    },
    

    downloadDeferredResult(hashId) {
      axios({
        url:this.$baseURL +
            'download/deferred/checkFileForHashFormatResponseEntity/'  +
            hashId + '/' +
            this.userSelectedFormat.name + '/' +
            this.filename,
        method: 'GET',
        responseType: 'blob',
      }).then((response) => {
        var fileURL = window.URL.createObjectURL(new Blob([response.data]));
        var fileLink = document.createElement('a');

        fileLink.href = fileURL;
        fileLink.setAttribute('download', this.filename + '.' + this.userSelectedFormat.extension);
        document.body.appendChild(fileLink);
        fileLink.click();
      }).catch(function(error) {
        console.error("Deferred Download Error: " + error);
        alert("Error Downloading file");
      }).finally(function() {
        //this.clearDeferredData()
      });

    },

    getRoots(){
      api.getRoots(this.$baseURL)
          .then((data)=>{
            if (data != null) {
              //console.log("got roots : " + data);
            }
            else {
              console.log("Error retrieving roots");
            }
          })
    },

    getChildren(){
      api.getChildren(this.$baseURL, this.userEnteredCodes, 1)
          .then((data)=>{
            if (data != null) {
              //console.log("got children : " + data);
            }
            else {
              console.log("Error retrieving children");
            }
          })
    },

    // Add to local storage
    addHashToLocalStorage() {
      // ensure there is a hashID
      if (!this.deferredStatusHash) {
        return;
      }

      this.saveDeferredDownloads();
    },

    // save to local storage
    saveDeferredDownloads() {
      this.$storage.set(this.deferredStatusHash,
          {
            key: this.deferredStatusHash,
            format: this.userSelectedFormat.name,
            date: new Date().toLocaleString(),
            status: "Unknown"
          },
          { ttl: 60 * 60 * 1000 })

      localStorage.name = "Cory"
    },

    clearDeferredData() {
      this.deferredStatusUrl = ''
      this.deferredStatusHash = ''
      this.deferredStatus = false
    },

    getHashFromURL(hash) {
      const startIndex = hash.lastIndexOf('/') + 1
      return hash.substring(startIndex)
    },

    sleep: function(ms) {
      return new Promise((resolve) => {
        setTimeout(resolve, ms);
      });
    }
  },
  created() {
    // scroll to the top of the page
    window.scrollTo(0,0);

    this.updateShowSummary();

    // load properties after the page is loaded.
    api.getProperties(this.$baseURL)
        .then((data)=>{this.availableProperties = data;
        })

    // get the currated tags from the server,
    // then set the input field with these
    api.getCuratedTopNodes(this.$baseURL)
        .then((data)=>{
          this.curratedTopNodes = data;
          this.setCurratedTags();
        })
  }
}

</script>

<!-- styling for the component -->
<style>

/* Summary list box formatting */
.list-group{
  max-height: 150px;
  min-height: 150px;
  overflow-y:auto;
}
/* Typeahead elements style/theme
   for tags-input... override the defaults.        */
.tags-input-typeahead-item-default {
  color: black;
  background-color: whitesmoke;
}
.tags-input-root {
  line-height: 1.0;
}
/* .form-group {
    margin-bottom: 0rem;
} */
.modal-active{
  display:block;
}
.msl-multi-select {
  /* make the multi-select take up the entire width of the container */
  width: 100%
}
.treeViewButton {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}
.btn-primary, .btn-primary:hover, .btn-primary:focus, .btn-primary:active, .btn-primary.active, .open>.dropdown-toggle.btn-primary {
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
}
.tree-display{
  padding-left: 400px;
}

.levels-label{
  padding-left: 1px;

}

.levelsDropdown{

  width: 555px;
  position: absolute;
}
.entityCodeInput{
  width: 555px;
  position: absolute;
}

.entityText{
  padding-left: 277px;
}

.entityLabel{
  padding-left: 277px;
}

ul {
  list-style: none;
  display: flex;
  align-items: center;
  gap: 7px;
  margin: 0;
  padding: 0;
}
.tag {
  background-color: rgb(0, 125, 188);
  padding: 5px;
  border-radius: 4px;
  color: white;
  white-space: nowrap;
  transition: 0.1s ease background;
}

.delete {
  color: white;
  background: none;
  outline: none;
  border: none;
  cursor: pointer;
}

.toListBox {
  background-color: rgb(0, 125, 188);
  padding: 5px;
  border-radius: 4px;
  color: white;
  white-space: nowrap;
  transition: 0.1s ease background;
}

.fromListBox {
  background-color: rgb(0, 125, 188);
  padding: 5px;
  border-radius: 4px;
  color: white;
  white-space: nowrap;
  transition: 0.1s ease background;
}

.listBoxButton{
  margin-top : 110px;
  display: block;
}

.btn-delete{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 277px;
  width: 100px;
}

.btn-next{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 300px;
  width: 158px;
}

.btn-export{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 300px;
  width: 158px;
}

.btn-back{
  background-color: rgb(0, 125, 188);
  border-color: rgb(0, 125, 188);
  color: white;
  padding: 5px;
  border-radius: 4px;
  margin-left: 277px;
  width: 158px;
}


.modalfade{
  margin-top : 110px;
  margin-left: 277px;
}



</style>

