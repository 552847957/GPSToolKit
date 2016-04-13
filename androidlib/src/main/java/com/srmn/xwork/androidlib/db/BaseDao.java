package com.srmn.xwork.androidlib.db;


import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiler on 2016/2/20.
 */
public abstract class BaseDao<E> {

    protected Class<E> entityClass;
    protected DbManager _db;
    protected IDaoContainer dao;


    public BaseDao(DbManager db, IDaoContainer daoContainer) {
        this._db = db;
        this.dao = daoContainer;
        getClassType();
    }

    public abstract String getPkName();

    protected abstract void getClassType();


    public void save(E entity) throws DbException {
        this._db.save(entity);
    }

    public void update(E entity) throws DbException {
        this._db.update(entity);
    }

    public void saveOrUpdate(E entity) throws DbException {
        this._db.saveOrUpdate(entity);
    }

    public void saveBindingId(E entity) throws DbException {
        this._db.saveBindingId(entity);
    }

    public void delete(E entity) throws DbException {
        this._db.delete(entity);
    }

    public Selector<E> getSelector() throws DbException {
        return this._db.selector(entityClass);
    }


    public List<E> findAll() {
        List<E> lst = new ArrayList<E>();

        try {
            lst = getSelector().findAll();
        } catch (DbException ex) {
            ex.printStackTrace();
        }

        return lst;
    }

    public E findById(int id) {
        E entity = null;
        try {
            entity = getSelector().where(getPkName(), "=", id).findFirst();
        } catch (DbException ex) {
            ex.printStackTrace();
            return null;
        }
        return entity;
    }

    public boolean checkIDIsExisted(int id) {
        E e = findById(id);

        if (e == null) {
            return false;
        } else {
            return true;
        }
    }


}
