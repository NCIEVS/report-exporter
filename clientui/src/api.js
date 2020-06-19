import axios from 'axios';

const api = {
    
    
	getCodes(baseUrl, codes){
        return new Promise((resolve)=>{
            axios.get(baseUrl + '/codereadrest/' + codes)
            .then((response) =>{
            resolve(response.data);
            })   
        })
    },
    
    getProperties(baseUrl){
        return new Promise((resolve)=>{
            axios.get(baseUrl + '/properties')
            .then((response) =>{
            resolve(response.data);
            })   
        })
    },

     getFormats(baseUrl){
        return new Promise((resolve)=>{
            axios.get(baseUrl + '/download/output-formats')
            .then((response) =>{
            resolve(response.data);
            })   
        })
    },
    
}
export default api