package com.yusufemirbektas.sozlukBeta.mainApplication.forum.imageUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ImageUtils {
    //constants of image size when uploading the profile photo
    public static final int LOW_QUALITY_IMAGE_SIZE = 30000;
    public static final int HIGH_QUALITY_IMAGE_SIZE = 120000;
    //method to convert an image to string
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String imageToString(Bitmap bitmap, int targetBytes) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int currQuality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        while (byteArray.length > targetBytes) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, currQuality, byteArrayOutputStream);
            byteArray = byteArrayOutputStream.toByteArray();
            currQuality = (int) (currQuality * 0.9);
        }

        return Base64.getEncoder().encodeToString(byteArray);
    }

    //method to convert a string to a bitmap
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Bitmap stringToBitmap(String str) {
        byte[] bytes = Base64.getDecoder().decode(str);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
