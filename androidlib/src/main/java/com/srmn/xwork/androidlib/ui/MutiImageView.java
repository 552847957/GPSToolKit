package com.srmn.xwork.androidlib.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.gson.reflect.TypeToken;
import com.srmn.xwork.androidlib.R;
import com.srmn.xwork.androidlib.utils.GsonUtil;
import com.srmn.xwork.androidlib.utils.ImageUtil;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.LinkedList;
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


            mViewPager.setAdapter(new SamplePagerAdapter(imagesList, this));


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

        private LayoutInflater mInflater;
        private List<String> showImagePaths;
        private LinkedList<View> mViewCache = null;

        public SamplePagerAdapter(List<String> showImagePaths, Context context) {
            this.showImagePaths = showImagePaths;
            this.mInflater = LayoutInflater.from(context);
            this.mViewCache = new LinkedList<>();
        }



        @Override
        public int getCount() {
            return showImagePaths.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {


            ViewHolder viewHolder = null;
            View convertView = null;

            if (mViewCache.size() == 0) {
                convertView = mInflater.inflate(R.layout.muti_image_view_item, null);
                PhotoView iv_photo = (PhotoView) convertView.findViewById(R.id.iv_photo);
                TextView tvPager = (TextView) convertView.findViewById(R.id.tvPager);
                TextView tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
                viewHolder = new ViewHolder();
                viewHolder.iv_photo = iv_photo;
                viewHolder.tvPager = tvPager;
                viewHolder.tvMessage = tvMessage;
                convertView.setTag(viewHolder);
            } else {
                convertView = mViewCache.removeFirst();
                viewHolder = (ViewHolder) convertView.getTag();
            }


            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.FIT_XY)
                    .build();

            x.image().bind(viewHolder.iv_photo, showImagePaths.get(position), imageOptions);

            viewHolder.tvPager.setText(String.format("(%s/%s)", (position + 1) + "", getCount() + ""));

            String imageDescription = ImageUtil.readExifInfo(showImagePaths.get(position), ImageUtil.EXIF_TAG_IMAGE_DESCRIPTION);

            viewHolder.tvMessage.setText(imageDescription);

            // Now just add PhotoView to ViewPager and return it
            container.addView(convertView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            ;

            return convertView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        class ViewHolder {
            public PhotoView iv_photo;
            public TextView tvPager;
            public TextView tvMessage;
        }


    }


}
