package com.semaphore.network.helper;

/**
 * The handlers should implement the interface to handle the responses of the requests 
 * made using HttpAsync.makeRequest(Operation operation) call
 * 
 * @author arjavdave
 *
 */
public interface ISSCallback {

	/**
	 * Called only if the result is considered a success
	 * @param result The json string returned from the server. 
	 * It is the responsibility of the handlers to parse the string and act as needed.
	 */
	public void callback(String result);
	
	/**
	 * Called if the result is considered an error
	 * @param error Error string returned by the server
	 */
	public void error(String error);

}