package com.srmn.xwork.gpstoolkit.Entities;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kiler on 2016/2/20.
 */
@Table(name = "MarkerCategory")
public class MarkerCategory implements Serializable {

    @Column(name = "name")
    public String name;
    public List<Marker> markers;
    @Column(name = "id", isId = true)
    private Integer id;
    @Column(name = "objID")
    private String objID;

    public String getObjID() {
        return objID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }

    @Override
    public String toString() {
        return getName();
    }
}
