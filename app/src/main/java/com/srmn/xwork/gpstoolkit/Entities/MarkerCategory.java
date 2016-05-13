package com.srmn.xwork.gpstoolkit.Entities;

import com.srmn.xwork.androidlib.utils.IOUtil;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.File;
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


    public String getDescription() {
        StringBuilder sb = new StringBuilder();

        int totalCount = 0;

        int totalImageCount = 0;

        long totalSize = 0;

        for (Marker marker : this.getMarkers()) {
            totalCount++;
            totalImageCount += marker.getImagesList().size();

            for (String imagePath : marker.getImagesList()) {
                totalSize += new File(imagePath).length();
            }
        }


        sb.append("总计" + totalCount + "个标注 | 共" + totalImageCount + "张图片 | 总体积" + IOUtil.getDataSize(totalSize) + "");

        return sb.toString();
    }


    @Override
    public String toString() {
        return getName();
    }
}
