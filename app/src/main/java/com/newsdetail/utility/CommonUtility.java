package com.newsdetail.utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtility {

    public static String getFormattedDate(String inputdate, String format, String to) {
        String formattedDate = "";
        SimpleDateFormat inputFormat = new SimpleDateFormat(format);
        SimpleDateFormat outputFormat = new SimpleDateFormat(to);
        try {
            Date date = inputFormat.parse(inputdate);
            formattedDate = outputFormat.format(date);
        } catch (Exception e) {

        }
        return formattedDate;
    }

    public static long getDateInMillis(String inputdate, String format) {
        long millis = 0l;
        SimpleDateFormat inputFormat = new SimpleDateFormat(format);
        try {
            Date date = inputFormat.parse(inputdate);
            millis = date.getTime();
        } catch (Exception e) {

        }
        return millis;
    }

    public static Bitmap getBitmapfromUrl(String image) {
        try {
            URL url = new URL(image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            return null;
        }
    }

}
