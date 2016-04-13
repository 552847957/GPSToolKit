package com.srmn.xwork.gpstoolkit.Dao;

import com.srmn.xwork.androidlib.db.BaseDao;
import com.srmn.xwork.gpstoolkit.Entities.MarkerCategory;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/2/20.
 */
public class MarkerCategoryDao extends BaseDao<MarkerCategory> {

    public MarkerCategoryDao(DbManager db, DaoContainer daoContainer) {
        super(db, daoContainer);
    }

    public DaoContainer getDaoContainerInstance() {
        return (DaoContainer) this.dao;
    }

    @Override
    public String getPkName() {
        return "id";
    }

    public int getNewId() {

        try {
            MarkerCategory markerCategory = (MarkerCategory) getSelector().orderBy(getPkName(), true).findFirst();

            if (markerCategory == null)
                return 1;


            return markerCategory.getId() + 1;


        } catch (DbException e) {
            e.printStackTrace();
            return 1;

        }
    }

    public List<MarkerCategory> findAllFullMarkerCategory() {
        List<MarkerCategory> entities = findAll();

        if (entities == null) {
            entities = new ArrayList<MarkerCategory>();
        }

        for (MarkerCategory markerCategory : entities) {
            markerCategory.setMarkers(getDaoContainerInstance().getMarkerDaoInstance().queryByCategoryID(markerCategory.getId()));
        }

        return entities;
    }

    @Override
    protected void getClassType() {
        entityClass = MarkerCategory.class;
    }
}
