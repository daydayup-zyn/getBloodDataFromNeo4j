package com.zhbr.javaBean;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName DataVertex
 * @Description TODO
 * @Autor yanni
 * @Date 2020/7/10 9:39
 * @Version 1.0
 **/
public class DataVertex {

    private String id;
    private String[] labels;
    private VertextTable properties;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public VertextTable getProperties() {
        return properties;
    }

    public void setProperties(VertextTable properties) {
        this.properties = properties;
    }
}
