package com.hmi.assignment.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hmi.assignment.MainActivity;
import com.hmi.assignment.R;
import com.hmi.assignment.Volley.RestNetworkRequestHandler;
import com.hmi.assignment.helperClasses.CustomEditText;
import com.hmi.assignment.interfaces.DrawableClickListener;
import com.hmi.assignment.interfaces.INetworkListener;
import com.hmi.assignment.models.BaseModel;
import com.hmi.assignment.models.Login;
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
    CustomEditText userpassword, number;
    boolean isValidPassword, isvalidMobile;
    Dialog progressDialog;
    private static final Object TAG_LOGIN = "Login";
    TextView createAccount;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initViews();
        number.setText("03456789098");
        userpassword.setText("123456");
        createAccountClick();
        loginuserClick();
        numberDrawableLIstener();
        userpasswordDrawableLIstener();
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (number.getText().toString().length() >= 10) {
                    isvalidMobile = true;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.check_circle);
                    number.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                } else {
                    isvalidMobile = false;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.close);
                    number.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isValidPassword(userpassword)) {
                    isValidPassword = true;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.check_circle);
                    userpassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                } else {
                    isValidPassword = false;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.close);
                    userpassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    public boolean isValidPassword(CustomEditText customEditText) {
        boolean valid = false;
        if (customEditText.getText().toString().length() == 6 || customEditText.getText().toString().length() > 6) {
            valid = true;
        }

        return valid;

    }

    private void createAccountClick() {
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {


                    Utils.FragemntTransactionSimple(getFragmentManager(),
                            R.id.containerLoginSignup, new SignupFragment(), true, "SignupFragment");
                } catch (Exception e) {
                    Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

                }
            }
        });
    }

    public boolean toValidateLogin() {
        boolean isValidate = false;
        if (!number.getText().toString().equals("")
                && !userpassword.getText().toString().equals("")) {
            isValidate = true;
        }
        return isValidate;
    }

    private void loginuserClick() {
        loginuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isInternetConnected(getActivity())){
                    try {

                        if (!toValidateLogin()) {
                            Utils.showToastMessage("Please fill all required fields", getActivity());

                        } else {


                            if (number.getText().toString().trim().length() >= 10) {
                                if (userpassword.getText().toString().length() == 6 ||
                                        userpassword.getText().toString().length() > 6) {

                                    doLogin(number.getText().toString().trim(),
                                            userpassword.getText().toString().trim());


                                } else {
                                    Utils.showToastMessage("Password Should be greater then 5 characters", getActivity());

                                }


                            } else {
                                Utils.showToastMessage("Enter Valid Mobile Number", getActivity());

                            }


                        }
                    } catch (Exception e) {
                        Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

                    }
                }else {
                    Utils.showToastMessage("Internet Not Available Please try again Later", getActivity());

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
            restNetworkRequestHandler.postRequestApi(Constants.URL_LOGIN, param, header, TAG_LOGIN, null, Login.class);
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
        createAccount = view.findViewById(R.id.createAccount);
        userpassword = view.findViewById(R.id.userpassword);
        number = view.findViewById(R.id.number);
    }

    @Override
    public void onResponseReceive(BaseModel response, Object senderTag, Object senderObject) {
        try {

            Utils.dismissDialog(progressDialog);
            if (senderTag.equals(TAG_LOGIN)) {
                Login model = (Login) response;
                Utils.showToastMessage("Login Successfully", getActivity());
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
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
