"use strict";
const {
    promises: {writeFile, readFile}
} = require("fs");

/**
 * Converts postman_collection into an openapi-specification
 * @param {String} input input-file-name
 * @param {object} environments list of url environments
 * @returns {boolean}
 */
async function converter(input, environments) {
    const collectionFile = await readFile(input);

    const environmentsFile = await readFile(environments);
    const json = await changeJsonPostmanStructur(collectionFile);
    const openapi = await convertPostmanToOpenapi(json, environmentsFile);
    try {
        return openapi
    } catch (err) {
        console.log(err);
        return false;
    }
}

/**
 * Changes folder-structer, as OpenAPI only has tags and can't detect subfolders
 * Changed postman_json only has folders on the highest level
 * @param {object} collectionFile original postman_collection
 * @returns {object} converted postman_collection
 */
async function changeJsonPostmanStructur(collectionFile) {
    const postmanJson = JSON.parse(collectionFile);
    const {item: items} = postmanJson;
    const newJson = {};
    const allFolderNames = [];
    newJson["info"] = postmanJson.info;
    newJson["item"] = [];
    newJson["protocolProfileBehavior"] = postmanJson.protocolProfileBehavior;
    for (let [index, element] of items.entries()) {
        const {item, name, description: tagDesc} = element;
        if (item == null) {
            //element is not a folder
            newJson.item.push(element);
        }
        if (item != null) {
            await recursiveSubfolder(
                newJson,
                element.name,
                element,
                allFolderNames
            );
        }
    }
    removeEmptyFolders(newJson);
    return newJson;
}

/**
 * Recursive call, until a folder has no more subfolders
 * Adds a subfolder as a folder with a new name on the hightest level to newJson
 * @param {obejct} newJson changed postman_collection
 * @param {String} folderName name of the parent folder
 * @param {object} element whole folder content with its 'name', 'item', 'protocolProfileBehavior' and if existing a boolean '_postman_isSubFolder'
 * @param {object} allFolderNames list with all folder names in newJson
 */
async function recursiveSubfolder(
    newJson,
    folderName,
    element,
    allFolderNames
) {
    createFolder(
        newJson,
        folderName,
        element.protocolProfileBehavior,
        allFolderNames,
        element.description,
    );
    const items = element.item;
    for (let [index, newElement] of items.entries()) {
        const {item, name, description: tagDesc} = newElement;
        if (item == null) {
            //newElement doesn't have a subfolder
            insertItems(newJson, folderName, newElement, allFolderNames);
            continue;
        }
        if (item != null) {
            const newFolderName = folderName + "." + newElement.name;
            await recursiveSubfolder(
                newJson,
                newFolderName,
                newElement,
                allFolderNames
            );
        }
    }
}

/**
 * Adds API-Request to the folder with the given name in newJson
 * @param {object} newJson changed postman_collection
 * @param {String} name foldername to which the API-Request are added to in newJson
 * @param {object} element API-Request
 * @param {object} allFolderNames list with all folder names in newJson
 */
function insertItems(newJson, name, element, allFolderNames) {
    const itemName = name;
    for (let nameIndex in allFolderNames) {
        if (allFolderNames[nameIndex] != itemName) {
            continue;
        } else {
            for (let index in newJson.item) {
                if (newJson.item[index].name == itemName) {
                    newJson.item[index].item.push(element);
                    break;
                }
            }
        }
    }
}

/**
 * Creates new folder in newJson with the given 'name', 'protocolProfileBehavior' and adds 'name' to the list of 'allFolderNames'
 * @param {object} newJson changed postman_collection
 * @param {String} name new folder name
 * @param {object} protocolProfileBehavior protocolProfileBehavior of the new wfolder
 * @param {object} allFolderNames list with all folder names in newJson
 */
function createFolder(newJson, name, protocolProfileBehavior, allFolderNames, description) {
    const folder = {};
    folder["name"] = name;
    folder["item"] = [];
    folder["protocolProfileBehavior"] = protocolProfileBehavior;
    folder["description"] = description;
    newJson.item.push(folder);
    allFolderNames.push(name);
}

/**
 * Removes empty folders in newJson with no API-Requests
 * @param {object} newJson changed postman_collection
 */
function removeEmptyFolders(newJson) {
    const {item: items} = newJson;
    for (let [i, element] of items.entries()) {
        if (element.item != null && element.item.length == 0) {
            items.splice(i, 1);
        }
    }
}

/**
 * Converts postman_collection to json with openapi-specification
 * @param {object} json postman_collection with changed folder structure
 * @param {object} environments list of url environments
 * @returns {object} json with openapi-specification
 */
async function convertPostmanToOpenapi(json, environmentsFile) {
    const environmentsJson = JSON.parse(environmentsFile);
    const postmanJson = json;
    const paths_ServerUrl_allFolderTags = getPaths_ServerUrls_allFolderTags(
        postmanJson.item
    );
    const info = getInfo(postmanJson.info);
    const paths = paths_ServerUrl_allFolderTags.paths;
    const serverUrls = paths_ServerUrl_allFolderTags.serverUrls;
    const allFolderTags = paths_ServerUrl_allFolderTags.allFolderTags;
    const servers = compileEnvironments(environmentsJson, serverUrls);
    const tags = compileTags(allFolderTags);

    const openApi = {
        openapi: "3.0.0",
        info: info,
        paths: paths,
        tags: tags,
        servers: servers
    };
    return openApi;
}

/**
 * Gets information from postman_collection and compiles it into openapi-specification
 * @param {object} info information about postman_collection
 * @returns {object} information in openapi-specification
 */
function getInfo(info) {
    const infoOpenapi = {
        title: info.name,
        description: info.description,
        version: "1.0.0"
    };
    return infoOpenapi;
}

/**
 * Compiles all used serverUrls into openapi-specification
 * @param {object} environments list of url environments
 * @param {object} serverUrls list of all used serverUrls
 * @returns {object} all used serverUrls in openapi-specification
 */
function compileEnvironments(environmentsJson, serverUrls) {
    const servers = [];
    for (let index in serverUrls) {
        let name = serverUrls[index];
        for (let j in environmentsJson.values) {
            if (environmentsJson.values[j].key == name) {
                const server = {
                    url: environmentsJson.values[j].value
                };
                servers.push(server);
            }
        }
    }
    return servers;
}

/**
 * Gets all API-Request and compiles them into paths in openapi-specification
 * Checks in every API-Request which serverUrl is used and adds foldername to tags
 * @param {object} items folders with all API-Request in postman_collection
 * @returns {Object} list of folder paths, all used serverUrls and list of all folder tags
 */
function getPaths_ServerUrls_allFolderTags(items) {
    const paths = {};
    const serverUrls = [];
    const allFolderTags = {};
    //Every folder
    for (let [index, folder] of items.entries()) {
        const {item, name, description: tagDesc} = folder;
        const tag = folder.name;
        allFolderTags[tag] = {};
        if (folder.description != null) {
            allFolderTags[tag] = folder.description
        }
        //Every API-Request
        for (let request in item) {
            if (item != null) {
                //API-request
                const element = item[request];
                let pathName = element.name;
                let host = element.request.url.host[0];
                //Adds host to serverUrls
                addHost(serverUrls, host);
                let method = element.request["method"].toLowerCase();
                //Checks if pathName is already saved in paths
                if (paths[pathName] == null) {
                    paths[pathName] = {};
                }
                paths[pathName][method] = {
                    tags: [tag],
                    summary: ""
                };
                //Checks for descriptions
                if (element["request"]["description"] != null) {
                    let description = element["request"]["description"];
                    addDescription(paths, description, pathName, method);
                }
                //Checks for path variables
                if (element["request"]["url"]["variable"] != null || element["request"]["url"]["query"] != null) {
                    addParametersToPath(paths, element, pathName, method);
                }
                //Adds Requestbody
                if (method != "get") {
                    addRequestBodyToPath(paths, element, pathName, method);
                }
                //Adds Responses
                if (element["response"] != null) {
                    const responses = element.response;
                    addResponses(paths, responses, pathName, method);
                }
            }
        }
    }
    return {
        paths: paths,
        serverUrls: serverUrls,
        allFolderTags: allFolderTags
    };
}

/**
 * Adds description to API-Request
 * @param {object} paths all API-Requests
 * @param {string} description specific description of given API-Request
 * @param {string} pathName name of API-Request
 * @param {string} method method of API-Request
 */
function addDescription(paths, description, pathName, method) {
    paths[pathName][method]["description"] = description;
}

/**
 * Adds used host to serverUrls
 * @param {object} serverUrls list of all used serverUrls
 * @param {string} host used host in API-Request
 */
function addHost(serverUrls, host) {
    let hostName="";
    try{host = host.split("{{");
        host = host[1].split("}}");
        hostName = host[0];
    }catch(err){
    }
    if (serverUrls.length == 0) {
        serverUrls.push(hostName);
    } else {
        let hostExist = false;
        for (let index in serverUrls) {
            if (serverUrls[index] == hostName) {
                hostExist = true;
                break;
            }
        }
        if (!hostExist) {
            serverUrls.push(hostName);
        }
    }
}

/**
 * Adds responses to API-Request in openapi-specification
 * @param {object} paths all API-Requests
 * @param {object} responses examples in API-Request in postman-collection
 * @param {string} pathName name of API-Request
 * @param {string} method method of API-Request
 */
function addResponses(paths, responses, pathName, method) {
    paths[pathName][method]["responses"] = {};
    let content;
    for (let index in responses) {
        let content = {};
        let response = responses[index];
        let code = response.code;
        let responseName = response.name;
        let body = "";
        if (response.body != null) {
            try {
                body = JSON.parse(response.body);
            } catch (err) {
                body = response.body;
            }
        }
        const language = response["_postman_previewlanguage"];
        if (language === "json") {
            content = {
                "application/json": {
                    schema: {
                        type: "object",
                        example: body
                    }
                }
            };
        } else if (language === "html") {
            content = {
                "application/html": {
                    schema: {
                        type: "object",
                        example: response.body
                    }
                }
            };
        } else if (
            (language === "text" || language === "plain" || language === 'raw') && response.body != "") {
            content = {
                "text/plain": {
                    schema: {
                        type: "object",
                        example: response.body
                    }
                }
            };
        } else {
            possibleErrorInExample;
        }
        paths[pathName][method]["responses"][code] = {
            description: responseName,
            content: content
        };
    }
}

/**
 * Adds resquest-body to API-Request in openapi-specification
 * @param {object} paths all API-Requests
 * @param {object} element API-Request in postman_collection
 * @param {string} pathName name of API-Request
 * @param {string} method methode of API-Request
 */
function addRequestBodyToPath(paths, element, pathName, method) {
    if (element["response"] != null) {
        for (let index in element.response) {
            //Checks if request has a body
            if (
                (element.response[index].status == "OK" ||element.response[index].status == "Created" )&&
                element.response[index].originalRequest.body != null
            ) {
                paths[pathName][method]["requestBody"] = {
                    content: {}
                };
                let mode = element.response[index].originalRequest.body.mode;
                let content = {};
                if (
                    mode == "raw" &&
                    element.response[index].originalRequest.body.raw != null
                ) {
                    content = {
                        "application/json": {
                            schema: {}
                        }
                    };
                    const raw =
                        element.response[index].originalRequest.body.raw;
                    let newRaw;
                    let language;
                    try {
                        newRaw = JSON.parse(raw);
                    } catch (err) {
                    }
                    if (
                        element.response[index].originalRequest.body.options !=
                        null
                    ) {
                        language =
                            element.response[index].originalRequest.body.options
                                .raw.language;
                    } else {
                        possibleErrorInPostman(pathName, method);
                    }
                    if (language === "json" && raw != null) {
                        content["application/json"]["schema"] = {
                            type: "object",
                            example: newRaw
                        };
                    } else {
                        content["application/json"]["schema"] = {
                            type: "string",
                            example: raw
                        };
                    }
                } else if (mode == "file") {
                    content = {
                        "text/plain": {}
                    };
                } else if (mode == "formdata") {
                    content = {
                        "multipart/form-data": {
                            schema: {}
                        }
                    };
                    let formdata =
                        element.response[index].originalRequest.body.formdata;
                    content["multipart/form-data"]["schema"] = {
                        type: "object",
                        properties: {}
                    };
                    for (let j in formdata) {
                        let data = {type: "string"};
                        if (formdata[j].type == "file") {
                            data["format"] = "binary";
                        }
                        if (formdata[j].description != null) {
                            data["description"] = formdata[j].description;
                        }
                        content["multipart/form-data"]["schema"]["properties"][
                            formdata[j].key
                            ] = data;
                    }
                }
                paths[pathName][method]["requestBody"]["content"] = content;
            }
        }
    } else {
        console.log(
            pathName +
            " -> " +
            method +
            " : You should consider adding examples"
        );
    }
}

/**
 * Adds parameters to API-Request in openapi-specification
 * @param {object} paths all API-Requests
 * @param {object} element API-Request in postman_collection
 * @param {string} pathName name of API-Request
 * @param {string} method methode of API-Request
 */
function addParametersToPath(paths, element, pathName, method) {
    paths[pathName][method]["parameters"] = [];
    let parameters
    let type
    let required
    if (element.request.url.variable != null) {
        parameters = element.request.url.variable
        type = "path"
        required = true
        for (let index in parameters) {
            let parameter = {
                name: parameters[index].key,
                in: type,
                required: required,
                description: parameters[index].description
            };
            paths[pathName][method]["parameters"].push(parameter);
        }
    }
    if (element.request.url.query != null) {
        parameters = element.request.url.query
        type = "query"
        required = false
        for (let index in parameters) {
            let parameter = {
                name: parameters[index].key,
                in: type,
                required: required,
                description: parameters[index].description
            };
            paths[pathName][method]["parameters"].push(parameter);
        }
    }
}

/**
 * Compiles all folder names into openapi-specification
 * @param {object} allFolderTags list of all folder-names
 * @returns {object} all folder names in openapi-specification
 */
function compileTags(allFolderTags) {
    const tags = [];
    for (let index in allFolderTags) {
        const tag = {
            name: index,
            description: allFolderTags[index]
        };
        tags.push(tag);
    }
    return tags;
}

/**
 * Possible Errror in the given API-Request in Postman, gives suggestions
 * @param {String} name name of the API-Request
 * @param {String} method methode of the API-Request
 */
function possibleErrorInPostman(name, method) {
    console.log(
        "Check " +
        name +
        "->: " +
        method +
        " and its examples (especially 200 OK) for correctness in Postman"
    );
    console.log(
        "Possible faults: incorrect or empty body, incorrect selected language for the body "
    );
    console.log("");
}

/**
 * Possible Errror in the given example in a API-Request in Postman, gives suggestions
 * @param {String} name name of the API-Request
 * @param {String} method methode of the API-Request
 * @param {String} responseName respnse_Name of the API-Request
 */
function possibleErrorInExample(name, method, responseName) {
    console.log(
        "Check example '" +
        responseName +
        "' in '" +
        name +
        "->: " +
        method +
        "' for correctness in Postman"
    );
    console.log(
        "Possible faults: incorrect or empty body, incorrect selected language for the body "
    );
    console.log("");
}

module.exports = converter;
