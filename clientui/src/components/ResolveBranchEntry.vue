
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
            <b><router-link v-bind:to="'/exports'" title="Link to Downloads">Downloads page</router-link> </b>
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


      <br>
      <Tree
          id="my-tree-id"
          ref="my-tree"
          :custom-options="myCustomOptions"
          :custom-styles="myCustomStyles"
          :nodes="treeDisplayData"
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
        <button tabindex="-1" type="button" id = "exportButton" class="btn-export" v-on:click="ExportNowOrLater()"  style="background-color: rgb(1, 126, 190); border-color: rgb(1, 126, 190); color: white;"> Export </button>
      </span>


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
import {ref} from "vue";
import Tree from "vue3-tree";
import "vue3-tree/dist/style.css";
import { defineComponent } from "vue";
//import Tree from "vuejs-tree"




//vue 3 counter for (Select Next Option) button due to form-wizard not working
let selectNextOptionBTN_counter =  1;


export default {
  name: 'resolve-branch-entry',
  props: {
    msg: String, defineComponent,
  },
  components: {
    Tree
    // ExportFormat
  },
  metaInfo: {
    title: 'EVS Report Exporter - Branch Resolve',
  },

  mounted() {
    this.hideObjectsOnScreen();  //Vue 3 function for when page loads certain objects like buttons or text boxes will be hidden
    this.selectedExportListName = "JSON (json) JavaScript Object Notation Format"  //Vue 3 Loads default file format
    this.$refs["my-tree"].expandNode(1);

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
      curratedTopNodes: [],
      userSelectedTopNode: '',
      filename: 'branch',
      fileFormat: '',
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
      tmpData: '',
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
      deferredDataFormatted: '',
      showTree: true,
      asyncData: [],
      selectedExportListName: '',
      treeSelectedCode: null,
      showSummary: true,
      showSummaryText: '',
      deferredDownloadData: '',
      exportType: 'exportNow',
      treeLevel: '',

      // treeData: [ {
      //   id: 0,
      //   label: '(NCIt Tree):'}],

      treeData: [],
      treeDisplayData: [],
      treeCode: [],
      treeChildNode: [],
      levelCounter: 1,
      /*
         treeDisplayData: [
           {
             text: this.treeLevel = "level 1",
             state: {checked: false, selected: false, expanded: true},
             id: 1,
             checkable: true,
             nodes: [
               {
                 text: "Level 2a",
                 state: {checked: false, selected: true, expanded: false},
                 id: 2,
                 checkable: true,
                 nodes: [           {
                   text: "level 2b",
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
             checkable: false,
             nodes: [],
           },
           {
             text: "level 3",
             state: {checked: false, selected: true, expanded: false},
             checkable: false,
             id: 5,
             nodes: [],

           },
           {
             text: "Child 2",
             state: { checked: false, selected: true, expanded: false },
             checkable: false,
             id: 4,
           },
         ] ,


         */

      /*
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

                                "ida": children[x].code,
                                "labelb": children[x].code + ' : ' + children[x].name,
                               // "checkable": false,
                               // "state": children[x].leaf,
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
      */
    }
  },

  methods: {



    loadData: function (oriNode, resolve) {
      // set id to the node to retrieve children for.
      // set to null to indicate this is the root.
      alert(oriNode);
      alert(resolve);
      var id = oriNode.data.id ? oriNode.data.id : null
      var data = []
      //console.log('id: ' + id)

      // if id is null, this is the root.  get all root children
      if (id == null) {
        api.getRoots(this.$baseURL)
            .then((children)=>{
              if (children != null) {

                for (let x=0; x < children.length; x++){
                  //console.log(children[x].code + '  :  ' + children[x].name)
                  data.push(
                      {
                        "id": children[x].code,
                        "text": children[x].code + ' : ' + children[x].name,
                        "isLeaf": false,
                        "disabled": children[x].leaf,
                      },
                  )
                }
                resolve(data)
              }
              else {
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
            .then((children)=>{
              if (children != null) {
                for (let x=0; x < children.length; x++){
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
              }
              else {
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









































    // Vue 3 when tree is click the code is added to the blue tags below the entity code search
/*
    onNodeClick(node) {

     // var tmpData;
      if ((node.label !== "(NCIt Tree):") && (this.tags.length < 1)) {
        this.tags.push(node.label)
      }

    },
*/










/*
    onNodeClick(node) {
      // node.label = Object.values(node.label).replace(',', '');
      alert("This node was selected: " + Object.values(node.label));
      alert("before api call ");
      api.getCuratedTopNodes(this.$baseURL)
          .then((data)=>{
            // this.curratedTopNodes = data;
            // this.setCurratedTags();
            alert("this is the data "+ Object.values(data));
          })
      alert("after api call  ");
    },
*/





    checkRoutes(){
      alert("routes Check" + this.routes[1].length);
    },

    getNodeValue(value){
      //this.treeLevel = "TEST"
      alert("this is the function value " + value);
      var test1;
      test1 = document.getElementById('this.my_tree_id').value
      alert("this is the tree level: " + this.treeDisplayData[0].selected);
      alert("This is the selected node " );
      alert("This is the element " + test1);
      alert(this.$refs["my-tree"].getSelectedNode());
      this.myCustomOptions();
      this.myCheckedFunction();


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


    /*
    myCheckedFunction (nodeId, disabled) {
      alert("myCHeckedFunction");
      alert("this is the node id " + nodeId);
      alert("this is disabled " + disabled);
      this.loadData(nodeId, disabled);
      alert(`is ${nodeId} checked ? ${disabled}`);
      alert(this.$refs["my-tree"].getCheckedNodes("id"));
      alert(this.$refs["my-tree"].getCheckedNodes("text"));
    },


    mySelectedFunction: function (nodeId, disabled) {
      alert("mySelectedFunction invoked")
      this.loadData(nodeId, disabled);
      alert(`is ${nodeId} selected ? ${disabled}`);
      alert(this.$refs["my-tree"].getSelectedNode());
    },

    functiontest2()
    {
      alert("second function");
    },


     */





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




    // addNodeFunction: function (node, childCode, text) {

    addNodeFunction: function (node) {
      //  console.log(childCode);
      //  console.log(text);
      //  console.log(leafCheck);
      //console.log(newNodeInd);


    //  var tmpnodeArray;
      var childDupCheck = false;
      var tagAddFlag = true;

   //   this.treeCode.push({treeid: root[x].code, treeIndex: x});
   //   this.treeCode.push({id: root[x].code, treeIndex: x});
//this.treeCode.push(root[x].code);




/*

      for (let z=0; z < this.treeCode.length; z++) {

        if (node.id  === this.treeCode[z])
        {
        //  alert("node Position " + z)
          tmpnodeArray = z
        }
      }
*/
//alert("treeCode Length " + this.treeCode.length)
      if (this.treeCode.length > 0) {
        //  for (let y = 0; y < this.treeCode.length; y++) {
        api.getChildren(this.$baseURL, node.id, 1)
            .then((children) => {

             //  alert("Children " + children.length);


            //  if ((children !== null) && (childDupCheck === false)) {



                if ((children !== null) ) {
                for (let x = 0; x < children.length; x++) {


                  for (let y=0; y < this.treeArrayExclude.length; y++) {
                    if (children[x].code  === this.treeArrayExclude[y])
                    {
                       childDupCheck = true;
                    //  alert("true")
                    }
                  }
               //   alert("Node: " + children[x].code)
               //   alert("leaf Value: " + children[x].leaf);


                //
               //   console.log("selected node " + this.treeData[tmpnodeArray].value)
               //   console.log("code label " + children[x].code + ' : ' + children[x].name)
               //   console.log("childDupCheck " + childDupCheck)
                  if (childDupCheck === false) {
                  //  alert("code " + children[x].code)

                  //  alert("tree code length " + this.treeCode)
                  //  alert("tree data length " + Object.values(this.treeData))

                  //  alert("node Location " + this.treeData[tmpnodeArray].nodes)
/*
                    this.treeData[tmpnodeArray].nodes.push({
                      label: children[x].code + ' : ' + children[x].name,
                      id: children[x].code,
                      state: { checked: false, selected: true, expanded: false },
                      checkable: false,
                      nodes: []
                    })
*/
                // console.log("tmpnodeArray " + tmpnodeArray)
                 //  alert("tree length " + this.treeData.node.nodes)
                 //   alert("node Location " + Object.values(this.treeData[tmpnodeArray].nodes))





       /*
                    for (let y=0; y < this.treeCodeExclude.length; y++) {

                      if (node.id  !== this.treeCodeExclude[y])
                      {
                        tagAddFlag = false;

                      }
                    }
*/

                    //   if (tagAddFlag === true) {
                   // this.tags.push(node.label);

                    //   }

                    if (children[x].leaf === true){

                      const newNode = {
                        label: children[x].code + ' : ' + children[x].name + ' *',
                        id: children[x].code,
                        state: {checked: false, selected: true, expanded: false},
                        checkable: false,
                        nodes: []
                      };

                      if (node.nodes === undefined) {
                        node.nodes = [newNode];
                      } else {
                        node.nodes.push(newNode);
                      }

                      this.treeCodeExclude.push(children[x].code)
                    }else {


                      const newNode = {
                        label: children[x].code + ' : ' + children[x].name,
                        id: children[x].code,
                        state: {checked: false, selected: false, expanded: false},
                        checkable: false,
                        nodes: []
                      };

                      if (node.nodes === undefined) {
                        node.nodes = [newNode];
                      } else {
                        node.nodes.push(newNode);
                      }




                    }



                      /*

                    this.treeData[tmpnodeArray].nodes.push({
                      label: children[x].code + ' : ' + children[x].name,
                      id: children[x].code,
                      state: { checked: false, selected: true, expanded: false },
                      isLeaf: children[x].leaf,
                      checkable: false,
                      nodes: []
                    })
*/

                      /*
                    this.treeData[tmpnodeArray].nodes.push({
                      label: children[x].code + ' : ' + children[x].name,
                      id: children[x].code,
                      state: { checked: false, selected: true, expanded: false },
                      isLeaf: children[x].leaf,
                      checkable: false,
                      nodes: []
                    })

 */

                      /*
                    this.treeData.push({label: children[x].code + ' : ' + children[x].name,
                      state: { checked: false, selected: true, expanded: false },
                      id: children[x].code,
                      checkable: false,
                      nodes:[]});
*/
                      this.treeCode.push(children[x].code);
                      this.treeArrayExclude.push(children[x].code);






                  }
                  if (tagAddFlag === true) {
                    this.removeAllTags2(0)
                    this.tags.push(node.label);
                    this.updateChildrenToResolve();
                    tagAddFlag = false
                  }
                }
              }
            })
        //  }


      }
    },







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

    addTag1(tag) {
      var codeDescription = [];
      //var baseLink = document.getElementById('basURL').value;
      //    var this.tagCounter = 0;
      //    var this.newTagCounter = 0;
      var dupTagCheck = false;
      //const tags = ref([]);
      //const newTag = ref('') //keep up with new tag

      var indexBottomTab = 0;
      tag = tag.replace(/[\s/]/g, '')
      tag = tag.replace(',', '')


      indexBottomTab = tag.indexOf(":");
      if ((indexBottomTab > 0) && (this.tags.length <= 0)){
        this.tags.push(tag);
        this.newTag = ""; // reset newTag
        this.tagCounter = this.tagCounter + 1;
        this.newTagCounter = this.newTagCounter + 1;
        this.updateChildrenToResolve()
      }


  //    this.setSelectedTags()
  //    alert("index check " + indexBottomTab )
//alert("Entity list dropdown " + tag)
 //     alert("Entity list dropdown formatted " + this.userEnteredCodes)


      for (let i = 0; i < this.userEnteredCodes.length; i++) {  //Vue 3 checks for duplicate codes
        if (this.tags[i] === tag) {
          dupTagCheck = true;
        }
      }
      //Vue 3 checks entity code entered and returns a description if on is available
      if ((tag != "") && (this.tags.length <= 0) && (indexBottomTab <= 0)) {
   //     alert("test")
        api.getCodes( this.$baseURL, tag, 'ENTITY')
            .then((data)=> {
              if ((data !== null) && (data!== undefined) && (data!== "")) {
                for (let x = data.length - 1; x >= 0; x--) {
                  //  alert(data[x].name);
                  //  if ((data[x].name != null) && (data[x].name!== undefined)  && (data[x].length < 1)) {
                  //  if ((data[x].name.length > 0) && (data[x].name!== undefined)) {
                  if ((data[x].name.length > 0)  &&  (data[x].name != null)){

                    //  alert("Code: " + data[x].code + " is invalid: " + data[x].queryStatus + " roles: " + data[x].roles + " association: " + data[x].associations + " Description: " + data[x].name);
                    if (dupTagCheck === true) {
                      this.newTag = [];
                      dupTagCheck = false;
                    }else {
                      codeDescription = data[x].name;
                      this.tags.push(tag + ":" + codeDescription);
                      this.newTag = ""; // reset newTag
                      this.tagCounter = this.tagCounter + 1;
                      this.newTagCounter = this.newTagCounter + 1;
                      this.setSelectedTags()
                      this.updateChildrenToResolve()
                    }
                  }else{
                    this.tags.push(tag + ":" + "");   //take out after testing
                    this.newTag = ""                  //take out after testing
                    this.tagCounter = this.tagCounter + 1;  //take out after testing
                    this.$notify({
                      group: 'app',
                      title: 'Validation Failure',
                      text: 'Could not verify concept code(s).  Possible network issue.',
                      type: 'error',
                      duration: 4000,
                      position: "left bottom"
                    });
                  }
                }
              }else {
                if (dupTagCheck === true) {
                  this.newTag = [];
                  dupTagCheck = false;
                }else{
                  this.tags.push(tag + ":" + "");   //take out after testing
                  this.newTag = ""                  //take out after testing
                  this.tagCounter = this.tagCounter + 1;  //take out after testing
                  this.setSelectedTags()              //take out after testing
                  this.updateChildrenToResolve()      //take out after testing
                  this.$notify({
                    group: 'app',
                    title: 'Validation Failure',
                    text: 'Could not verify concept code(s).  Possible network issue.',
                    type: 'error',
                    duration: 4000,
                    position: "left bottom"
                  });
                }
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
        document.getElementById("exportStep").style.display = "";  //Show Export dropdown
        document.getElementById("SelectProperties1").style.display = "none";  //Hide list boxes from step 2
        document.getElementById("exportButton").style.display = ""; //Show Export button
        document.getElementById("nextButton").style.display = ""; //Hides next button
      }

      if (selectNextOptionBTN_counter === 2) {
        this.validatePropertyStep();
      }


      //  alert(selectNextOptionBTN_counter);
      //  alert("test c");
      //  alert(Object.keys(this.selectedTags).length);

      if (selectNextOptionBTN_counter === 1) {
        if ((this.tags.length > 0) && (this.selectedLevel > 0)) {  // checks to make sure that a code was entered before proceeding to next screen
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

         /*

          api.getProperties(this.$baseURL)
              .then((data) => {
                for (let x = 0; x < data.length; x++) {
                  this.availableProperties.push(data[x].name);
                }
              })


          for (let i = 0; i <= this.rightUsers.length + 2; i++) {
            this.rightUsers.pop();
          }
*/

          //}
        }
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

        /*
        if(!this.rightUsers.length) return;
        for(let i=this.rightUsers.length;i>0;i--) {
          let idx = this.rightUsers.indexOf(this.rightSelectedUsers[i-1]);
          this.rightUsers.splice(idx, 1);
          this.availableProperties.push(this.tempListClear[i - 1])
          this.rightSelectedUsers.pop();
          this.tempListClear.pop();
        }
        this.availableProperties.sort();
        */

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

    /*
    setSelectedPropertyNames() {
      this.userSelectedProperyNames = []
      for (let i = 0; i < this.rightUsers.length; i++) {
        this.userSelectedProperyNames.push(this.rightUsers[i].name)
      }
    },
*/


    //Vue 3 Function controls Select format Export dropdown on Step 3
    changeSelectedExportList(event){
     // alert("ChangeDropDrownTest " + event.target.value)
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
     // this.setSelectedPropertyNames()

      /*
      if (this.exportType == 'exportNow') {
        // export and wait for it to complete
        this.initiateDeferredDownloadAndWait()
      }
      else {
        // export and get a URL to go to later
        this.initiateDeferredDownloadAndReturn()
      }

       */
    },

    async pollForStatus(hashId) {

      /*
      // show the busy indicator
      let loader = this.$loading.show({
        container: this.$refs.formContainer,
        loader: 'dots',
        isFullPage: false,
      });

       */

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

    /*
    setSelectedTags() {
      // clear the internal user codes that are entered
      this.userEnteredCodes = []

      for (let i = 0; i < Object.keys(this.selectedTags).length; i++) {
        // currated top nodes (from the server hava a value of "C12434:Blood")
        // so we need to strip off everything from the : to the right.
        this.userEnteredCodes.push(this.selectedTags[i].value.split(":",1))
      }
    },
*/

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


      // set the user selected tags and properties
      this.setSelectedTags();
     // this.setSelectedPropertyNames();

      //Vue 3 Sets default value to JSON for Select format for export dropdown on Step 3
      if (this.fileFormat === ""){
        this.userSelectedFormat = 'json';
        this.fileFormat = 'JSON';
        this.selectedExportListName = 'JSON (json) JavaScript Object Notation Format';
      }

      axios({
        url: this.$baseURL + 'download/get-file-for-resolved-branch/'  +
            this.userEnteredCodes + '/' +
            this.rightUsers + '/' +
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
        alert("Error Downloading file");
      })
      //.finally(function() { loader.hide()});
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
          this.rightUsers, this.selectedLevel,
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
              alert("Error downloading deferred file");
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

      this.gaTrackDeferredDownload();
      this.setSelectedTags();
     // this.setSelectedPropertyNames();

      //Vue 3 Sets default value to JSON for Select format for export dropdown on Step 3
      if (this.fileFormat === ""){
        this.userSelectedFormat = 'json';
        this.fileFormat = 'JSON';
        this.selectedExportListName = 'JSON (json) JavaScript Object Notation Format';
      }
      // alert("check 2")
      // alert("base URL: " + this.$baseURL);
      // alert("tags: " + this.userEnteredCodes);
      // alert("selectedPropertyName: " + this.rightUsers);
      // alert("selectedLevel " + this.selectedLevel)
      //alert("SelectedFormat: " + this.fileFormat);
      //alert("filename: " + this.filename);
      //  alert("selectedFormat Extension: " + this.userSelectedFormat.name);



      api.initiateDeferredDownload(this.$baseURL, this.userEnteredCodes,
          this.rightUsers, this.selectedLevel,
          this.fileFormat)
          .then((data)=>{
            if (data != null) {
              this.deferredStatusUrl = data
              this.deferredStatusHash = this.getHashFromURL(this.deferredStatusUrl)
              this.addHashToLocalStorage(this.deferredStatusHash)
            }
            else {
              alert("test5");
              this.deferredStatusUrl = null
              alert("Error making Deferred call");
            }
          })
    },

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

      if (!this.deferredStatusHash) {
        return;
      }

      this.saveDeferredDownloads();
    },

    async sleepTimer() {
      // await this.sleep(500);
      await this.sleep(500);
    },

    // save to local storage
    saveDeferredDownloads() {

      const fileData = { key: this.deferredStatusHash, format: this.fileFormat,  date: new Date().toLocaleString(), status: "TRUE"};  // Vue 3 Data saved on local storage
      localStorage.setItem(this.deferredStatusHash, JSON.stringify(fileData));  //Vue 3 Save data on local storage

      localStorage.name = "Cory"
    },


    getFormat(key) {
      var data = this.getData(key)
      if (data) {
        return data.value.format
      }
      else {
        return "Unknown"
      }
    },
    getTimestamp(key) {
      var data = this.getData(key)
      if (data) {
        return data.value.date
      }
      else {
        return "Unknown"
      }
    },
    getLocalStatus(key) {
      var data = this.getData(key)
      if (data) {
        return data.value.status
      }
      else {
        return "Unknown"
      }
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

    addNodeFunction1 (node, childCode, text) {
      // alert("1")


      console.log("this is the node " + node)
      console.log("this is the childCode " + childCode)
      console.log("this is the text " + text)
      const newNode = {
        text: text,
        state: { checked: false, selected: false, expanded: false },
        id: childCode,
      };
      console.log("node check " + this.treeDisplayData.nodes)
      console.log("2")


      //this.treeDisplayData.nodes.push({text: 12345, state: { checked: false, selected: false, expanded: false }, id: 33, checkable: false},);


      //node.nodes.push(newNode);

      // node.nodes.push({text: 12345, state: { checked: false, selected: false, expanded: false }, id: 33, checkable: false},);





      console.log("3")
      console.log("example: add node", newNode);
      console.log("node.nodes check " + this.nodes)
      if (node.nodes === undefined) {
        console.log("true")
        this.treeDisplayData.nodes = [newNode];
        console.log(this.treeDisplayData.nodes);
      } else {
        console.log("false")
        node.nodes.push(newNode);
      }
      console.log("4")
    },

    getParentNode(){
    //  var treeIndexCounter = 0
      //Vue 3 Displays code and descriptions for top node
      api.getRoots(this.$baseURL)  // Top node
          .then((root)=> {
            // alert("root results " + root)
            if (root != null) {
              for (let x = 0; x < root.length; x++) {

                this.treeData.push({label: root[x].code + ' : ' + root[x].name,
                  state: { checked: false, selected: false, expanded: false },
                  id: root[x].code,
                  checkable: false,
                  nodes:[]});

                this.treeCode.push(root[x].code);
               // treeIndexCounter = treeIndexCounter  1
                // this.treeCode[x] = root[x].code;

                //  alert("root results 2 " + this.treeCode[x])
                //  alert(root[x].code)
                //   alert(x)
                //  alert(this.treeCode[x])
                //   this.treeCode.value.push(root[x].code);
                // nodes: [{id: children[x].code, label: children[x].code + ' : ' + children[x].name + Object.values(treeTemp)}]};

              }
              //  alert("tree length " + this.treeCode.length)
              //if (this.treeCode.length > 0) {
              //          if (this.treeCode.length === root.length) {
              //          this.getChildNode();
              //        }
            }
          })
    },


    getChildNode(){

      // alert("second tree length a" + this.treeCode.length)
      if (this.treeCode.length > 0) {
        // alert("second tree length  b" + this.treeCode.length)
        for (let y = 0; y < this.treeCode.length; y++) {
          // alert("tree length " + this.treeCode.length)
          // alert("tree code " + this.treeCode[x])
          api.getChildren(this.$baseURL, this.treeCode[y], this.levelCounter)
              .then((children) => {
                //  childCount = children.length;

                if (children != null) {
                  for (let x = 0; x < children.length; x++) {
                    //   alert("Parent node " + this.treeCode[y]);
                    //   alert("child code " + children[x].code);
                    //   alert("child code1 " + children[0].code);
                    //   alert("child code2 " + children[1].code);
                    //   alert("child code3 " + children[2].code);
                    //    alert("child Length " + children.length)


                    //  alert(children[x].code + '  :  ' + children[x].name)
                    //   console.log("test33432  " + children[x].code)

                    //   alert("Parent node " + this.treeCode[x]);
                    //    alert("child code " + children[x].code);
                    //  alert("description "+ children[x].name);
                    //         alert("leaf " + children[x].leaf);
                    // console.log("Parent code " + this.treeCode[y]);
                    // console.log("child code " + children[x].code);
                    // console.log("leaf " + children[x].leaf);
                    // console.log("description " + children[x].name);

                    //    this.treeChildNode.push(children[x].code);
                    this.addNodeFunction(this.treeCode[y], children[x].code, children[x].code + ' : ' + children[x].name)
                    // alert(this.treeChildNode);
                    // alert(this.treeChildNode.length

                    //tree counter

                    //   if (this.treeCode.length > 0  && y === this.treeCode.length-1) {
                    //      this.levelCounter = this.levelCounter + 1;
                    //  this.treeCode = [];
                    //   this.treeCode = this.treeChildNode;
                    //  alert(this.treeCode.length);
                    //  alert(this.treeChildNode);
                    //  this.getChildNode();
                    //  }



                  }
                  // this.getMoreChildren(this.treeChildNode);


                }
                //this.treeCode = [];
                //this.treeCode = this.treeChildNode;
                //  alert(this.treeCode.length);
              })
//alert(y)
          //     alert(this.treeCode.length)

        }
      }



    },






    getMoreChildren(treeChildNodeParam){

      //  alert(this.levelCounter)
      if (treeChildNodeParam.length > 0){
        this.treeChildNod =[];
        for (let y = 0; y < treeChildNodeParam.length; y++) {
          // alert("tree length " + this.treeCode.length)
          // alert("tree code " + this.treeCode[x])

          api.getChildren(this.$baseURL, treeChildNodeParam[y], this.levelCounter)
              .then((children) => {
                //  childCount = children.length;
                //alert("child output ")
                //this.sleepTimer();


                if (children != null) {
                  for (let x = 0; x < children.length; x++) {
                    //   alert("Parent node " + treeChildNodeParam[y]);
                    //    alert("child code " + children[x].code);

                    //       alert("child code1 " + children[0].code);

                    //      alert("child code2 " + children[1].code);

                    //      alert("child code3 " + children[2].code);
                    //    alert("child Length " + children.length)
                    //DO call by node

                    //  alert(children[x].code + '  :  ' + children[x].name)
                    //   console.log("test33432  " + children[x].code)

                    //   alert("Parent node " + this.treeCode[x]);
                    //    alert("child code " + children[x].code);
                    //  alert("description "+ children[x].name);
                    //         alert("leaf " + children[x].leaf);
                    //  console.log("Parent code " + treeChildNodeParam[y]);
                    //  console.log("child code " + children[x].code);
                    //  console.log("leaf " + children[x].leaf);
                    //  console.log("description " + children[x].name);
                    this.treeChildNode.push(children[x].code);
                    this.addNodeFunction(treeChildNodeParam[y], children[x].code, children[x].code + ' : ' + children[x].name)

                  }
                }
              })

          //alert(this.treeChildNode.length)

        }
      }
      this.levelCounter = this.levelCounter + 1;
      //alert(this.treeChildNode.length)
      // this.getMoreChildren(this.treeChildNode)

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
    // api.getProperties(this.$baseURL)
    //     .then((data)=>{this.availableProperties = data;
    //     })
    api.getProperties(this.$baseURL)
        .then((data)=> {
          for (let x = 0 ; x < data.length; x++) {
            this.availableProperties.push(data[x].name);
          }
        })
    // get the currated tags from the server,
    // then set the input field with these
    api.getCuratedTopNodes(this.$baseURL)
        .then((data)=>{
          this.curratedTopNodes = data;
          this.setCurratedTags();
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





