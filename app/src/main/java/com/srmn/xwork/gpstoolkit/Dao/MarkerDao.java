package com.srmn.xwork.gpstoolkit.Dao;

import com.srmn.xwork.androidlib.db.BaseDao;
import com.srmn.xwork.gpstoolkit.Entities.Marker;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/2/20.
 */
public class MarkerDao extends BaseDao<Marker> {

    public MarkerDao(DbManager db, DaoContainer daoContainer) {
        super(db, daoContainer);
    }

    @Override
    public String getPkName() {
        return "id";
    }

    public List<Marker> queryByCategoryID(int categoryID) {
        List<Marker> entities = new ArrayList<Marker>();
        try {
            entities = getSelector().where("markerCategoryID", "=", categoryID).findAll();
        } catch (DbException ex) {
            ex.printStackTrace();
            return null;
        }
        return entities;
    }

    @Override
    protected void getClassType() {
        entityClass = Marker.class;
    }

}
