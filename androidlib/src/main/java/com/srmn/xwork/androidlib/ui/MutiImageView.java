package com.srmn.xwork.androidlib.ui;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.srmn.xwork.androidlib.R;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.ImageUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

public class MutiImageView extends BaseActivity {

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_image_view);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        setContentView(mViewPager);
        hideActionBar();


        Intent intent = getIntent();

        if (intent.hasExtra("imagePaths")) {
            String imagePaths = intent.getStringExtra("imagePaths");

            List<String> imagesList = GsonUtil.<List<String>>DeserializerSingleDataResult(imagePaths, new TypeToken<List<String>>() {
            }.getType());


            mViewPager.setAdapter(new SamplePagerAdapter(imagesList));


        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }


    static class SamplePagerAdapter extends PagerAdapter {

        public SamplePagerAdapter(List<String> showImagePaths) {
            this.showImagePaths = showImagePaths;
        }

        private List<String> showImagePaths;

        @Override
        public int getCount() {
            return showImagePaths.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());


            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    .build();

            x.image().bind(photoView, showImagePaths.get(position), imageOptions);

            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
