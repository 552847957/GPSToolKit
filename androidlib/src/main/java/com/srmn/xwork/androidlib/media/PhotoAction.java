package com.srmn.xwork.androidlib.media;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.srmn.xwork.androidlib.ui.BaseActivity;
import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.IOUtil;
import com.srmn.xwork.androidlib.utils.NumberUtil;
import com.srmn.xwork.androidlib.utils.ScalingUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * Created by kiler on 2016/2/25.
 */
public class PhotoAction {

    public static final int PHOTO_TACK = 1;
    public static final int PHOTO_PICKUP = 2;
    public static final int DEFAULT_WIDTH = 720;
    public static final int DEFAULT_HEIGHT = 1280;
    public static final int DEFAULT_IMAGE_QUANLITY = 85;
    protected Context context;
    protected Uri imageUri;
    protected String imageurl;

    public PhotoAction(Context context) {
        this.context = context;
    }

    public static String GenerateTempImageName(Context context) {

        String fileName = "ZP_TEMP_PHOTO";

        File file = new File(IOUtil.getDiskCacheDir(context), fileName);

        if (file.exists())
            file.delete();

        return file.getAbsolutePath();
    }

    public Bitmap resizeBitmap(Bitmap bitmap) {

        if (bitmap.getWidth() > bitmap.getHeight()) {
            bitmap = performResize(bitmap, DEFAULT_HEIGHT, DEFAULT_WIDTH);
        } else {
            bitmap = performResize(bitmap, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        }
        return bitmap;
    }

    public void takePhoto(int photoPickType, int camera_facing) {
        if (photoPickType == PHOTO_TACK) {

            imageurl = GenerateTempImageName(context);

            File file = new File(imageurl);

            imageUri = Uri.fromFile(file);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", camera_facing);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            ((BaseActivity) context).startActivityForResult(intent, PHOTO_TACK);


        } else if (photoPickType == PHOTO_PICKUP) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            ((BaseActivity) context).startActivityForResult(intent, PHOTO_PICKUP);
        }
    }


    public String GenerateStorageImageName(Context context) {

        String fileNamePreFix = "ZP" + DateTimeUtil.FormatTimeName(new Date());

        int i = 1;

        boolean hasFile = true;

        String fileName = "";

        File file = null;

        while (true) {
            fileName = fileNamePreFix + NumberUtil.GetCode(i, 2);
            file = new File(IOUtil.getDiskCacheDir(context), fileName);
            hasFile = file.exists();

            if (!hasFile) {
                break;
            }
        }

        return file.getAbsolutePath();
    }

    public Bitmap performResize(Bitmap bitmap, int requiredWidth,
                                int requiredHeight) {
        int imageWidth = bitmap.getWidth();
        int imageHeight = bitmap.getHeight();
        float differenceWidth = requiredWidth - imageWidth;
        float percentage = differenceWidth / imageWidth * 100;
        float estimatedheight = imageHeight + (percentage * imageHeight / 100);
        float estimatedwidth = requiredWidth;

        if (estimatedheight < requiredHeight) {
            float incresePercentage = (float) (requiredHeight - estimatedheight);
            percentage += (incresePercentage / imageHeight * 100);
            estimatedheight = imageHeight + (percentage * imageHeight / 100);
            estimatedwidth = imageWidth + (percentage * imageWidth / 100);
        }

        bitmap = ScalingUtilities.performResize(bitmap, (int) estimatedheight,
                (int) estimatedwidth);

        if (bitmap.getHeight() < requiredHeight) // if calculate height is
        // smaller then the required
        // Height
        {
        } else {
            int xCropPosition = (int) ((bitmap.getWidth() - requiredWidth) / 2);
            int yCropPosition = (int) ((bitmap.getHeight() - requiredHeight) / 2);

            bitmap = Bitmap.createBitmap(bitmap, xCropPosition, yCropPosition,
                    (int) requiredWidth, (int) requiredHeight);
        }
        return bitmap;
    }

    private Bitmap getThumbPic(String photoPath) {
        // Get the dimensions of the View
        int targetW = DEFAULT_WIDTH;
        int targetH = DEFAULT_HEIGHT;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

    public Bitmap recievedPhoto(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_PICKUP) {
            Uri photoUri = data.getData();
            if (photoUri == null) {
                ((BaseActivity) context).showShortToastMessage("无法获取图片路径。");

                return null;
            }
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(photoUri,
                    filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            return resizeBitmap(BitmapFactory.decodeFile(picturePath));
        } else if (requestCode == PHOTO_TACK) {
            // 获得压缩后的bitmap
            Bitmap imageBitmap = BitmapFactory.decodeFile(imageurl);

            imageBitmap = resizeBitmap(imageBitmap);

            String imageStorageUrl = GenerateStorageImageName(context);

            try {
                OutputStream outStream = new FileOutputStream(imageStorageUrl);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, DEFAULT_IMAGE_QUANLITY, outStream);
                outStream.flush();
                outStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageurl = imageStorageUrl;
            imageUri = Uri.fromFile(new File(imageurl));
            imageBitmap = BitmapFactory.decodeFile(imageurl);
            return imageBitmap;
        }
        return null;
    }
}
