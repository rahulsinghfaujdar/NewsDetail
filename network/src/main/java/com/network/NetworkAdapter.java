package com.network;

import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * NetworkAdapter Builder class to call Api's using HttpsURLConnection
 * developed by Rahul
 */
public class NetworkAdapter {

    public static final String USER_AGENT = BuildConfig.LIBRARY_PACKAGE_NAME + "/Ver-" + BuildConfig.BUILD_TYPE + "/";
    private String ApiBaseUrl;
    private String Route;
    private String ApiEndPoint;
    private Map<String, String> headers;
    private Map<String, String> query;
    private NetworkAdapter.RequestType RequestType;
    private boolean isShowNetworkLogs;

    //Constructor
    private NetworkAdapter(NetworkAdapterBuilder builder) {
        this.RequestType = builder.RequestType;
        this.ApiBaseUrl = builder.ApiBaseUrl;
        this.Route = builder.Route;
        this.ApiEndPoint = builder.ApiEndPoint;
    }

    /*
     * Getter & Setter
     */
    public String getApiBaseUrl() {
        return ApiBaseUrl;
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        ApiBaseUrl = apiBaseUrl;
    }

    public String getRoute() {
        return Route;
    }

    public void setRoute(String route) {
        Route = route;
    }

    public String getApiEndPoint() {
        return ApiEndPoint;
    }

    public void setApiEndPoint(String apiEndPoint) {
        ApiEndPoint = apiEndPoint;
    }

    public boolean isShowNetworkLogs() {
        return isShowNetworkLogs;
    }

    public void setShowNetworkLogs(boolean isShowLogs) {
        isShowNetworkLogs = isShowLogs;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public void setQuery(Map<String, String> query) {
        this.query = query;
    }

    public NetworkAdapter.RequestType getRequestType() {
        return RequestType;
    }

    public void setRequestType(NetworkAdapter.RequestType requestType) {
        RequestType = requestType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    //Method To call Api over the network and provide response data
    public BaseResponseModel enqueeApi() {
        String result = "";
        BaseResponseModel baseResponseModel = null;
        StringBuffer finalUrl = new StringBuffer();

        //Build final url
        if (!TextUtils.isEmpty(ApiBaseUrl) && !TextUtils.isEmpty(Route) && !TextUtils.isEmpty(ApiEndPoint)) {
            finalUrl.append(ApiBaseUrl);
            finalUrl.append(Route);
            finalUrl.append(ApiEndPoint);

            //add query params
            if (query != null && !query.isEmpty()) {
                finalUrl.append("?");
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    finalUrl.append(entry.getKey() + "=" + entry.getValue());
                }
            }
        }

        try {
            //Create URL
            URL httpbinEndpoint = new URL(finalUrl.toString());

            //Create connection to any REST endpoint
            HttpsURLConnection myConnection = (HttpsURLConnection) httpbinEndpoint.openConnection();

            //Connection Time configs
            myConnection.setConnectTimeout(30 * 1000);
            myConnection.setReadTimeout(60 * 1000);

            //Adding Request Type
            myConnection.setRequestMethod(RequestType.toString());

            //Adding Request Headers
            myConnection.setRequestProperty("User-Agent", USER_AGENT);
            if (RequestType.equals(RequestType.POST)) {
                myConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                myConnection.setRequestProperty("Content-Type", "application/json");
            }

            //adding custom headers
            if (headers != null && !headers.isEmpty()) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    myConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            //Intercept Request
            if (isShowNetworkLogs) {
                System.out.println("****** Start Content of the URL ********");
                System.out.println("Request Type : "+myConnection.getRequestMethod());
                System.out.println("Request URL : "+myConnection.getURL());
            }

            if (myConnection.getResponseCode() == 200) {
                // Success

                //get a reference to the input stream of the connection.
                InputStream responseBody = myConnection.getInputStream();

                //return data formatted as valid JSON
                InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                int data = responseBodyReader.read();

                while (data != -1) {
                    result += (char) data;
                    data = responseBodyReader.read();
                }

                //Intercept Reponse
                if (isShowNetworkLogs) {
                    System.out.println("Response Code : " + myConnection.getResponseCode());
                    System.out.println("Response Body : " + result);
                    System.out.println("****** End Content of the URL ********");
                }
                baseResponseModel = new BaseResponseModel(myConnection.getResponseCode(),result);

                //Close connection
                myConnection.disconnect();
            } else {
                // Error handling code goes here
                if (isShowNetworkLogs) {
                    System.out.println("Response Code : " + myConnection.getResponseCode());
                    System.out.println("Response Body : " + result);
                    System.out.println("****** End Content of the URL ********");
                }
                baseResponseModel = new BaseResponseModel(myConnection.getResponseCode(),result);

                //close connection
                myConnection.disconnect();
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseResponseModel;
    }

    //Network Request Methods
    public enum RequestType {GET, PUT, POST, DELETE, PATCH}

    //Builder Class
    public static class NetworkAdapterBuilder {

        // required parameters
        private final String ApiBaseUrl;
        private final String Route;
        private final String ApiEndPoint;
        private final NetworkAdapter.RequestType RequestType;

        //Constructor
        public NetworkAdapterBuilder(NetworkAdapter.RequestType RequestType, String ApiBaseUrl, String Route, String ApiEndPoint) {
            this.RequestType = RequestType;
            this.ApiBaseUrl = ApiBaseUrl;
            this.Route = Route;
            this.ApiEndPoint = ApiEndPoint;
        }

        public NetworkAdapter build() {
            return new NetworkAdapter(this);
        }

    }


    /**
     * Response Model class
     */
    public class BaseResponseModel{

        private int ResponseCode;
        private String ResponseMessage;

        public BaseResponseModel(int responseCode, String responseMessage) {
            ResponseCode = responseCode;
            ResponseMessage = responseMessage;
        }

        public int getResponseCode() {
            return ResponseCode;
        }

        public String getResponseMessage() {
            return ResponseMessage;
        }
    }
}
