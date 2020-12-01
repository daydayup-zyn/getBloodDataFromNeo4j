package com.zhbr.neo4jDataJson;

/**
 * @ClassName Neo4jDataJson
 * @Description TODO
 * @Autor yanni
 * @Date 2020/7/10 9:17
 * @Version 1.0
 **/
public class Neo4jDataJson {

    public static void main(String[] args) {

        ConfigNeo4jDBUtil configNeo4jDBUtil = new ConfigNeo4jDBUtil();
        String yj_experience_exchange = configNeo4jDBUtil.getGraphJson("供电电压采集系统","dyt_docsyncdetail");

        System.out.println(yj_experience_exchange);
    }
}
