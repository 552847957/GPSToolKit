package com.srmn.xwork.gpstoolkit.Dao;

import com.srmn.xwork.androidlib.db.BaseDao;
import com.srmn.xwork.gpstoolkit.Entities.RouterPath;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

/**
 * Created by kiler on 2016/3/10.
 */
public class RouterPathDao extends BaseDao<RouterPath> {

    public RouterPathDao(DbManager db, DaoContainer daoContainer) {
        super(db, daoContainer);
    }

    @Override
    public String getPkName() {
        return "id";
    }

    @Override
    protected void getClassType() {
        entityClass = RouterPath.class;
    }

    public boolean existByCode(String code) {
        RouterPath e = findByCode(code);

        if (e == null) {
            return false;
        } else {
            return true;
        }
    }

    public RouterPath findByCode(String code) {
        return findFirstByWhere("code", "=", code);
    }


    public void updateObjectID(RouterPath entity, String objID) throws DbException {
        entity.setObjID(objID);
        this._db.update(entity, "objID");
    }
}
