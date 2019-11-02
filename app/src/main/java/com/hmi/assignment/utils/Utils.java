package com.hmi.assignment.utils;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.hmi.assignment.R;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Utils {
    static String TAG = "Image";

    //connectivtiy
    public static boolean isInternetConnected(Context context) {
//requires permsiion for network statw
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = manager.getActiveNetworkInfo();
        if (connection != null && connection.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    //email validation
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


    //toast
    //show normal toast messages in app
    public static void showToastMessage(String message, Context context) {
        if (message == null || message.isEmpty()) return;
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }




    //loader
    //loader for api request
    public static Dialog progressDialog(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_layout);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }

    public static void showDialog(Dialog dialog) {
        if (dialog != null && !dialog.isShowing()) dialog.show();
    }




    //log printing
    public static void e(String tag, String msg) {
        if (Constants.isDebugMOde) {

            Log.e(tag, msg);
        }
    }


    //fragment
    //fragment transaction simple
    public static void FragemntTransactionSimple(FragmentManager Manager, int containerID, Fragment fragment, boolean isaddtoBackStack, String tagtext) {

        FragmentManager fragmentManager = Manager;
        if (fragmentManager != null) {
            android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_right);

//            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
            transaction.add(containerID, fragment, tagtext);
//            transaction.addToBackStack(null);

            if (isaddtoBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();


        }

    }

    public static void checknullandSetValueofString(String datavalue, TextView tobesetOn) {
        if (datavalue != null && !datavalue.equals("")) {
            tobesetOn.setText("" + datavalue);
        } else {
            tobesetOn.setText("Not Available");

        }

    }









}
