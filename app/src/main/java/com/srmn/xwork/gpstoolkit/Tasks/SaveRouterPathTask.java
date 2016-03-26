package com.srmn.xwork.gpstoolkit.Tasks;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.srmn.xwork.androidlib.utils.StringUtil;
import com.srmn.xwork.androidlib.utils.UIUtil;
import com.srmn.xwork.gpstoolkit.App.MyApplication;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;
import com.srmn.xwork.gpstoolkit.Entities.RouterPathItem;

/**
 * Created by kiler on 2016/3/23.
 */
public class SaveRouterPathTask extends BaseProgressTask<RouterPath> {

    public static final String TAG = "SaveRouterPathTask";

    public SaveRouterPathTask(Context context, String title, String startMessage, String progressMessage) {
        super(context, title, startMessage, progressMessage);
    }

    @Override
    protected RouterPath doInBackground(RouterPath... routerPaths) {


        RouterPath saveRouterPath = routerPaths[0];


        AVObject routerPathClould = MyApplication.getInstance().getCloudDb().convertRouterPathToClould(saveRouterPath);

        int i = 0;
        int totalTaskCount = 1;

        if (saveRouterPath.getItems() != null) {
            totalTaskCount = saveRouterPath.getItems().size() + totalTaskCount;
        }

        try {

            routerPathClould.save();

            i++;

            publishProgress(i * 100 / totalTaskCount, i, totalTaskCount);

            saveRouterPath.setObjID(routerPathClould.getObjectId());

            for (RouterPathItem routerPathItem : saveRouterPath.getItems()) {
                AVObject routerPathItemClould = MyApplication.getInstance().getCloudDb().convertRouterPathItemToClould(routerPathItem);

                try {
                    routerPathItemClould.save();
                } catch (AVException e) {
                    e.printStackTrace();
                    Log.e(TAG, e.getMessage());
                }

                i++;

                publishProgress(i * 100 / totalTaskCount, i, totalTaskCount);


            }

            Log.i(TAG, routerPathClould.getObjectId() + "-----");


        } catch (AVException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            Log.e(TAG, e.getMessage());

        }

        return saveRouterPath;

    }

    @Override
    protected void onCancelled(RouterPath s) {
        super.onCancelled(s);
        MyApplication.getInstance().showShortToastMessage("数据保存任务取消！");
    }

    @Override
    protected void onPostExecute(RouterPath s) {
        super.onPostExecute(s);
        if (!StringUtil.isNullOrEmpty(s.getObjID())) {
            MyApplication.getInstance().showShortToastMessage("数据保存成功！数据ID:" + s.getObjID());
        } else {
            MyApplication.getInstance().showShortToastMessage("数据保存失败：" + errorMessage);
        }
    }
}
