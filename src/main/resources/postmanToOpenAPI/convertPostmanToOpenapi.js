const converterPostmanToOpenApi = require('./helper/converter-postman-to-openapi.js')
const { promises: { writeFile, readFile } } = require('fs')
const postmanCollection = './../static/postman_collection.json'
const endFile = './../templates/openapi.json'
const environment = './../static/postman_environment.json'

/**
 * Converts postman_collection into an openapi-specification and saves it
 */
async function convertPostmanCollectionToOpenApi(){
    try {
        let result = await converterPostmanToOpenApi(postmanCollection, environment)
        await write(endFile, result)
    }catch (err) {
        console.log(err)
    }
}
/**
 * Saves data to file 'output'
 * @param {String} output
 * @param {object} data
 */
async function write(output, data){
    let json = JSON.stringify(data, null, 2);
    await writeFile(output, json, (err) => {
        if (err) throw err;
    });
}
convertPostmanCollectionToOpenApi()
