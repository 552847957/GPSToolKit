package com.srmn.xwork.gpstoolkit.Tasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.srmn.xwork.androidlib.utils.IOUtil;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.androidlib.utils.UIUtil;
import com.srmn.xwork.gpstoolkit.App.MyApplication;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;
import com.srmn.xwork.gpstoolkit.R;

/**
 * Created by kiler on 2016/3/22./后面尖括号内分别是输入参数 ，进度(publishProgress用到)，返回值 类型
 */
public abstract class BaseProgressTask<E> extends AsyncTask<E, Integer, E> {

    protected Context context;
    protected String errorMessage;
    protected ProgressDialog pgDialog;
    protected String progressMessage;
    private String title;
    private String startMessage;


    public BaseProgressTask(Context context, String title, String startMessage, String progressMessage) {
        this.context = context;
        this.title = title;
        this.startMessage = startMessage;
        this.progressMessage = progressMessage;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (pgDialog != null) {
            pgDialog.setProgress(values[0]);
            pgDialog.setMessage(String.format(progressMessage, "(" + values[1] + "/" + values[2] + ")"));
        }
    }

    @Override
    protected void onPostExecute(E s) {
        super.onPostExecute(s);

        if (pgDialog != null) {
            pgDialog.dismiss();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (pgDialog != null) {
            pgDialog.dismiss();
        }
    }


    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {

        pgDialog = new ProgressDialog(context);
        pgDialog.setIndeterminate(false);
        pgDialog.setTitle(title);
        pgDialog.setMessage(startMessage);
        pgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pgDialog.setProgress(0);
        pgDialog.setMax(100);

        pgDialog.setCancelable(true);// 设置是否可以通过点击Back键取消
        pgDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
        pgDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                UIUtil.showConfrim(context, "确认信息", "确定取消上传任务？", R.drawable.ic_info_grey600_18dp, "确定", "取消", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancel(true);
                    }
                });
            }
        });
        pgDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                UIUtil.showConfrim(context, "确认信息", "确定取消上传任务？", R.drawable.ic_info_grey600_18dp, "确定", "取消", new Dialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancel(true);
                    }
                });
            }
        });

        pgDialog.show();

        super.onPreExecute();
    }


}