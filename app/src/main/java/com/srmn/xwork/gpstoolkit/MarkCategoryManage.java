package com.srmn.xwork.gpstoolkit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.srmn.xwork.androidlib.ui.BaseArrayAdapter;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.gpstoolkit.App.BaseActivity;
import com.srmn.xwork.gpstoolkit.App.MyApplication;
import com.srmn.xwork.gpstoolkit.Entities.MarkerCategory;

import org.xutils.ex.DbException;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MarkCategoryManage extends BaseActivity {


    @BindView(R.id.lstCategory)
    protected ListView lstCategory;

    protected List<MarkerCategory> dataItems;
    protected MarkerCategoryAdapter markerCategoryAdapter;
    protected AlertDialog opEditDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showBackButton();

        LoadCategoryData();

        markerCategoryAdapter = new MarkerCategoryAdapter(MarkCategoryManage.this, R.layout.item_category, dataItems);

        lstCategory.setAdapter(markerCategoryAdapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_common, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        setResult(Activity.RESULT_OK);
        finish();

        return super.onOptionsItemSelected(item);
    }

    public void reloadData() {
        LoadCategoryData();

        markerCategoryAdapter = new MarkerCategoryAdapter(MarkCategoryManage.this, R.layout.item_category, dataItems);
        lstCategory.setAdapter(markerCategoryAdapter);
        markerCategoryAdapter.notifyDataSetChanged();

//        setHeight();


    }

    private void LoadCategoryData() {

        dataItems = getDaos().getMarkerCategoryDaoInstance().findAll();

        if (dataItems == null) {
            dataItems = new ArrayList<MarkerCategory>();
        }
    }

    //@Event(value = R.id.lstCategory, type = ListView.OnItemClickListener.class)
    @OnItemClick(R.id.lstCategory)
    protected void lstCategoryOnItemClick(AdapterView<?> parent, View v, int aPosition,
                                          long arg3) {

        final View v1 = LayoutInflater.from(context).inflate(R.layout.dialog_operation, null);
        final MarkerCategory opData = (MarkerCategory) parent.getItemAtPosition(aPosition);


        View.OnClickListener onclick = new View.OnClickListener() {
            @Override
            public void onClick(View cview) {

                if (opEditDialog != null)
                    opEditDialog.hide();

                if (cview.getId() == R.id.rlEdit) {
                    showEditDialog(opData);
                } else if (cview.getId() == R.id.rlDelete) {
                    try {
                        getDaos().getMarkerCategoryDaoInstance().delete(opData);
                        showShortToastMessage("删除类别成功！");
                    } catch (DbException e) {
                        showShortToastMessage("删除类别失败：" + e.getMessage());
                        e.printStackTrace();
                    }

                    reloadData();

                }
            }
        };

        RelativeLayout rlEdit = (RelativeLayout) v1.findViewById(R.id.rlEdit);
        RelativeLayout rlDelete = (RelativeLayout) v1.findViewById(R.id.rlDelete);

        rlEdit.setOnClickListener(onclick);
        rlDelete.setOnClickListener(onclick);

        opEditDialog = new android.app.AlertDialog.Builder(context)
                .setTitle("操作数据")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(v1)
                .setNegativeButton("取消", null)
                .show();

    }

    //@Event(value = R.id.btnSave)
    @OnClick(R.id.btnSave)
    protected void onSaveClick(View view) {

        showEditDialog(null);

    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_mark_category_manage;
    }

    private void showEditDialog(final MarkerCategory opData) {

        final View v = LayoutInflater.from(context).inflate(R.layout.dialog_category, null);


        final EditText txtId = (EditText) v.findViewById(R.id.txtId);
        final EditText txtName = (EditText) v.findViewById(R.id.txtName);

        if (opData != null) {
            txtId.setText(opData.getId() + "");
            txtId.setEnabled(false);
            txtName.setText(opData.getName());
        } else {
            txtId.setEnabled(true);
            txtId.setText("");
            txtName.setText("");
        }

        new android.app.AlertDialog.Builder(context)
                .setTitle("类型数据")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(v)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (opData == null) {
                            int id = 1;

                            if (!StringUtil.isNullOrEmpty(txtId.getText().toString())) {
                                id = Integer.parseInt(txtId.getText().toString());
                            } else {
                                id = getDaos().getMarkerCategoryDaoInstance().getNewId();
                            }

                            if (getDaos().getMarkerCategoryDaoInstance().checkIDIsExisted(id)) {
                                showShortToastMessage("ID已经存在！");
                                return;
                            }


                            MarkerCategory markerCategory = new MarkerCategory();

                            markerCategory.setId(id);
                            markerCategory.setName(txtName.getText().toString());

                            try {
                                getDaos().getMarkerCategoryDaoInstance().save(markerCategory);
                                showShortToastMessage("数据添加成功！");
                            } catch (DbException e) {
                                showShortToastMessage("数据添加失败：" + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            MarkerCategory markerCategory = MyApplication.getInstance().getDaos().getMarkerCategoryDaoInstance().findById(opData.getId());

                            if (markerCategory != null) {
                                try {
                                    markerCategory.setName(txtName.getText().toString());
                                    getDaos().getMarkerCategoryDaoInstance().update(markerCategory);
                                    showShortToastMessage("数据编辑成功！");
                                } catch (DbException e) {
                                    showShortToastMessage("数据编辑失败：" + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                        }


                        reloadData();


                    }
                }).setNegativeButton("取消", null)
                .show();
    }

    class MarkerCategoryAdapter extends BaseArrayAdapter<MarkerCategory> {

        public MarkerCategoryAdapter(Context context, int resource, List<MarkerCategory> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            MarkerCategory markerCategory = (MarkerCategory) getItem(position);

            View view;
            ViewHolder viewHolder;

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(layoutID, null);
                viewHolder = new ViewHolder();
                viewHolder.txtId = (TextView) view.findViewById(R.id.txtId);
                viewHolder.txtName = (TextView) view.findViewById(R.id.txtName);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }


            viewHolder.txtId.setText(markerCategory.getId() + "");
            viewHolder.txtName.setText(markerCategory.getName() + "");

            return view;
        }

        class ViewHolder {
            public TextView txtId;
            public TextView txtName;
        }
    }
}
