package com.hmi.assignment.models;

public class User extends BaseModel {

    private String uid;

    private String special_msg;


    public void setUid(String uid){
        this.uid = uid;
    }
    public String getUid(){
        return this.uid;
    }
    public void setSpecial_msg(String special_msg){
        this.special_msg = special_msg;
    }
    public String getSpecial_msg(){
        return this.special_msg;
    }
}
