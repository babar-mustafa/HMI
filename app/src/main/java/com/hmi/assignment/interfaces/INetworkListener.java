package com.hmi.assignment.interfaces;


import com.hmi.assignment.models.BaseModel;

public interface INetworkListener {
    public void onResponseReceive(BaseModel response, Object senderTag, Object senderObject);

    public void onError(BaseModel model, Object senderTag, Object senderObject);
}
