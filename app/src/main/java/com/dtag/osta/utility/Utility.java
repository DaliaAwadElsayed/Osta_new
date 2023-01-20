package com.dtag.osta.utility;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utility {

    private static String TAG = "Utility";

    public static void updateResources(Context mContext) {
        String lang = Sal7haSharedPreference.getSelectedLanguageValue(mContext);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources res = mContext.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    public static String fixNullString(String str) {
        if (str == null)
            return "";
        return str;
    }

    public static String fixNullString(String str, String replaceStr) {
        if (str == null)
            return replaceStr;
        return replaceStr;
    }

    public static String getRealPathFromURI(Context mContext, Uri contentURI) {
        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    public static MultipartBody.Part compressImageRequestBody(Context mContext, Uri uri) {
        // for jpg files
        Bitmap original = getBitmapFromPath(uri, mContext);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.JPEG, 80, out);
        byte[] imageBytes = out.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), encodedImage);
        return MultipartBody.Part.create(fileReqBody);
    }
    public static MultipartBody.Part compressImage(Context mContext, Uri uri, String fieldName) {
        //create a file to write bitmap data
        File f = new File(mContext.getCacheDir(), ThreadLocalRandom.current().nextInt() + ".jpg");
        try {
            f.createNewFile();
            Bitmap original = getBitmapFromPath(uri, mContext);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            original.compress(Bitmap.CompressFormat.JPEG, 70, out);
            byte[] imageBytes = out.toByteArray();
            //write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(imageBytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), f);
        MultipartBody.Part part = MultipartBody.Part.createFormData(fieldName, f.getName(), fileReqBody);
        return part;
    }
    public static List<MultipartBody.Part> compressImageArray(Context mContext, List<Uri> uri, String fieldName) {

        List<MultipartBody.Part> imagesEncodedList = new ArrayList<>();
        for (int i = 0; i < uri.size(); i++) {

            //create a file to write bitmap data
            File f = new File(mContext.getCacheDir(), ThreadLocalRandom.current().nextInt() + ".jpg");
            try {
                f.createNewFile();

                Bitmap original = getBitmapFromPath(uri.get(i), mContext);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                original.compress(Bitmap.CompressFormat.JPEG, 70, out);
                byte[] imageBytes = out.toByteArray();


                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(imageBytes);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), f);
            MultipartBody.Part part = MultipartBody.Part.createFormData(fieldName + "[" + i + "]", f.getName(), fileReqBody);
            imagesEncodedList.add(part);
        }
        return imagesEncodedList;
    }
    public static List<MultipartBody.Part> compressImageArrayWithout(Context mContext, List<Uri> uri, String fieldName) {

        List<MultipartBody.Part> imagesEncodedList = new ArrayList<>();
        for (int i = 0; i < uri.size(); i++) {

            //create a file to write bitmap data
            File f = new File(mContext.getCacheDir(), ThreadLocalRandom.current().nextInt() + ".jpg");
            try {
                f.createNewFile();

                Bitmap original = getBitmapFromPath(uri.get(i), mContext);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                original.compress(Bitmap.CompressFormat.JPEG, 70, out);
                byte[] imageBytes = out.toByteArray();


                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(imageBytes);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), f);
            MultipartBody.Part part = MultipartBody.Part.createFormData(fieldName + i, f.getName(), fileReqBody);
            imagesEncodedList.add(part);
        }
        return imagesEncodedList;
    }

    public static Bitmap getBitmapFromPath(Uri uri, Context mContext) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        ContentResolver cr = mContext.getContentResolver();
        InputStream input = null;
        InputStream input1 = null;
        try {
            input = cr.openInputStream(uri);
            BitmapFactory.decodeStream(input, null, bmOptions);
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap takenImage = null;
        try {
            input1 = cr.openInputStream(uri);
            takenImage = BitmapFactory.decodeStream(input1);
            if (input1 != null) {
                input1.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return takenImage;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
