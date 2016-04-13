package com.srmn.xwork.gpstoolkit.Dao;

import com.srmn.xwork.androidlib.db.IDaoContainer;

import org.xutils.DbManager;

/**
 * Created by kiler on 2016/2/20.
 */
public class DaoContainer implements IDaoContainer {

    private DbManager _db;

    private RouterPathItemDao routerPathItemDaoInstance;
    private RouterPathDao routerPathDaoInstance;
    private MarkerCategoryDao markerCategoryDaoInstance;
    private MarkerDao markerDaoInstance;

    public DaoContainer(DbManager db) {
        this._db = db;
        markerCategoryDaoInstance = new MarkerCategoryDao(this._db, this);
        markerDaoInstance = new MarkerDao(this._db, this);
        routerPathItemDaoInstance = new RouterPathItemDao(this._db, this);
        routerPathDaoInstance = new RouterPathDao(this._db, this);
    }

    public MarkerCategoryDao getMarkerCategoryDaoInstance() {
        return markerCategoryDaoInstance;
    }

    public MarkerDao getMarkerDaoInstance() {
        return markerDaoInstance;
    }

    public RouterPathDao getRouterPathDaoInstance() {
        return routerPathDaoInstance;
    }

    public RouterPathItemDao getRouterPathItemDaoInstance() {
        return routerPathItemDaoInstance;
    }


}
