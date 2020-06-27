package com.example.model;

import java.util.ArrayList;
import java.util.List;

import java.sql.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class DatabaseConnection {

    private final String mysql_url ="jdbc:mysql://127.0.0.1:3306/javaAPI_schema";
    private final String userName = "root";
    private final String tableName = "info_table";
    //String password = "123";//密码
    private Connection conn = null;

    public Boolean establishConnection() throws Exception {
        try {
            //加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            //得到连接
    
            // 获取数据库连接
            conn = DriverManager.getConnection(mysql_url, userName /*, password */);            if(!conn.isClosed())
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("noooooo!!!");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Boolean insertIntoDatabase(V1Service v1service) throws Exception {
        Statement statement=conn.createStatement();
        String sql="insert into " + tableName + "(service_name, ip_addr, service_namespace)";
        // String uuid=getUUID();

        sql += "values('"
            + v1Service.getMetadata().getName() + "','"
            + v1Service.getSpec().getClusterIP() + "','";
            + v1Service.getMetadata().getNamespace() + "'";
            + ")";

        int result=stmt.executeUpdate(sql);
        System.out.println(result);

        statement.close();
    }

    public Boolean closeConnection()
    {
        try {
            if(conn != null)
            {
                conn.close();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return false;
        }

        return true;
    }

}