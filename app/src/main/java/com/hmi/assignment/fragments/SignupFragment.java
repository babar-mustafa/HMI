package com.hmi.assignment.fragments;


import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

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
public class SignupFragment extends Fragment implements INetworkListener {

    View view;
    CustomEditText useremail, userpassword, userconfirmpassword, fullname,number;
    LinearLayout backbutton;
    boolean isvalideEmail, isValidPassword, isconfirmValidPassword,isvalidMobile;
    Button registeruser;
    Dialog progressDialog;
    private static final Object TAG_SIGNUP = "Signup";

    public SignupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        initVIews();
        backbuttonClick();
        registeruserClick();
        numberDrawableLIstener();
        useremailDrawableLIstener();
        userpasswordDrawableLIstener();
        userconfirmpasswordDrawableLIstener();
        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (number.getText().toString().length()>=10) {
                    isvalidMobile = true;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.check_circle);
                    userconfirmpassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                } else {
                    isvalidMobile = false;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.close);
                    userconfirmpassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        userconfirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (userpassword.getText().toString().equals(userconfirmpassword.getText().toString())) {
                    isconfirmValidPassword = true;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.check_circle);
                    userconfirmpassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                } else {
                    isconfirmValidPassword = false;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.close);
                    userconfirmpassword.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

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
        useremail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utils.isValidEmail(s)) {
                    isvalideEmail = true;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.check_circle);
                    useremail.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                } else {
                    isvalideEmail = false;
                    Drawable img = getContext().getResources().getDrawable(R.drawable.close);
                    useremail.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void registeruserClick() {
        registeruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if (!toValidateSignup()) {
                        Utils.showToastMessage("Please fill all required fields", getActivity());

                    } else {


                        if (Utils.isValidEmail(useremail.getText().toString().trim())) {
                            if (fullname.getText().toString().length() == 4 || fullname.getText().toString().length() > 4) {
                                if (userpassword.getText().toString().length() == 6 ||
                                        userpassword.getText().toString().length() > 6) {
                                    if (userpassword.getText().toString().equals(userconfirmpassword.getText().toString())) {

                                        doSignup(numberString, useremail.getText().toString(),
                                                userpassword.getText().toString(), genderString,
                                                fullname.getText().toString());


                                    } else {
                                        Utils.showToastMessage("Password doesn't match", getActivity());

                                    }
                                } else {
                                    Utils.showToastMessage("Password Should be greater then 5 characters", getActivity());

                                }
                            } else {
                                Utils.showToastMessage("Name Should be greater then 3 characters", getActivity());

                            }


                        } else {
                            Utils.showToastMessage("Enter Valid Email", getActivity());

                        }

//                        signup api call

                    }
                } catch (Exception e) {
                    Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

                }

            }
        });
    }

    void doSignup(String number, String email, String paswd, String gender, String fullname) {
        try {
            Utils.showDialog(progressDialog);
            RestNetworkRequestHandler restNetworkRequestHandler = new RestNetworkRequestHandler(getActivity(), this);
            HashMap<String, String> header = new HashMap<>();
            HashMap<String, String> param = new HashMap<>();
            param.put("name", "" + fullname);
            param.put("mobile", "" + number);
            param.put("email", "" + email);
            param.put("password", "" + paswd);
            Utils.e("params", param.toString());
            restNetworkRequestHandler.postRequestApi(Constants.URL_SIGNUP, param, header, TAG_SIGNUP, null, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initVIews() {
        progressDialog = Utils.progressDialog(getActivity());
        fullname = view.findViewById(R.id.fullname);
        registeruser = view.findViewById(R.id.registeruser);
        backbutton = view.findViewById(R.id.backbutton);
        number = view.findViewById(R.id.number);
        useremail = view.findViewById(R.id.useremail);
        userpassword = view.findViewById(R.id.userpassword);
        userconfirmpassword = view.findViewById(R.id.userconfirmpassword);
    }

    private void backbuttonClick() {
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    private void userconfirmpasswordDrawableLIstener() {
        userconfirmpassword.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawableClickListener.DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        //Do something here
                        if (isconfirmValidPassword) {

                        } else {
                            userconfirmpassword.setText("");
                        }

                        break;

                    default:
                        break;
                }
            }

        });
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

    private void useremailDrawableLIstener() {
        useremail.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawableClickListener.DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        if (isvalideEmail) {

                        } else {
                            useremail.setText("");
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

    public boolean toValidateSignup() {
        boolean isValidate = false;
        if (!number.getText().toString().equals("")&&
                !useremail.getText().toString().equals("")
                && !userpassword.getText().toString().equals("") &&
                !userconfirmpassword.getText().toString().equals("")
                && !fullname.getText().toString().equals("")) {
            isValidate = true;
        }
        return isValidate;
    }

    public boolean isValidPassword(CustomEditText customEditText) {
        boolean valid = false;
        if (customEditText.getText().toString().length() == 6 || customEditText.getText().toString().length() > 6) {
            valid = true;
        }

        return valid;

    }

    @Override
    public void onResponseReceive(BaseModel response, Object senderTag, Object senderObject) {
        try {

            Utils.dismissDialog(progressDialog);

            if (senderTag.equals(TAG_SIGNUP)) {
                User user = (User) response;
                Utils.showToastMessage(user.getMessage(), getActivity());

            }
        } catch (Exception e) {
            Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

        }
    }

    @Override
    public void onError(BaseModel model, Object senderTag, Object senderObject) {
        try {

            Utils.dismissDialog(progressDialog);
            if (senderTag.equals(TAG_SIGNUP)) {
                Utils.showToastMessage(model.getMessage(), getActivity());
            }
        } catch (Exception e) {
            Utils.showToastMessage(getActivity().getResources().getString(R.string.error_String), getActivity());

        }
    }
}
