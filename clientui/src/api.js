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

    getChildren(baseUrl, code, levels){
        return new Promise((resolve)=>{
            // hard code to 1 for single level resolution only
            axios.get(baseUrl + 'resolve-branch-for-codes/' + code + '/' + levels
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

		initiateDeferredDownload(baseUrl, userEnteredCodes,
			userSelectedProperyNames, selectedLevel, userSelectedFormatName){
        return new Promise((resolve)=>{
						console.log("URL: " + baseUrl + 'download/deferred/getURLHashForDeferredResult/' +
							userEnteredCodes + '/' + userSelectedProperyNames + '/' +
							selectedLevel + '/' + userSelectedFormatName)

            axios.get(baseUrl + 'download/deferred/getURLHashForDeferredResult/' +
							userEnteredCodes + '/' + userSelectedProperyNames + '/' +
							selectedLevel + '/' + userSelectedFormatName)
                .then((response) =>{
                    resolve(response.data);
                })
                .catch(error => {
                    console.log("Error initiating Deferred Download: " + error);
                    resolve(null)
                })
        })
    },

		pollDeferredDownloadStatus(baseUrl, pollUrl){
			return new Promise((resolve)=>{
				//console.log("Polling URL: " + baseUrl + 'download/' + pollUrl)
				axios.get(baseUrl + 'download/' + pollUrl)
						.then((response) =>{
								resolve(response.data);
						})
						.catch(error => {
								console.log("Error polling Deferred Download: " + error);
								resolve(null)
						})
				})
			},

			async pollDeferredDownloadStatus1(baseUrl, id){
				return new Promise((resolve)=>{
					//console.log("Polling URL: " + baseUrl + 'download/deferred/checkURLHashForDeferredStatus/' + id)
					axios.get(baseUrl + 'download/deferred/checkURLHashForDeferredStatus/' + id)
							.then((response) =>{
									resolve(response.data);
							})
							.catch(error => {
									console.log("Error polling Deferred Download: " + error);
									resolve(null)
							})
					})
				},
}
export default api
