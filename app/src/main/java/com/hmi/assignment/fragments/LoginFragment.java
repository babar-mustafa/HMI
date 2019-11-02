package com.hmi.assignment.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hmi.assignment.R;
import com.hmi.assignment.Volley.RestNetworkRequestHandler;
import com.hmi.assignment.helperClasses.CustomEditText;
import com.hmi.assignment.interfaces.DrawableClickListener;
import com.hmi.assignment.interfaces.INetworkListener;
import com.hmi.assignment.models.BaseModel;
import com.hmi.assignment.models.User;
import com.hmi.assignment.utils.Constants;
import com.hmi.assignment.utils.Utils;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements INetworkListener {

    View view;
    Button loginuser;
    CustomEditText userpassword,number;
    boolean  isValidPassword,isvalidMobile;
    Dialog progressDialog;
    private static final Object TAG_LOGIN = "Login";
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews();
        loginuserClick();
        numberDrawableLIstener();
        userpasswordDrawableLIstener();

        return view;
    }
    public boolean toValidateLogin() {
        boolean isValidate = false;
        if (!number.getText().toString().equals("")
                && !userpassword.getText().toString().equals("") ) {
            isValidate = true;
        }
        return isValidate;
    }
    private void loginuserClick() {
        loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if (!toValidateLogin()) {
                        Utils.showToastMessage("Please fill all required fields", getActivity());

                    } else {


                        if (number.getText().toString().trim().length()>=10) {
                                if (userpassword.getText().toString().length() == 6 ||
                                        userpassword.getText().toString().length() > 6) {

                                        doLogin(number,
                                                userpassword.getText().toString());



                                } else {
                                    Utils.showToastMessage("Password Should be greater then 5 characters", getActivity());

                                }



                        } else {
                            Utils.showToastMessage("Enter Valid Mobile Number", getActivity());

                        }

//                        signup api call

                    }
                } catch (Exception e) {
                    Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

                }

            }
        });
    }
    void doLogin(String number, String paswd) {
        try {
            Utils.showDialog(progressDialog);
            RestNetworkRequestHandler restNetworkRequestHandler = new RestNetworkRequestHandler(getActivity(), this);
            HashMap<String, String> header = new HashMap<>();
            HashMap<String, String> param = new HashMap<>();
            param.put("mobile", "" + number);
            param.put("password", "" + paswd);
            Utils.e("params", param.toString());
            restNetworkRequestHandler.postRequestApi(Constants.URL_SIGNUP, param, header, TAG_LOGIN, null, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void userpasswordDrawableLIstener() {
        userpassword.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawableClickListener.DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        if (isValidPassword) {

                        } else {
                            userpassword.setText("");
                        }

                        break;

                    default:
                        break;
                }
            }

        });
    }
    private void numberDrawableLIstener() {
        number.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawableClickListener.DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        if (isvalidMobile) {

                        } else {
                            number.setText("");
                        }

                        break;

                    default:
                        break;
                }
            }

        });
    }


    private void initViews() {
        progressDialog = Utils.progressDialog(getActivity());

        loginuser = view.findViewById(R.id.loginuser);
        userpassword = view.findViewById(R.id.userpassword);
        number = view.findViewById(R.id.number);
    }

    @Override
    public void onResponseReceive(BaseModel response, Object senderTag, Object senderObject) {
        try {

            Utils.dismissDialog(progressDialog);
            if (senderTag.equals(TAG_LOGIN)) {
                Utils.showToastMessage(model.getMessage(), getActivity());
            }
        } catch (Exception e) {
            Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

        }
    }

    @Override
    public void onError(BaseModel model, Object senderTag, Object senderObject) {
        try {

            Utils.dismissDialog(progressDialog);
            if (senderTag.equals(TAG_LOGIN)) {
                Utils.showToastMessage(model.getMessage(), getActivity());
            }
        } catch (Exception e) {
            Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

        }
    }
}
