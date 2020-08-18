import axios from 'axios';

const api = {


	getCodes(baseUrl, codes){
      return new Promise((resolve)=>{
          axios.get(baseUrl + 'codereadrest/' + codes)
              .then((response) =>{
                  resolve(response.data);
              })
              .catch(error => {
                  console.log("OOOPS: " + error);
                  resolve(null)
              })
      })
    },

    getProperties(baseUrl){
        return new Promise((resolve)=>{
            axios.get(baseUrl + 'properties')
            .then((response) =>{
            resolve(response.data);
            })
        })
    },

    getFormats(baseUrl){
        return new Promise((resolve)=>{
            axios.get(baseUrl + 'download/output-formats')
            .then((response) =>{
            resolve(response.data);
            })
        })
    },

    getCuratedTopNodes(baseUrl){
        return new Promise((resolve)=>{
            axios.get(baseUrl + 'curated-top-nodes')
            .then((response) =>{
            resolve(response.data);
            })
        })
    },

    getRoots(baseUrl){
        return new Promise((resolve)=>{
            axios.get(baseUrl + 'roots/')
                .then((response) =>{
                    resolve(response.data);
                })
                .catch(error => {
                    console.log("Error getting roots: " + error);
                    resolve(null)
                })
        })
    },

    getChildren(baseUrl, code){
        return new Promise((resolve)=>{
            // hard code to 1 for single level resolution only
            axios.get(baseUrl + 'resolve-branch-for-codes/' + code + '/1'
          )
                .then((response) =>{
                    resolve(response.data);
                })
                .catch(error => {
                    console.log("OOOPS: " + error);
                    resolve(null)
                })
        })
    },

}
export default api
