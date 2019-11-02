package com.hmi.assignment.Volley;

import android.content.Context;


import com.hmi.assignment.interfaces.INetworkListener;
import com.hmi.assignment.models.BaseModel;

import java.util.Map;


public class RestNetworkRequestHandler implements INetworkListener {

    private Context context;
    private INetworkListener listener;

    public RestNetworkRequestHandler(Context context, INetworkListener listener) {
        this.listener = listener;
        this.context = context;
    }

    public void getRequestApiCache(String url, final Map<String, String> header, Object tag, Object senderObject, Class responseModel) {
        NetworkHandler networkHandler = new NetworkHandler(context, listener, responseModel);
        networkHandler.getApiCall(url, header, tag, senderObject, true);
    }
//


    public void getRequestApi(String url, final Map<String, String> header, Object tag, Object senderObject, Class responseModel) {
        NetworkHandler networkHandler = new NetworkHandler(context, listener, responseModel);
        networkHandler.getApiCall(url, header, tag, senderObject, false);
    }

    public void postRequestApi(String url, final Map<String, String> params, final Map<String, String> header, Object tag, Object senderObject, Class responseModel) {
        NetworkHandler networkHandler = new NetworkHandler(context, listener, responseModel);
        networkHandler.postApiCall(url, params, header, tag, senderObject, false);
    }


    public void putRequestApi(String url, final Map<String, String> params, final Map<String, String> header, Object tag, Object senderObject, Class responseModel) {
        NetworkHandler networkHandler = new NetworkHandler(context, listener, responseModel);
        networkHandler.putApiCall(url, params, header, tag, senderObject, false);
    }

    public void deleteRequestApi(String url, final Map<String, String> params, final Map<String, String> header, Object tag, Object senderObject, Class responseModel) {
        NetworkHandler networkHandler = new NetworkHandler(context, listener, responseModel);
        networkHandler.deleteApiCall(url, params, header, tag, senderObject, false);
    }

    @Override
    public void onResponseReceive(BaseModel response, Object senderTag, Object senderObject) {


        if (listener != null)
            listener.onResponseReceive(response, senderTag, senderObject);

    }


    @Override
    public void onError(BaseModel model, Object senderTag, Object senderObject) {
        if (listener != null)
            listener.onError(model, senderTag, senderObject);

    }


}
