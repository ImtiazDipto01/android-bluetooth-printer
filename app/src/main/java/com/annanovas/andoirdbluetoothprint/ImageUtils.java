package com.annanovas.andoirdbluetoothprint;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    private Context context;

    public ImageUtils (Context context) {
        this.context = context;
    }

    public File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "eHiseb");
        //File storageDir = new File(Environment.getExternalStorageDirectory(), "eHiseb/Images");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public Bitmap decodeBitmapFromUri(Uri selectedImage, int requiredSize) throws FileNotFoundException {
        // Decode image size 
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);
        // The new size we want to scale to 
        //final int REQUIRED_SIZE = requiredSize;
        // Find the correct scale value. It should be the power of 2. 
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }
        // Decode with inSampleSize 
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    public Bitmap decodeBitmapFromFile(String path, int reqWidth, int reqHeight) {
        // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    public Bitmap imageProcess(Bitmap imageBitmap, String imagePath) {
        int width = imageBitmap.getWidth();
        int height = imageBitmap.getHeight();
        int newWidth = 1000;
        int newHeight = 1000;
        if (width > 1000) {
            double x = (double) width / 1000d;
            newHeight = (int) (height / x);
            newWidth = 1000;
        } else if (height > 1000) {
            double x = (double) height / 1000d;
            newWidth = (int) (width / x);
            newHeight = 1000;
        }
        // calculate the scale - in this case = 0.4f 
        ExifInterface exif = null;
        int rotationAngle = 0;
        try {
            exif = new ExifInterface(imagePath);
            String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            showLog("orientString:" + orientString + " DateTime:" + exif.getAttribute(ExifInterface.TAG_IMAGE_WIDTH));
            int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
                rotationAngle = 90;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
                rotationAngle = 180;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
                rotationAngle = 270;
            showLog("Rotation Angle:" + rotationAngle);
        } catch (IOException e) {
            showLog("ExifInterface Failed!");
            e.printStackTrace();
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        matrix.postRotate(rotationAngle);
        return Bitmap.createBitmap(imageBitmap, 0, 0, width, height, matrix, true);
    }

    public String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private class ImageString extends AsyncTask<String, String, String> {
        Bitmap bitmap;

        ImageString(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return encodeToBase64(bitmap, Bitmap.CompressFormat.JPEG, 100);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            image = result;
//            showLog(image);
        }
    }

    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void showLog(String message) {
        String TAG = "ImageUtils";
        Log.e(TAG, message);
    }
}
