package com.zhbr.neo4jDataJson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhbr.javaBean.DataEdge;
import com.zhbr.javaBean.DataGraph;
import com.zhbr.javaBean.DataVertex;
import com.zhbr.javaBean.VertextTable;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static javafx.scene.input.KeyCode.K;

/**
 * @ClassName ConfigNeo4jDBUtil
 * @Description TODO
 * @Autor yanni
 * @Date 2020/3/6 10:49
 * @Version 1.0
 **/
public class ConfigNeo4jDBUtil {

    private Connection conn = null;

    private Connection getNeo4jConnection() throws SQLException {
        System.out.println("----------------------1111------------------------");
        conn = DriverManager.getConnection("jdbc:neo4j:bolt://192.168.72.143:7687/","neo4j","1a2b3c4d");
        System.out.println("-----------------------222---------------------------");
        return conn;
    }

    public String getGraphJson(String ywxt,String tableName) {
        StringBuffer stringBuffer = new StringBuffer();
        try{
            conn = getNeo4jConnection();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery("match (n)-[r*1..5]->(m{ywxtmc:'"+ywxt+"',tableName:'"+tableName+"'}) return n,r,m");

            DataGraph dataGraph = new DataGraph();
            Set<DataVertex> dataVertices = new HashSet<DataVertex>();
            Set<DataEdge> dataEdge = new HashSet<DataEdge>();
            while (resultSet.next()){
                HashMap source = (HashMap)resultSet.getObject(1);
                DataVertex sourceJson = vertextDataToJson(source);
                dataVertices.add(sourceJson);
                HashMap target = (HashMap)resultSet.getObject(3);
                DataVertex targetJson = vertextDataToJson(target);
                dataVertices.add(targetJson);
                String relationship = resultSet.getObject(2).toString();
                ArrayList<DataEdge> dataEdges = strToEdge(relationship);
                for (int i=0;i<dataEdges.size();i++){
                    DataEdge dataEdge1 = dataEdges.get(i);
                    dataEdge.add(dataEdge1);
                }
                //DataEdge edges = edgeDataToJson((List<Map>) resultSet.getObject(2));
                //dataEdge.add(edges);
            }

            Object[] verticesArr = dataVertices.toArray();
            Object[] edgesArr = dataEdge.toArray();

            dataGraph.setNodes(verticesArr);
            dataGraph.setRelationships(edgesArr);

            String dataGraphStr = JSON.toJSONString(dataGraph);

            stringBuffer = new StringBuffer();
            stringBuffer.append("{\n" +
                    "    \"results\": [\n" +
                    "        {\n" +
                    "            \"data\": [\n" +
                    "                {\n" +
                    "                    \"graph\":").append(dataGraphStr).append("                }\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}");

            resultSet.close();
            statement.close();
            conn.close();

            return stringBuffer.toString();

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private DataVertex vertextDataToJson(HashMap hashMap){
        String jsonString = JSON.toJSONString(hashMap);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        String id = jsonObject.getString("_id");
        JSONArray labels = jsonObject.getJSONArray("_labels");
        String labelStr = labels.getString(0);
        String dbName = jsonObject.getString("dbName");
        String tableName = jsonObject.getString("tableName");
        String tid = jsonObject.getString("tid");
        String ywxtmc = jsonObject.getString("ywxtmc");

        VertextTable vertextTable = new VertextTable();
        vertextTable.setTid(tid);
        vertextTable.setDbName(dbName);
        vertextTable.setTableName(tableName);
        vertextTable.setYwxtmc(ywxtmc);

        DataVertex dataVertex = new DataVertex();
        dataVertex.setId(id);
        String[] labelArr = {labelStr};
        dataVertex.setLabels(labelArr);
        dataVertex.setProperties(vertextTable);

        //String vertexJsonStr = JSON.toJSONString(dataVertex);

        return dataVertex;
    }

    private DataEdge edgeDataToJson(HashMap hashMap){
        DataEdge dataEdge = new DataEdge();
            String jsonString = JSON.toJSONString(hashMap);
            //System.out.println(jsonString);
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            String id = jsonObject.getString("_id");
            String type = jsonObject.getString("_type");
            String startId = jsonObject.getString("_startId");
            String endId = jsonObject.getString("_endId");
            String sourceTableName = jsonObject.getString("sourceTableName");
            String sourceColumn = jsonObject.getString("sourceColumn");
            String targetTableName = jsonObject.getString("targetTableName");
            String targetColumn = jsonObject.getString("targetColumn");


            dataEdge.setId(id);
            dataEdge.setType(type);
            dataEdge.setStartNode(startId);
            dataEdge.setEndNode(endId);
            dataEdge.setSourceTableName(sourceTableName);
            dataEdge.setSourceColumn(sourceColumn);
            dataEdge.setTargetTableName(targetTableName);
            dataEdge.setTargetColumn(targetColumn);

        //String edgeJsonStr = JSON.toJSONString(dataEdge);

        return dataEdge;
    }

    private ArrayList<DataEdge> strToEdge(String relationships) throws SQLException {
        ArrayList<DataEdge> list = new ArrayList<>();
        String relationship = relationships.replaceAll("relationship", "")
                .replaceAll("<", "")
                .replaceAll(">", "")
                .replaceAll("\\[", "")
                .replaceAll("]", "");
        String[] split = relationship.split(",");
        for (int i=0;i<split.length;i++){
            String s = split[i];
            //System.out.println(s);
            DataEdge relationship1 = getRelationship(s);
            list.add(relationship1);
        }
        return list;
    }

    public DataEdge getRelationship(String id) throws SQLException {
        DataEdge dataEdge = null;
//        Connection conn = getNeo4jConnection();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("match (n)-[r]-(m) where id(r)="+id+" return n,r,m");

        while (resultSet.next()){
            HashMap target = (HashMap)resultSet.getObject(2);
            dataEdge = edgeDataToJson(target);
        }
        return dataEdge;
    }
}
