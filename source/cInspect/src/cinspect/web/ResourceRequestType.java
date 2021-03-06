package cinspect.web;

/**
 * This enum class signifies the type of HTTP request we will send to the server. 
 * Currently, only GET and POST are implemented by {@link WebRequester}. 
 * @author Austin Bentley <ab6d9@mst.edu>
 */
public enum ResourceRequestType {
	GET, //A standard URL request like you're used to.
	POST, //A URL request where the data is not inside the URL, but sent separately, inside of POSTDATA.
	HEAD, //Similar to GET, but the server only sends HTTP headers -- not the requested data. (We won't touch this.)
	PUT, //Used for uploading files to a server. (We probably won't touch this.)
	OPTIONS, //We won't touch this.
	DELETE, //Deletes a server resource. We probably won't touch this.
	TRACE, //We won't touch this.
	CONNECT //Tries to use the HTTP server as a proxy. We probably won't touch this.
}
