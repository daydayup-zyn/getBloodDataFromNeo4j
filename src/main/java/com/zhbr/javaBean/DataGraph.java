package com.zhbr.javaBean;

import org.omg.CORBA.OBJ_ADAPTER;

import java.util.Arrays;

/**
 * @ClassName DataGraph
 * @Description TODO
 * @Autor yanni
 * @Date 2020/7/10 10:11
 * @Version 1.0
 **/
public class DataGraph {

    private Object[] nodes;
    private Object[] relationships;

    public Object[] getNodes() {
        return nodes;
    }

    public void setNodes(Object[] nodes) {
        this.nodes = nodes;
    }

    public Object[] getRelationships() {
        return relationships;
    }

    public void setRelationships(Object[] relationships) {
        this.relationships = relationships;
    }
}
