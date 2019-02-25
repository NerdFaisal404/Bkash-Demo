package com.faisal.bkashdemo;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hasanaits on 12/12/17.
 */

public class Utils {

    private static final String TAG = "UtilsJiggle";



    /**
     * Check if mail is valid
     * @param mail
     * @return
     */
    public static boolean isValidMail(String mail){

        if(TextUtils.isEmpty(mail))
            return false;

        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mail);

        return m.find();
    }

    public static boolean isPassWordValid(String password, int minDigit){
        if(TextUtils.isEmpty(password) || password.trim().length() < minDigit)
            return false;

        return true;
    }

    /**
     * Show Long Toast
     *
     * @param context
     * @param msg
     */
    public static void showLongToast(Context context, String msg) {
        String message = msg;

        if (TextUtils.isEmpty(message))
            message = "";

        if (context != null) {
            try {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e("ShortToast", e.getMessage() + "");
            }
        } else {
            Log.e("ShortToast", "Context Can not be null");
        }
    }

    /**
     * SHow Short Toast
     *
     * @param context
     * @param msg
     */
    public static void showShortToast(Context context, String msg) {
        String message = msg;

        if (TextUtils.isEmpty(message))
            message = "";

        if (context != null) {
            try {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Log.e("ShortToast", e.getMessage() + "");
            }
        } else {
            Log.e("ShortToast", "Context Can not be null");
        }
    }

    /**
     * Init Progress Dialog
     *
     * @param activity
     * @param msg
     * @return
     */
    public static ProgressDialog initProgressDialog(Activity activity, String msg) {
        if (TextUtils.isEmpty(msg)) {
            msg = "Loading...";
        }

        ProgressDialog progressDialog = null;
        try {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(msg);
            progressDialog.setCancelable(false);
        } catch (Exception e) {
            Log.e("ProgressDialog", "Can Not Initiate Progress Dialog" + e + "");
        }


        return progressDialog;
    }

    /**
     * Show Progress Dialog
     *
     * @param progressDialog
     */
    public static void showProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            try {
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            } catch (Exception e) {
                Log.e("ProgressDialog", e.getMessage() + "");
            }

        } else {
            Log.e("ProgressDialog", "Initiate Progress Dialog");
        }
    }

    /**
     * hide Progress dialog
     *
     * @param progressDialog
     */
    public static void hideProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            try {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } catch (Exception e) {
                Log.e("ProgressDialog", e.getMessage() + "");
            }

        } else {
            Log.e("ProgressDialog", "Initiate Progress Dialog");
        }
    }



    /**
     * @param activity the context of the activity
     * @brief methods for showing the soft keyboard by forced
     */
    public static void showSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), 0);
        } else {
            Log.e("showSoftKeyboard", "Current Focus is null");
        }
    }

    /**
     * @param activity the context of the activity
     * @brief methods for hiding the soft keyboard by forced
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        } else {
            Log.e("showSoftKeyboard", "Current Focus is null");
        }
    }


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color product1 will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    //Date converter
    public static String dateConverter(String dateFromApi) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM h:mm a");
        Date date = null;
        try {
            date = inputFormat.parse(dateFromApi);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedDate = outputFormat.format(date);
        String str = formattedDate.replace("AM", "am").replace("PM", "pm");
        return str; // 13 Oct 10:34 am
    }


    public static boolean internetCheck(Context context)
    {
        boolean available=false;
        ConnectivityManager connectivity= (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connectivity!=null)
        {
            NetworkInfo[] networkInfo= connectivity.getAllNetworkInfo();
            if(networkInfo!=null)
            {
                for(int i=0; i<networkInfo.length;i++)
                {
                    if(networkInfo[i].getState()== NetworkInfo.State.CONNECTED)
                    {
                        available=true;
                        break;
                    }
                }
            }


        }

        return available;
    }


}
