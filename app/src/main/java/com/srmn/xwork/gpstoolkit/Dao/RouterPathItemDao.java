package com.srmn.xwork.gpstoolkit.Dao;

import com.srmn.xwork.androidlib.db.BaseDao;
import com.srmn.xwork.gpstoolkit.Entities.RouterPathItem;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/3/10.
 */
public class RouterPathItemDao extends BaseDao<RouterPathItem> {
    public RouterPathItemDao(DbManager db, DaoContainer daoContainer) {
        super(db, daoContainer);
    }

    @Override
    public String getPkName() {
        return "id";
    }

    @Override
    protected void getClassType() {
        entityClass = RouterPathItem.class;
    }

    public List<RouterPathItem> findAllByRouterPathId(Integer routerPathId) {
        List<RouterPathItem> lst = new ArrayList<RouterPathItem>();

        try {
            lst = getSelector().where("routerPathid", "=", routerPathId).orderBy("id", false).findAll();
        } catch (DbException ex) {
            ex.printStackTrace();
        }

        return lst;
    }
}
