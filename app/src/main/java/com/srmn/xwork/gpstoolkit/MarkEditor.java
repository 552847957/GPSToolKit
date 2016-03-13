package com.srmn.xwork.gpstoolkit;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.srmn.xwork.androidlib.gis.GISLocation;
import com.srmn.xwork.androidlib.media.PhotoAction;
import com.srmn.xwork.androidlib.ui.BaseArrayAdapter;
import com.srmn.xwork.androidlib.utils.DateTimeUtil;
import com.srmn.xwork.androidlib.utils.IOUtil;
import com.srmn.xwork.androidlib.utils.ImageUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.androidlib.utils.UIUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.Dao.MarkerCategoryDao;
import com.srmn.xwork.gpstoolkit.Dao.MarkerDao;
import com.srmn.xwork.gpstoolkit.Entities.Marker;
import com.srmn.xwork.gpstoolkit.Entities.MarkerCategory;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;

import org.xutils.ex.DbException;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_mark_editor)
public class MarkEditor extends BaseActivity {

    private static final int REQUEST_CODE_MANAGECATEGORY = 5;
    @ViewInject(R.id.txtLocationInfo)
    protected TextView txtLocationInfo;
    @ViewInject(R.id.btnManageCategory)
    protected Button btnManageCategory;
    @ViewInject(R.id.spnCategory)
    protected Spinner spnCategory;


    @ViewInject(R.id.txtName)
    protected TextView txtName;
    @ViewInject(R.id.txtRemark)
    protected TextView txtRemark;


    protected ListView lstImages;
    @ViewInject(R.id.llPhoto)
    protected LinearLayout llPhoto;


    protected List<MarkerCategory> dataItems;

    protected List<String> loadPics;

    protected PhotoAction photoAction;

    private ArrayAdapter<MarkerCategory> spinadapter;

    private Marker editData;
    private boolean isInsert;

    private MarkerImagesAdapter markerImagesAdapter;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        lstImages = (ListView) findViewById(R.id.lstImages);

        photoAction = new PhotoAction(this);

        LoadUIData();

        spinadapter = new ArrayAdapter<MarkerCategory>(this, android.R.layout.simple_spinner_item, android.R.id.text1, dataItems);

        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCategory.setAdapter(spinadapter);

        spinadapter.notifyDataSetChanged();

        //使用Intent对象得到FirstActivity传递来的参数
        Intent intent = getIntent();

        if (intent.hasExtra("location")) {
            GISLocation location = (GISLocation) intent.getSerializableExtra("location");

            loadPics = new ArrayList<String>();
            txtLocationInfo.setText(location.toLocationInfo());
        }

        isInsert = false;

        if (editData == null) {
            isInsert = true;
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void LoadUIData() {

        dataItems = getMarkerCategoryDaoInstance().findAll();

        if (dataItems == null) {
            dataItems = new ArrayList<MarkerCategory>();
        }


    }


    @Event(value = R.id.btnManageCategory)
    private void onManageCategoryClick(View view) {
        gotoActivityForResult(MarkCategoryManage.class, REQUEST_CODE_MANAGECATEGORY);
    }

    @Event(value = R.id.btnSave)
    private void onbtnSaveClick(View view) {

        if (isInsert) {
            editData = new Marker();
        }

        MarkerCategory markerCategory = (MarkerCategory) spnCategory.getSelectedItem();
        editData.setMarkerCategoryID(markerCategory.getId());
        editData.setName(txtName.getText().toString());
        editData.setImagesList(loadPics);
        editData.setLocationInfo(txtLocationInfo.getText().toString());
        editData.setDescription(txtRemark.getText().toString());


        try {
            if (isInsert) {
                getMarkerDaoInstance().saveBindingId(editData);
            } else {
                getMarkerDaoInstance().update(editData);
            }
            showShortToastMessage("标注保存成功！");
            finish();
        } catch (DbException e) {
            showShortToastMessage("标注保存失败！");
            e.printStackTrace();
        }

    }

    private MarkerDao getMarkerDaoInstance() {
        return getDaos().getMarkerDaoInstance();
    }

    private MarkerCategoryDao getMarkerCategoryDaoInstance() {
        return getDaos().getMarkerCategoryDaoInstance();
    }


    @Event(value = R.id.btnPhoto)
    private void onbtnPhotoClick(View view) {
        getPhotoAction().takePhoto(PhotoAction.PHOTO_TACK, Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    @Event(value = R.id.llPhoto)
    private void onllPhotoClick(View view) {
        getPhotoAction().takePhoto(PhotoAction.PHOTO_TACK, Camera.CameraInfo.CAMERA_FACING_BACK);
    }


    @Event(value = R.id.btnSelectPhoto)
    private void onbtnSelectPhotoClick(View view) {
        getPhotoAction().takePhoto(PhotoAction.PHOTO_PICKUP, Camera.CameraInfo.CAMERA_FACING_BACK);
    }


    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MarkEditor Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.srmn.xwork.gpstoolkit/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode != Activity.RESULT_OK)
            return;

        if (requestCode == REQUEST_CODE_MANAGECATEGORY) {
            LoadUIData();
            spinadapter = new ArrayAdapter<MarkerCategory>(this, android.R.layout.simple_spinner_item, android.R.id.text1, dataItems);
            spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spnCategory.setAdapter(spinadapter);
            spinadapter.notifyDataSetChanged();
        } else if (requestCode == PhotoAction.PHOTO_PICKUP) {
            Bitmap bitmap = getPhotoAction().recievedPhoto(requestCode, resultCode, data);

            byte[] fileContent = ImageUtil.Bitmap2Bytes(bitmap, ImageUtil.DEFAULT_IMAGE_QUANLITY);

            String md5 = IOUtil.getFileMD5(fileContent);

            File file = new File(IOUtil.getDiskFilesDir(context), "ZP" + md5);

            String filePath = file.getAbsolutePath();

            try {
                IOUtil.saveFile(filePath, fileContent);
                loadPics.add(filePath);
                reloadImages();

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PhotoAction.PHOTO_TACK) {
            Bitmap bitmap = getPhotoAction().recievedPhoto(requestCode, resultCode, data);

            byte[] fileContent = ImageUtil.Bitmap2Bytes(bitmap, ImageUtil.DEFAULT_IMAGE_QUANLITY);

            String md5 = IOUtil.getFileMD5(fileContent);

            File file = new File(IOUtil.getDiskFilesDir(context), "ZP" + md5);

            String filePath = file.getAbsolutePath();

            try {
                IOUtil.saveFile(filePath, fileContent);
                loadPics.add(filePath);
                reloadImages();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void reloadImages() {

        markerImagesAdapter = new MarkerImagesAdapter(MarkEditor.this, R.layout.list_image_item, loadPics);
        lstImages.setAdapter(markerImagesAdapter);
        markerImagesAdapter.notifyDataSetChanged();

        UIUtil.setListViewHeigth(markerImagesAdapter, lstImages);

    }


    private PhotoAction getPhotoAction() {
        if (photoAction == null) {
            photoAction = new PhotoAction(context);
        }
        return photoAction;
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MarkEditor Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.srmn.xwork.gpstoolkit/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    class MarkerImagesAdapter extends BaseArrayAdapter<String> {

        public MarkerImagesAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final String obj = (String) getItem(position);
            final int cindex = position;
            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(layoutID, null);
                viewHolder = new ViewHolder();
                viewHolder.imgListImage = (ImageView) view.findViewById(R.id.imgListImage);
                viewHolder.btnDelete = (ImageView) view.findViewById(R.id.btnDelete);


                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            if (null == viewHolder.imgListImage)
                return view;

            ImageOptions imageOptions = new ImageOptions.Builder()
                    // 如果ImageView的大小不是定义为wrap_content, 不要crop.
                    .setCrop(true) // 很多时候设置了合适的scaleType也不需要它.
                    // 加载中或错误图片的ScaleType
                    //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
                    .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    .setLoadingDrawableId(R.mipmap.ic_launcher)
                    .setFailureDrawableId(R.mipmap.ic_launcher)
                    .build();

            x.image().bind(viewHolder.imgListImage, obj, imageOptions);


            viewHolder.imgListImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    File file = new File(obj);

                    if (!file.exists()) {
                        showShortToastMessage("图片不存在.");
                        return;
                    }

                    Intent it = new Intent(Intent.ACTION_VIEW);
                    Uri mUri = Uri.parse("file://" + file.getPath());
                    it.setDataAndType(mUri, "image/*");
                    startActivity(it);
                }
            });

            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    UIUtil.showConfrim(context, "确定确认", "确定删除该图片？", R.drawable.ic_info_grey600_18dp, "确定", "取消", new Dialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            IOUtil.deleteFile(obj);
                            loadPics.remove(cindex);
                            reloadImages();
                            showShortToastMessage("删除图片成功！");
                        }
                    });


                }
            });

            return view;
        }

        class ViewHolder {
            public ImageView imgListImage;
            public ImageView btnDelete;
        }
    }


}
