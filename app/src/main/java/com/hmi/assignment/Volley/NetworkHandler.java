package com.hmi.assignment.Volley;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;

import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.hmi.assignment.R;
import com.hmi.assignment.enums.HttpRequestType;
import com.hmi.assignment.interfaces.INetworkListener;
import com.hmi.assignment.models.BaseModel;
import com.hmi.assignment.utils.ApplicationController;
import com.hmi.assignment.utils.Constants;
import com.hmi.assignment.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;


public class NetworkHandler {

    private static int MY_SOCKET_TIMEOUT_MS = 15000;
    private RequestQueue mRequestQueue;
    private String TAG = "http Response";
    private INetworkListener listener;
    private Class mResponseModel;
    private Context mContext;


    /*
     * @param mResponseModel provide Response Model to receive response
     * @param listener      provide call back listener
     */
    public NetworkHandler(Context context, INetworkListener listener, Class responseModel) {
//        mRequestQueue = Volley.newRequestQueue(context);
        this.listener = listener;
        mResponseModel = responseModel;
        mContext = context;
    }

    /*
     *
     *           Handle Cache Management
     *
     * */
    public static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response) {
        Cache.Entry entry = null;
        try {


            long now = System.currentTimeMillis();

            Map<String, String> headers = response.headers;
            long serverDate = 0;
            String serverEtag = null;
            String headerValue;

            headerValue = headers.get("Date");
            if (headerValue != null) {
                serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
            }

            serverEtag = headers.get("ETag");
            //30000
            final long cacheHitButRefreshed = 30 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
            final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
            final long softExpire = now + cacheHitButRefreshed;
            final long ttl = now + cacheExpired;

            entry = new Cache.Entry();
            entry.data = response.data;
            entry.etag = serverEtag;
            entry.softTtl = softExpire;
            entry.ttl = ttl;
            entry.serverDate = serverDate;
            entry.responseHeaders = headers;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return entry;
    }

    /**
     * Post Api Call
     *
     * @param url          provide url to hit
     * @param param        provide post parameters body
     * @param header       provide header map with key value pair
     * @param tag          provide tag to maintain queue
     * @param paramRequest provide object to receive on response successful
     * @return A String indicating the type of transition
     */
    public void postApiCall(final String url,
                            final Map<String, String> param,
                            final Map<String, String> header,
                            final Object tag,
                            final Object paramRequest,
                            boolean isCache
    ) {
        generalApiCall(url, param, header, tag, paramRequest, isCache, HttpRequestType.POST);
    }

    public void putApiCall(final String url,
                           final Map<String, String> param,
                           final Map<String, String> header,
                           final Object tag,
                           final Object paramRequest,
                           boolean isCache
    ) {
        generalApiCall(url, param, header, tag, paramRequest, isCache, HttpRequestType.GET);
    }

    public void deleteApiCall(final String url,
                              final Map<String, String> param,
                              final Map<String, String> header,
                              final Object tag,
                              final Object paramRequest,
                              boolean isCache
    ) {
        generalApiCall(url, param, header, tag, paramRequest, isCache, HttpRequestType.DELETE);
    }

    /**
     * General Api Call
     *
     * @param url          provide url to hit
     * @param param        provide post parameters body
     * @param header       provide header map with key value pair
     * @param tag          provide tag to maintain queue
     * @param paramRequest provide object to receive on response successful
     * @return A String indicating the type of transition
     */
    public void generalApiCall(final String url,
                               final Map<String, String> param,
                               final Map<String, String> header,
                               final Object tag,
                               final Object paramRequest,
                               boolean isCache, HttpRequestType requestType
    ) {
        final StringRequest stringRequest = new StringRequest(getRequestType(requestType), url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            if (Constants.isDebugMOde) {

                                Log.e(TAG, "--url--" + url + " Success: " + response);
                            }
//                        System.out.println(response);
                            BaseModel baseModel;
                            try {

                                baseModel = (BaseModel) new Gson().fromJson(response, mResponseModel);
                            } catch (Exception ex) {
                                Utils.e("Network Exc",ex.toString());
                                ex.printStackTrace();
                                baseModel = getBaseBaseModel(ex);
                            }
                            if (listener != null) {
                                if (!baseModel.getStatus())
                                    listener.onError(baseModel, tag, paramRequest);
                                else listener.onResponseReceive(baseModel, tag, paramRequest);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG, "--url--" + url + " Error: " + response);
                            BaseModel baseModel = new BaseModel();
                            baseModel.setMessage("Connection error.");
                            listener.onError(baseModel, tag, paramRequest);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley Error", "Error: " + error
//                        + "\nStatus Code " + error.networkResponse.statusCode
//                        + "\nCause " + error.getCause()
//                        + "\nnetworkResponse " + error.networkResponse.data.toString()
//                        + "\nmessage" + error.getMessage());
                if (error.networkResponse != null && error.networkResponse.data != null)
                    httpServerErrorHandler(error, tag, paramRequest, url);
                else {
                    BaseModel baseModel = new BaseModel();
                    onError(error, baseModel, tag, paramRequest);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (param == null) return super.getParams();
                return param;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (header == null) return super.getHeaders();
                return header;
            }


            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String parsed;
                try {
                    parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                } catch (UnsupportedEncodingException e) {
                    parsed = new String(response.data);
                }
                return Response.success(parsed, parseIgnoreCacheHeaders(response));
            }

        };
        stringRequest.setTag(tag);
        stringRequest.setShouldCache(isCache);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        ApplicationController.getApplication().addToRequestedQueue(stringRequest, tag);
    }

    /**
     * Get Api Call
     *
     * @param url          provide url to hit
     * @param header       provide header map with key value pair
     * @param tag          provide tag to maintain queue
     * @param paramRequest provide object to receive on response successful
     * @return A String indicating the type of transition
     */
    public void getApiCall(final String url,
                           final Map<String, String> header,
                           final Object tag,
                           final Object paramRequest, boolean isCache

    ) {
        generalApiCall(url, null, header, tag, paramRequest, isCache, HttpRequestType.GET);
    }

    //on standard http response of server
    private void httpServerErrorHandler(VolleyError error, Object tag, Object paramRequest, String url) {
        VolleyError err = new VolleyError(new String(error.networkResponse.data));
//        Log.e(TAG, "--url--" + url + "  Error: " + err);
        BaseModel baseModel = null;
        try {
            baseModel = new Gson().fromJson(err.getMessage(), BaseModel.class);
            if (baseModel == null) {
                baseModel = getBaseBaseModel("Auth Failed", error.networkResponse.statusCode);
//
            }
        } catch (Exception ex) {
            Log.e(TAG, "--url-- " + url + " Error: ");
            baseModel = getBaseBaseModel(ex);
            if (listener != null)
                listener.onError(baseModel, tag, paramRequest);
            ex.printStackTrace();
            return;
        }
        if (baseModel != null && listener != null)
            listener.onError(baseModel, tag, paramRequest);
    }

    //Create Error model when exception
    @NonNull
    private BaseModel getBaseBaseModel(String ex, int code) {
        BaseModel baseModel;
        baseModel = new BaseModel();
        baseModel.setMessage(ex.toString());
        baseModel.setStatus(true);
        return baseModel;
    }

    @NonNull
    private BaseModel getBaseBaseModel(Exception ex) {
        BaseModel baseModel;
        baseModel = new BaseModel();
        baseModel.setMessage(ex.toString());
        baseModel.setStatus(true);
        return baseModel;
    }

    //Create Error Message
    private void onError(VolleyError error, BaseModel baseModel, Object tag, Object paramRequest) {
//        Log.e("Volly Error", "" + error.getMessage());
        String errorMessage = "";
        if (error instanceof NoConnectionError)
            errorMessage = mContext.getString(R.string.network_internet_connection_error);

        if (!baseModel.getStatus() && errorMessage.isEmpty())
            errorMessage = (mContext.getString(R.string.toast_request_timeout_excetion));
        baseModel.setMessage(errorMessage);
        baseModel.setStatus(true);
        if (listener != null)
            listener.onError(baseModel, tag, paramRequest);
    }

    //Returns http request type
    private int getRequestType(HttpRequestType requestType) {
        int request = 0;
        switch (requestType) {
            case POST:
                request = Request.Method.POST;
                break;
            case PUT:
                request = Request.Method.PUT;
                break;
            case DELETE:
                request = Request.Method.DELETE;
                break;
            case GET:
                request = Request.Method.GET;
                break;
        }
        return request;
    }

    //This will manage cache as google official document suggests.
    private void manageCache(Context context, boolean isCache) {

        // Instantiate the cache
        Cache cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        Network network = new BasicNetwork(new HurlStack());

// Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);

// Start the queue
        mRequestQueue.start();
    }
}
