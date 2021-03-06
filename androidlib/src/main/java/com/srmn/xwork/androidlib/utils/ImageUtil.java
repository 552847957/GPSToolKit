package com.srmn.xwork.androidlib.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Random;

/**
 * Created by kiler on 2016/2/24.
 */
public class ImageUtil {

    public static final int DEFAULT_IMAGE_QUANLITY = 85;
    private int width = 140, height = 40, codeLen = 4;
    public static final String EXIF_TAG_IMAGE_DESCRIPTION = ExifInterface.TAG_MAKE;
    private String checkCode = "";
    private Random random = new Random();

    public static byte[] Bitmap2Bytes(Bitmap bm, int quantity) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quantity, baos);
        return baos.toByteArray();
    }

    /**
     * 压缩图片
     *
     * @param bitmap   源图片
     * @param width    想要的宽度
     * @param height   想要的高度
     * @param isAdjust 是否自动调整尺寸, true图片就不会拉伸，false严格按照你的尺寸压缩
     * @return Bitmap
     */
    public static Bitmap reduce(Bitmap bitmap, int width, int height, boolean isAdjust) {
        // 如果想要的宽度和高度都比源图片小，就不压缩了，直接返回原图
        if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
            return bitmap;
        }
        // 根据想要的尺寸精确计算压缩比例, 方法详解：public BigDecimal divide(BigDecimal divisor, int scale, int roundingMode);
        // scale表示要保留的小数位, roundingMode表示如何处理多余的小数位，BigDecimal.ROUND_DOWN表示自动舍弃
        float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN).floatValue();
        float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN).floatValue();
        if (isAdjust) {// 如果想自动调整比例，不至于图片会拉伸
            sx = (sx < sy ? sx : sy);
            sy = sx;// 哪个比例小一点，就用哪个比例
        }
        Matrix matrix = new Matrix();
        matrix.postScale(sx, sy);// 调用api中的方法进行压缩，就大功告成了
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 旋转图片
     *
     * @param bitmap 源图片
     * @param angle  旋转角度(90为顺时针旋转,-90为逆时针旋转)
     * @return Bitmap
     */
    public static Bitmap rotate(Bitmap bitmap, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 放大或缩小图片
     *
     * @param bitmap 源图片
     * @param ratio  放大或缩小的倍数，大于1表示放大，小于1表示缩小
     * @return Bitmap
     */
    public static Bitmap zoom(Bitmap bitmap, float ratio) {
        if (ratio < 0f) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(ratio, ratio);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 在图片上印字
     *
     * @param bitmap 源图片
     * @param text   印上去的字
     * @param param  字体参数分别为：颜色,大小,是否加粗,起点x,起点y; 比如：{color : 0xFF000000, size : 30, bold : true, x : 20, y : 20}
     * @return Bitmap
     */
    public static Bitmap printWord(Bitmap bitmap, String text, Map<String, Object> param) {
        if (StringUtil.isNullOrEmpty(text) || null == param) {
            return bitmap;
        }
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        Paint paint = new Paint();
        if (null == param.get("color")) {
            paint.setColor(Color.BLACK);
        } else {
            paint.setColor((Integer) param.get("color"));
        }

        paint.setTextSize(null != param.get("size") ? (Integer) param.get("size") : 20);
        paint.setFakeBoldText(null != param.get("bold") ? (Boolean) param.get("bold") : false);
        canvas.drawText(text, null != param.get("x") ? (Integer) param.get("x") : 0, null != param.get("y") ? (Integer) param.get("y") : 0, paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }

    /**
     * 创建logo(给图片加水印),
     *
     * @param bitmaps 原图片和水印图片
     * @param left    左边起点坐标
     * @param top     顶部起点坐标t
     * @return Bitmap
     */
    public static Bitmap createLogo(Bitmap[] bitmaps, int left, int top) {
        Bitmap newBitmap = Bitmap.createBitmap(bitmaps[0].getWidth(), bitmaps[0].getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        for (int i = 0; i < bitmaps.length; i++) {
            if (i == 0) {
                canvas.drawBitmap(bitmaps[0], 0, 0, null);
            } else {
                canvas.drawBitmap(bitmaps[i], left, top, null);
            }
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        }
        return newBitmap;
    }

    /**
     * 产生一个4位随机数字的图片验证码
     *
     * @return Bitmap
     */
    public Bitmap createCode() {
        checkCode = "";
        String[] chars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
        for (int i = 0; i < codeLen; i++) {
            checkCode += chars[random.nextInt(chars.length)];
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setTextSize(30);
        paint.setColor(Color.BLUE);
        for (int i = 0; i < checkCode.length(); i++) {
            paint.setColor(randomColor(1));
            paint.setFakeBoldText(random.nextBoolean());
            float skewX = random.nextInt(11) / 10;
            paint.setTextSkewX(random.nextBoolean() ? skewX : -skewX);
            int x = width / codeLen * i + random.nextInt(10);
            canvas.drawText(String.valueOf(checkCode.charAt(i)), x, 28, paint);
        }
        for (int i = 0; i < 3; i++) {
            drawLine(canvas, paint);
        }
        for (int i = 0; i < 255; i++) {
            drawPoints(canvas, paint);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bitmap;
    }

    /**
     * 获得一个随机的颜色
     *
     * @param rate
     * @return
     */
    public int randomColor(int rate) {
        int red = random.nextInt(256) / rate, green = random.nextInt(256) / rate, blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    /**
     * 画随机线条
     *
     * @param canvas
     * @param paint
     */
    public void drawLine(Canvas canvas, Paint paint) {
        int startX = random.nextInt(width), startY = random.nextInt(height);
        int stopX = random.nextInt(width), stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(randomColor(1));
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    /**
     * 画随机干扰点
     *
     * @param canvas
     * @param paint
     */
    public void drawPoints(Canvas canvas, Paint paint) {
        int stopX = random.nextInt(width), stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(randomColor(1));
        canvas.drawPoint(stopX, stopY, paint);
    }

    /**
     * 返回真实验证码字符串
     *
     * @return String
     */
    public String getCheckCode() {
        return checkCode;
    }


    public static void wirteExifInfo(String filePath,String tag,String value)
    {
        try {
            ExifInterface exifInterface  = new ExifInterface(filePath);
            exifInterface.setAttribute(tag, StringUtil.encryptBASE64(value));
            exifInterface.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readExifInfo(String filePath,String tag)
    {
        try {
            ExifInterface exifInterface  = new ExifInterface(filePath);
            return StringUtil.decryptBASE64(exifInterface.getAttribute(tag));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


}
