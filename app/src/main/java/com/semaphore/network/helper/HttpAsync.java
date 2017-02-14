package com.semaphore.network.helper;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.semaphore.network.request.SSOperation;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class HttpAsync extends AsyncTask<SSOperation, Void, SSResponse> {

    /**
     * A static method that executes the operation request and calls the
     * operations callback with response or error.
     *
     * @param operation The operation whose request needs to be made. Depending on the
     *                  success or error the operations callback will be called with
     *                  'callback(String result)' or 'error(String error)' methods
     */
    public static void makeRequest(SSOperation operation) {
        HttpAsync client = new HttpAsync();
        client.execute(operation, null, null);
    }

    /**
     * This would make an AsyncHttp call and fetch/post the data while the
     * UIThread is not blocked
     *
     * @param asyncHttpData <p>
     *                      asyncHttpData[0] - The operation to make call to
     * @return
     */
    @Override
    protected SSResponse doInBackground(SSOperation... asyncHttpData) {

        SSOperation operation = asyncHttpData[0];
        return doRequest(operation);
    }

    @Override
    protected void onPostExecute(SSResponse result) {
        super.onPostExecute(result);
        Gson gson = new Gson();

        try {

            Map<String, Object> map = gson.fromJson(result.result,
                    new TypeToken<HashMap<String, Object>>() {
                    }.getType());
            if (map.containsKey("auth_token")
                    || result.operation.isAuthenticationRequired() == false) {
                result.operation.getCallback().callback(result.result);
            } else if (map.containsKey("message")) {
                result.operation.getCallback().error(
                        map.get("message").toString());
            } else {
                result.operation.getCallback().error("Unknown error");
            }
        } catch (JsonSyntaxException e) {
            result.operation.getCallback().error("Unknown error" + e);
        }
    }

    private SSResponse doRequest(SSOperation operation) {
        HttpRequestBase baseRequest = operation.getRequestBase();
        String jsonBody = operation.getRequestString();
        InputStream inputStream = null;
        String result = "";
        int statusCode;

        Log.v("TAG", "doRequest*****************************************");

        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL

            if (jsonBody != null
                    && baseRequest instanceof HttpEntityEnclosingRequestBase) {
                Log.i("RequestResponse", "Request: " + jsonBody);
                // 5. set json to StringEntity
                StringEntity se = new StringEntity(jsonBody, "UTF-8");
                // 6. set httpPost Entity
                ((HttpEntityEnclosingRequestBase) baseRequest).setEntity(se);
            }

            // 7. Set some headers to inform server about the type of the
            // content
            baseRequest.setHeader("Accept", "application/json");
            baseRequest.setHeader("Content-type",
                    operation.getContentTypeHeader());

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(baseRequest);

            statusCode = httpResponse.getStatusLine().getStatusCode();
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            // Log.e("InputStream", e.getLocalizedMessage());
            result = "{\"message\":\"Keep connected and stay calm \n\t                                 -internet\"}";
            statusCode = -1;
        }

        Log.i("RequestResponse", "Response: " + result);
        SSResponse response = new SSResponse();

        response.result = result;
        response.statusCode = statusCode;
        response.operation = operation;
        // 11. return result
        return response;
    }

    private String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }
}

/**
 * Just needed to handle the result and status code and is passed on to the
 * onPostExecute method.
 */
class SSResponse {
    public String result; // String received by the server
    public int statusCode; // Status code of the server
    public SSOperation operation;
}