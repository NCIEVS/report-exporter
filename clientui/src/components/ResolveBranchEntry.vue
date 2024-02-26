
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
      <div class="row justify-content-center">
        <div class="col-12 col-md-6">
          <div role="alert" class="alert alert-secondary"> This report will resolve {{ selectedLevel }} level(s) with a total of {{ this.childrenToResolveObj.childrenCount }} children. </div>
          <label for="exportRadio">Select how to export</label>
          <div class="custom-control custom-radio">
            <input type="radio" v-model="exportType" value="exportNow" id="exportNow" checked="checked" name="exportRadio" class="custom-control-input">
            <label for="exportNow" class="custom-control-label">
              "Export now"
            </label>
          </div>
          <div class="custom-control custom-radio">
            <input type="radio" v-model="exportType" value="exportDeferred" id="exportDeferred" name="exportRadio" class="custom-control-input">
            <label for="exportDeferred" class="custom-control-label">
              "Export and download later"
            </label>
          </div>
        </div>
      </div>
      <br>
      <div class="row justify-content-center">
        <div class="col-12 col-md-6">
          <div class="alert alert-light" role="alert" v-if="exportType == 'exportDeferred'  && this.deferredStatusHash != ''">
            Use this Download ID on the
            <b><router-link v-bind:to="'/report-exporter/exports'" title="Link to Downloads">Downloads page</router-link> </b>
            to retrieve your report: <b>{{ this.deferredStatusHash }} </b>
          </div>
        </div>
      </div>
    </div>
    <!--Vue 3 End-->


    <!--Vue 3 Entity Label field  Start-->
    <div class="entityLabel" id = "entityLabelId">
      <label for="tags" >Enter NCI Thesaurus concept codes</label>
    </div>
    <!--Vue 3 Entity Label field  End-->

    <!--Vue 3 Entity Text field -->
    <div class="entityText" id = "entityTextID" element-id="tag-input">
      <input placeholder="Type entity code, then click enter"
             class="entityCodeInput" v-model="newTag"
             @keyup.enter.exact="addTag1(newTag)"
             @keyup.space.exact="addTag1(newTag)"
             list="entityList">
      <br>
      <datalist id="entityList" >
        <option value="C12219:Anatomic Structure System or Substance"></option>
        <option value="C12508:Cell"></option>
        <option value="C1909:Pharmacologic Substance"></option>
        <option value="C22187:Experimental Organism Diagnosis"></option>
        <option value="C2916:Carcinoma"></option>
        <option value="C3262:Neoplasm"></option>
        <option value="C61410:Clinical Data Interchange Standards Consortium Terminology"></option>
      </datalist>
      <br>
      <br>
      <div class = "tag-input"></div>
      <ul class="tags" id = "listOfTags">
        <li><a v-for="tag in tags" :key="tag" class="tag" id="tags2">
          {{ tag }}
          <button class="delete" @click="removeTag(tags, tag, tags.length)">x</button>
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
      <br>
      <br>
      <br>
      <strong><label for="ncitLabel" class = "levels-label">NCIt Tree:</label></strong>

      <Tree
          :nodes="treeData"
          :use-checkbox="false"
          :use-icon="yes"
          @nodeExpanded="onNodeExpanded"
          @update:nodes="onUpdate"
          @nodeClick="addNodeFunction"
      />
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
                  <select multiple v-model="leftSelectedOptions" @dblclick="moveRight" class="msl-searchable-list__items" id = "selectSearchProperties">
                    <option v-for="optionLeft in availableProperties" :key="optionLeft" class="multi-select-option msl-searchable-list__item" id = "optionSearchProperties">
                      {{ optionLeft }}
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
                  <select multiple v-model="rightSelectedOptions" @dblclick="moveLeft" class="msl-searchable-list__items" id = "selectSelectedProperties">
                    <option v-for="optionRight in rightOptions" :key="optionRight" class="multi-select-option msl-searchable-list__item" id = "optionSelectedProperties">
                      {{ optionRight }}
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

    <!--Vue 3 buttons start-->
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
        <button tabindex="-1" type="button" id = "exportButton" class="btn-export" v-on:click="ExportNowOrLater()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Export </button>
      </span>
    <!--Vue 3 buttons  End-->
    <center><VueSpinner id = "waitTimeIndicator" size="40" color="blue" /></center>
    <br>
    <br>

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

              <!-- Vue3 Selection Summary List boxes Start  -->
            </div>
            <div id="collapseSummary" aria-labelledby="headingOne" data-parent="#accordion" class="collapse show">
              <div class="card-body pb-1">
                <div class="row p-1">
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">Selected Top Node and Levels
                        <span class="badge badge-secondary" id = "selectConceptCodesCount">{{Object.keys(this.tags).length}}</span></div>
                      <div class="card-body">
                        <ul class="list-group" id="selectedTagList">
                          <li v-for="tags in tags" :key="tags.key">
                            {{ this.tags }}
                          </li>
                          <li>
                            Levels to Export: {{ selectedLevel }}
                          </li>
                          <li>
                            Children to Resolve: {{ this.childrenToResolveObj.childrenCount }}
                          </li>
                        </ul>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-4">
                    <div class="card bg-light border-dark mb-3">
                      <div class="card-header">
                        Selected Properties
                        <span class="badge badge-secondary">{{Object.keys(this.rightOptions).length}}</span>
                      </div>
                      <div class="card-body">
                        <span class="list-group" id="selectedPropertyList">{{ this.rightOptions }}</span>
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

  <h4>{{ title }}</h4>

</template>





<script>


import api from '../api.js'
import axios from 'axios'
import 'vue-loading-overlay/dist/vue-loading.css'
import {ref} from "vue";
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { defineComponent } from "vue";
import {VueSpinner} from  'vue3-spinners';
//import 'src/router/index.js'
//import router2 from './router'


/*

router2.beforeEach((to, from, next) => {
  // Get the page title from the route meta data that we have defined
  // See further down below for how we setup this data
  const title = to.meta.title
// If the route has a title, set it as the page title of the document/page
  if (title) {
    document.title = title
  }
  // Continue resolving the route
  next()
})
*/
//vue 3 counter for (Select Next Option) button due to form-wizard not working
let selectNextOptionBTN_counter =  1;


export default {

  name: 'resolve-branch-entry',
  props: {
    msg: String, defineComponent,
  },
  components: {
    Tree,
    VueSpinner
  },
  metaInfo: {
    title: 'EVS Report Exporter - Branch Resolve',
  },

  mounted() {
    this.hideObjectsOnScreen();  //Vue 3 function for when page loads certain objects like buttons or text boxes will be hidden
    this.selectedExportListName = "JSON (json) JavaScript Object Notation Format"  //Vue 3 Loads default file format
    this.$refs["my-tree"].expandNode(1);
    document.getElementById("waitTimeIndicator").style.display = "none"; // Hide Wait time indicator when system loads

  },
  setup(){
    const tags = ref([]);
    const newTag = ref('') //keep up with new tag
    var removeTagIndex = 0;

    //Vue 3 Removes a tag below text box
    const removeTag = (allTags, selectedTag,  tagLength) => {
      for (let x=0; x < tagLength; x++) {
        if (selectedTag  === allTags[x])
        {
          removeTagIndex = x
        }
      }
      tags.value.splice(removeTagIndex, 1);
    };

    const onUpdate = (nodes) => {
      console.log("nodes:", nodes);
    };

    return { tags, newTag, removeTag,   onUpdate, }
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
      userEnteredCodes: [],
      entityList: [],
      availableProperties: [],
      selectedProperties: [],
      userSelectedProperyNames: [],
      userSelectedFormat: '',
      userSelectedTopNode: '',
      filename: 'branch',
      fileFormat: '',
      selectedLevel: 0,
      childrenToResolve: 0,
      tempListClear:[],
      leftSelectedOptions:[],
      rightSelectedOptions:[],
      rightOptions:[],
      treeArrayExclude: [],
      treeCodeExclude: [],

      routes: [
        {
          path: '/',
          name: 'Home',
          hasIcon: true,
        },
        {
          path: '/dashboard',
          name: 'Dashboard',
          hasIcon: true,
        },
        {
          path: '/component',
          name: 'Components',
          hasIcon: true,
          children: [
            {
              path: '/alerts',
              name: 'Alerts',
            },
            {
              path: '/avatars',
              name: 'Avatars',
            },
            {
              path: '/buttons',
              name: 'Buttons',
            },
            {
              path: '/forms',
              name: 'Forms',
              children: [
                {
                  path: '/autocompletes',
                  name: 'Autocompletes',
                },
                {
                  path: '/checkboxes',
                  name: 'Checkboxes',
                },
              ],
            },
          ],
        },
      ],

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
      deferredData: '',
      selectedExportListName: '',
      showSummary: true,
      showSummaryText: '',
      exportType: 'exportNow',
      treeData: [],
      treeCode: [],
    }
  },

  methods: {

    //Vue 3 tree functionality
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
          },
          checked: {
            state: true,
          },
          editableName: {
            state: true,
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


    myCheckedFunction: function (nodeId, state) {
      console.log(`is ${nodeId} checked ? ${state}`);
      console.log(this.$refs["my-tree"].getCheckedNodes("id"));
      console.log(this.$refs["my-tree"].getCheckedNodes("text"));
    },
    mySelectedFunction: function (nodeId, state) {
      console.log(`is ${nodeId} selected ? ${state}`);
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



    //Vue 3 Add code adds node to tree.  When node it clicked it builds the children nodes in the
    //background and places a arrow to the left of the parent node
    addNodeFunction: function (node) {

      var childDupCheck = false;
      var tagAddFlag = true;


      //Vue 3  code below calls the Rest api for the children nodes and builds the tree
      if (this.treeCode.length > 0) {  // Vue 3 checks if parent nodes (level 1 ) already exists
        api.getChildren(this.$baseURL, node.id, 1)
            .then((children) => {

              if ((children !== null) ) {   // Vue 3 Checks if rest api returns children data
                for (let x = 0; x < children.length; x++) {  // Vue  3 loops through children data
                  for (let y=0; y < this.treeArrayExclude.length; y++) {  // Vue 3 loop prevents duplicate children nodes from being created
                    if (children[x].code  === this.treeArrayExclude[y])
                    {
                      childDupCheck = true;
                    }
                  }

                  if (childDupCheck === false) {  // Vue 3 Checks for duplicate children node
                    if (children[x].leaf === true){  // Vue 3 checks if entity code is not active

                      //Vue 3 formats how children node will get stored in tree for inactive codes
                      const newNode = {
                        label: children[x].code + ' : ' + children[x].name + ' *',
                        id: children[x].code,
                        state: {checked: false, selected: true, expanded: false},
                        checkable: false,
                        nodes: []
                      };

                      //Vue 3 adds children node to tree
                      if (node.nodes === undefined) {
                        node.nodes = [newNode];
                      } else {
                        node.nodes.push(newNode);
                      }

                      this.treeCodeExclude.push(children[x].code)  //Vue 3 keep track of all of the nodes to prevent duplicates
                    }else {

                      //Vue 3 formats how children node will get stored in tree for active codes
                      const newNode = {
                        label: children[x].code + ' : ' + children[x].name,
                        id: children[x].code,
                        state: {checked: false, selected: false, expanded: false},
                        checkable: false,
                        nodes: []
                      };

                      //Vue 3 adds children node to tree
                      if (node.nodes === undefined) {
                        node.nodes = [newNode];
                      } else {
                        node.nodes.push(newNode);
                      }
                    }

                    this.treeCode.push(children[x].code);
                    this.treeArrayExclude.push(children[x].code);  //Vue 3 keep track of all of the nodes to prevent duplicates
                  }

                  //Vue 3 adds code selected from tree to a blue tag below the text box
                  if (tagAddFlag === true) {
                    this.removeAllTags2(0)
                    this.tags.push(node.label);
                    this.updateChildrenToResolve();
                    tagAddFlag = false
                  }
                }
              }
            })
      }
    },

    //Vue 3 Removes all blue tags under text box
    removeAllTags2 (tagDeleteCounter) {
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

    //Vue 3 Code registers what entity code was entered in the text box then calls a api to return the code and description combo
    //in a blue tag below the text box
    addTag1(tag) {
      var codeDescription = [];
      var dupTagCheck = false;
      var indexBottomTab = 0;
      var tempStatus = ''
      tag = tag.replace(/[\s/]/g, '')
      tag = tag.replace(',', '')  // Vue 3 removes commas if entered in the text box

      document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator

      indexBottomTab = tag.indexOf(":");
      if ((indexBottomTab > 0) && (this.tags.length <= 0)){
        this.tags.push(tag);
        this.newTag = ""; // reset newTag
        this.tagCounter = this.tagCounter + 1;
        this.newTagCounter = this.newTagCounter + 1;
        this.updateChildrenToResolve()
      }


      for (let i = 0; i < this.userEnteredCodes.length; i++) {  //Vue 3 checks for duplicate codes
        if (this.tags[i] === tag) {
          dupTagCheck = true;
        }
      }

      //Vue 3 Code registers what entity code was entered in the text box then calls a api to return the code and description combo
      //in a blue tag below the text box

      //Vue 3 checks entity code entered and returns a description if one is available
      if ((tag != "") && (this.tags.length <= 0) && (indexBottomTab <= 0)) {


        api.getCodes( this.$baseURL, tag, 'ENTITY')
            .then((data)=> {
              if ((data !== null) && (data!== undefined) && (data!== "")) {  //Vue 3 check if rest api does not return any results
                for (let x = data.length - 1; x >= 0; x--) {      //Vue 3 loop through data returned from rest api
                  if ((data[x].name.length > 0)  &&  (data[x].name != null)){

                    if (dupTagCheck === true) {
                      this.newTag = [];
                      dupTagCheck = false;
                    }else {
                      codeDescription = data[x].name;
                      this.tags.push(tag + ":" + codeDescription);  //Vue 3 adds entity code and description ex (C12219:Anatomic Structure System or Substance) in blue tag under text box
                      this.newTag = ""; // Vue 3 reset newTag
                      this.tagCounter = this.tagCounter + 1;
                      this.newTagCounter = this.newTagCounter + 1;
                      this.setSelectedTags()
                      this.updateChildrenToResolve()
                    }
                  }else{
                    tempStatus = data[x].queryStatus
                    //this.tags.push(tag + ":" + "");   //Vue 3 used for testing take out after testing
                    //this.newTag = ""                  //Vue 3 used for testing take out after testing
                    //this.tagCounter = this.tagCounter + 1;  //Vue 3 used for testing take out after testing

                    //Vue 3 error message if invalid entity code is entered
                    this.$notify({
                      group: 'app',
                      title: 'Invalid Concept Code',
                      text: '"' + tag + '" is not valid. Reason: "' + tempStatus + '".',
                      type: 'error',
                      duration: 6000,
                      position: "left bottom"
                    });
                  }
                }
              }else {
                //Vue 3 if a duplicate tag is entered in the textbox then the code will not add a new tag
                if (dupTagCheck === true) {
                  this.newTag = [];
                  dupTagCheck = false;
                }else{
                  //this.tags.push(tag + ":" + "");   //Vue 3 used for testing take out after testing
                  //this.newTag = ""                  //Vue 3 used for testing take out after testing
                  //this.tagCounter = this.tagCounter + 1;  //Vue 3 used for testing take out after testing
                  //this.setSelectedTags()              //Vue 3 used for testing take out after testing
                  //this.updateChildrenToResolve()      //Vue 3 used for testing take out after testing
                  this.$notify({
                    group: 'app',
                    title: 'Invalid Concept Code',
                    text: '"' + tag + '" is not valid. Reason: "' + tempStatus + '".',
                    type: 'error',
                    duration: 6000,
                    position: "left bottom"
                  });
                }
              }
            })
      }
      this.WaitTimeIndicatorPause()
    },

    //Vue 3 move data from right list box on second screen to left list box on second screen
    moveLeft() {
      if(!this.rightSelectedOptions.length) return;
      for(let i=this.rightSelectedOptions.length;i>0;i--) {
        let idx = this.rightOptions.indexOf(this.rightSelectedOptions[i-1]);
        this.rightOptions.splice(idx, 1);
        this.availableProperties.push(this.rightSelectedOptions[i - 1])
        this.rightSelectedOptions.pop();
      }
    },

    //Vue 3 move data from left list box on second screen to right list box on second screen
    moveRight() {
      if (!this.leftSelectedOptions.length) return;
      for (let i = this.leftSelectedOptions.length; i > 0; i--) {
        let idx = this.availableProperties.indexOf(this.leftSelectedOptions[i-1]);
        this.availableProperties.splice(idx, 1);
        this.rightOptions.push(this.leftSelectedOptions[i - 1]);
        this.tempListClear.push(this.leftSelectedOptions[i - 1]);
        this.leftSelectedOptions.pop();
      }
    },

    //Vue 3 Start second screen left list box Search Function
    searchPropertiesFilter() {
      var input;
      var formattedInput;
      var listBoxValues;
      var selectedListBoxValue;
      var i;
      var txtValue;

      input = document.getElementById("searchProperties"); //text input
      formattedInput = input.value.toUpperCase();  //change to upper case
//debugger;
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



    //Vue 3 Start second screen right list box Search Function
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
      document.getElementById("waitTimeIndicator").style.display = "none"; // Hide Wait time indicator when system loads
    },

    validateFirstStep() {

      //Vue 3 Select Next Option Counter.  This counter replaces the form-Wizard logic that is not working
      //correctly under vue 3.  If value is 1 then it implements validateFirstStep fucction.  If value is 2 then
      //it implements validatePropertyStep function.  If validateExportStep is 3 then it implements the validateExportSetup function

      //Vue 3 STEP 1
      if (selectNextOptionBTN_counter === 3) {
        document.getElementById("waitTimeIndicator").style.display = "";  //Show wait time indicator
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextButton").style.display = ""; //Hides next button
        this.WaitTimeIndicatorPause()
      }

      if (selectNextOptionBTN_counter === 2) {
        this.validatePropertyStep();
      }


      if (selectNextOptionBTN_counter === 1) {
        if ((this.tags.length > 0) && (this.selectedLevel > 0)) {  // checks to make sure that a code was entered before proceeding to next screen
          document.getElementById("waitTimeIndicator").style.display = "";  //Show wait time indicator
          document.getElementById("clearButton").style.display = "none";    //Hides clear button
          document.getElementById("entityTextID").style.display = "none";   //Hides textbox on main screen
          document.getElementById("entityLabelId").style.display = "none";  //Hides label on main screen
          document.getElementById("SelectProperties1").style.display = "";  //Shows listboxs on second screen
          document.getElementById("backButton").style.display = "";     //Shows back button
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1  // Counter controls navigating between steps 1 -3
          this.WaitTimeIndicatorPause()
        }
      }
    },

    //Vue 3 STEP 2
    validatePropertyStep() {
      // make sure the user has selected at least one property
      //Hides objects on screen that shouldn't appear in step 2

      if (this.rightOptions.length > 0) {
        document.getElementById("waitTimeIndicator").style.display = "";  //Show wait time indicator
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextOption").style.display = "none"; //Hides next option button
        this.WaitTimeIndicatorPause()

        if (Object.keys(this.rightOptions).length > 0) {
          selectNextOptionBTN_counter = selectNextOptionBTN_counter + 1;
          return Object.keys(this.rightOptions).length > 0
        }
      }
    },

    //Vue 3 Back button
    backStep(){
      //Shows screen for step 1
      if (selectNextOptionBTN_counter === 2) {
        document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
        document.getElementById("SelectProperties1").style.display = "none";  //shows listboxs on second screen
        document.getElementById("clearButton").style.display = "";    //Shows clear button
        document.getElementById("entityTextID").style.display = "";   //Shows textbox on main screen
        document.getElementById("entityLabelId").style.display = "";  //Shows label on main screen
        document.getElementById("backButton").style.display = "none"; //Hides back button on main screen
        document.getElementById("nextOption").style.display = "";     //Shows next button
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;
        this.WaitTimeIndicatorPause()
      }

      //Shows screen =for step 2
      if (selectNextOptionBTN_counter === 3) {
        document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
        document.getElementById("SelectProperties1").style.display = "";  //shows listboxs on second screen
        document.getElementById("backButton").style.display = "";     //Shows back button on main screen
        document.getElementById("exportButton").style.display = "none"; //Hides Export button
        document.getElementById("nextOption").style.display = ""; //Hides next button
        document.getElementById("exportStep").style.display = "none";  //Hides Export Step
        selectNextOptionBTN_counter = selectNextOptionBTN_counter - 1;
        this.WaitTimeIndicatorPause()
      }
    },

    //Code provides a small delay so that the spinner circle shown to the end user when processing a task could be visible
    async WaitTimeIndicatorPause () {
      setTimeout(() => {
        document.getElementById("waitTimeIndicator").style.display = "None";  //hides wait time indicator
      }, 500);
    },

    async WaitTimeIndicatorPauseDownload () {
      setTimeout(() => {
        document.getElementById("waitTimeIndicator").style.display = "None";  //hides wait time indicator
      }, 2000);
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

    //Vue 3 method generates file
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

    //Vue 3 this method is used for the download now or later functionality
    ExportNowOrLater(){
      if (this.exportType == 'exportNow') {
        // export and wait for it to complete
        this.downloadFile();
      }
      else {
        // export and get a URL to go to later
        this.initiateDeferredDownloadAndReturn();
      }
    },

    async pollForStatus(hashId) {
      // check if a polling url was returned.
      if (this.deferredStatusUrl != null && this.deferredStatusUrl.length >0) {

        // loop and wait until the status comes back as true
        while (this.deferredStatus != null &&
        this.deferredStatus != "ERROR" &&
        this.deferredStatus != 'TRUE') {
          this.pollDeferredStatus()
          await this.sleep(500);
        }
        //loader.hide()
        this.WaitTimeIndicatorPauseDownload()

        if (this.deferredStatus === "ERROR") {
          alert("Error downloading deferred file");
        }
        else {
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

    //This method is updates the Children to relsove number when a new level is selected from the dropdown
    onLevelChange() {
      this.updateChildrenToResolve()
    },


    //Method updates the Children to resolve number listed at the bottom of the screen
    updateChildrenToResolve() {
      // if the selectedTag and selectLevel have changed, set them in the
      // object and get the NEW childrenCount
      this.setSelectedTags ()
      if ((this.childrenToResolveObj.tag != this.userEnteredCodes) ||
          (this.childrenToResolveObj.selectedLevel != this.selectedLevel))
      {
        this.childrenToResolveObj.tag = this.userEnteredCodes
        this.childrenToResolveObj.selectedLevel = this.selectedLevel
        /*
        // show the busy indicator
        let loader = this.$loading.show({
          container: this.$refs.formSelectCodes,
          loader: 'dots',
          isFullPage: false,
        });
         */

        api.getChildren(this.$baseURL, this.userEnteredCodes, this.selectedLevel)
            .then((children)=>{
              if (children != null) {
                this.childrenToResolveObj.childrenCount = children.length
              }
              else {
                this.childrenToResolveObj.childrenCount = 0
              }
            }).catch(function(error) {
          console.error("Error retrieving children to resolve: " + error);
        })
        //.finally(function() { loader.hide()});
      }
    },


    // Vue 3 this method takes the code description combo ex. (C12219:Anatomic Structure System or Substance) and returns only the code ex (C12219)
    setSelectedTags () {
      var bottomTab = "";
      var indexBottomTab = 0;
      // clear the internal user codes that are entered
      this.userEnteredCodes = []
      for (let i = 0; i < Object.keys(this.tags).length; i++) {
        //  for (let i = 0; i < 1; i++) {
        // currated top nodes (from the server hava a value of "C12434:Blood")
        // so we need to strip off everything from the : to the right.
        if (this.tags[i] !== "undefined") {
          bottomTab = this.tags[i];
          indexBottomTab = bottomTab.indexOf(":");
          this.userEnteredCodes.push(bottomTab.slice(0,indexBottomTab));
        }
      }
    },


    //Method controls downloading a file
    downloadFile() {
      document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
      this.$notify({
        group: 'download',
        title: 'Export in Progress',
        text: 'Your export is running.  Please wait.',
        type: 'success',
        duration: 2000,
        position: "bottom left"
      });


      // set the user selected tags and properties
      this.setSelectedTags();

      //Vue 3 Sets default value to JSON for Select format for export dropdown on Step 3
      if (this.fileFormat === ""){
        this.userSelectedFormat = 'json';
        this.fileFormat = 'JSON';
        this.selectedExportListName = 'JSON (json) JavaScript Object Notation Format';
      }

      axios({
        url: this.$baseURL + 'download/get-file-for-resolved-branch/'  +
            this.userEnteredCodes + '/' +
            this.rightOptions + '/' +
            this.selectedLevel + '/' +
            this.fileFormat + '/' +
            this.userSelectedFormat,
        method: 'GET',
        responseType: 'blob',
      }).then((response) => {
        var fileURL = window.URL.createObjectURL(new Blob([response.data]));
        var fileLink = document.createElement('a');
        fileLink.href = fileURL;
        fileLink.setAttribute('download', this.filename + '.' + this.userSelectedFormat);
        document.body.appendChild(fileLink);
        fileLink.click();
      }).catch(function(error) {
        console.error("Download Error: " + error);
      })
      //.finally(function() { loader.hide()});
      //alert("base " + this.$baseURL)
      //alert("User Entered Codes " + this.userEnteredCodes )
      //alert("User right options " + this.rightOptions)
      //alert("select level " + this.selectedLevel)
      //alert("select format " + this.userSelectedFormat)

      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes, this.rightOptions, this.selectedLevel, this.fileFormat)
          .then((data)=>{
            if (data != null) {
              this.deferredStatusUrl = data
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              this.pollForStatus(this.deferredStatusHash)
            }
            else {
              // this.deferredStatusUrl = null
              // console.log("Error making Deferred call");
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              this.pollForStatus(this.deferredStatusHash)
            }
          })
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
      /*
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });
       */

      this.gaTrackDownload();

      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes,
          this.rightOptions, this.selectedLevel,
          this.userSelectedFormat)
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
            }
          })
      //.finally(function() { loader.hide()});
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
      /*
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });

       */
      document.getElementById("waitTimeIndicator").style.display = ""; //shows wait time indicator
      this.gaTrackDeferredDownload();
      this.setSelectedTags();

      //Vue 3 Sets default value to JSON for Select format for export dropdown on Step 3
      if (this.fileFormat === ""){
        this.userSelectedFormat = 'json';
        this.fileFormat = 'JSON';
        this.selectedExportListName = 'JSON (json) JavaScript Object Notation Format';
      }

      // alert("base URL: " + this.$baseURL);
      // alert("tags: " + this.userEnteredCodes);
      // alert("selectedPropertyName: " + this.rightOptions);
      // alert("selectedLevel " + this.selectedLevel)
      //alert("SelectedFormat: " + this.fileFormat);
      //alert("filename: " + this.filename);
      //  alert("selectedFormat Extension: " + this.userSelectedFormat.name);

      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes, this.rightOptions, this.selectedLevel, this.fileFormat)
          .then((data)=>{
            if (data != null) {
              this.deferredStatusUrl = data
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              this.addHashToLocalStorage(this.deferredStatusHash)
            }
            else {
              this.deferredStatusUrl = null
            }
          })
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
            this.fileFormat + '/' +
            this.filename,
        method: 'GET',
        responseType: 'blob',
      }).then((response) => {
        var fileURL = window.URL.createObjectURL(new Blob([response.data]));
        var fileLink = document.createElement('a');

        fileLink.href = fileURL;
        fileLink.setAttribute('download', this.filename + '.' + this.userSelectedFormat);
        document.body.appendChild(fileLink);
        fileLink.click();
      }).catch(function(error) {
        console.error("Deferred Download Error: " + error);
      }).finally(function() {
        //this.clearDeferredData()
      });
    },


    // Add to local storage
    addHashToLocalStorage() {
      if (!this.deferredStatusHash) {
        return;
      }
      this.saveDeferredDownloads();
    },

    // Vue 3 save to local storage.
    saveDeferredDownloads() {
      const fileData = { key: this.deferredStatusHash, format: this.fileFormat,  date: new Date().toLocaleString(), status: "TRUE"};  // Vue 3 Data saved on local storage
      localStorage.setItem(this.deferredStatusHash, JSON.stringify(fileData));  //Vue 3 Save data on local storage
      this.WaitTimeIndicatorPauseDownload()
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


    //Vue 3 Displays code and descriptions for the top node.  After that another method (addNodeFunction) is called to
    //display the children nodes
    getParentNode(){
      api.getRoots(this.$baseURL)  // Top node
          .then((root)=> {

            if (root != null) {
              for (let x = 0; x < root.length; x++) {

                this.treeData.push({label: root[x].code + ' : ' + root[x].name,   //Vue 3 adds code and description to tree node
                  state: { checked: false, selected: false, expanded: false },
                  id: root[x].code,
                  checkable: false,
                  nodes:[]});

                this.treeCode.push(root[x].code);     //Vue 3 this variable keeps track of what codes were entered for the tree
              }
            }
          })
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

    api.getProperties(this.$baseURL)
        .then((data)=> {
          for (let x = 0 ; x < data.length; x++) {
            this.availableProperties.push(data[x].name);
          }
        })

    this.getParentNode();  //Vue 3 builds tree on Resolved Branch Export screen
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
  padding-left: 100px;
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

.tree-display {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.modalfade{
  margin-top : 110px;
  margin-left: 277px;
}




</style>

