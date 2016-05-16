package com.srmn.xwork.androidlib.ui;

import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.srmn.xwork.androidlib.R;
import com.srmn.xwork.androidlib.gis.AMapHelper;
import com.srmn.xwork.androidlib.gis.ShowMarker;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.ImageUtil;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


public class SingleImageView extends BaseActivity {

    private TextView mCurrMatrixTv;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image_view);
        hideActionBar();

        ImageView mImageView = (ImageView) findViewById(R.id.iv_photo);
        mCurrMatrixTv = (TextView) findViewById(R.id.tv_current_matrix);


        // The MAGIC happens here!
        mAttacher = new PhotoViewAttacher(mImageView);

        // Lets attach some listeners, not required though!
        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
        mAttacher.setOnPhotoTapListener(new PhotoTapListener());

        Intent intent = getIntent();
        if (intent.hasExtra("imagePath")) {
            String imagePath = intent.getStringExtra("imagePath");
            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    .build();

            x.image().bind(mImageView, imagePath, imageOptions);

            String imageDescription = ImageUtil.readExifInfo(imagePath, ImageUtil.EXIF_TAG_IMAGE_DESCRIPTION);

            mCurrMatrixTv.setText(imageDescription);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Need to call clean-up
        mAttacher.cleanup();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }

    private class PhotoTapListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
        }


    }


    private class MatrixChangeListener implements PhotoViewAttacher.OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {

        }
    }


}
