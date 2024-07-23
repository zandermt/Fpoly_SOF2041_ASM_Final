/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.poly.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Zander
 */
public class JdbcHelper {
    static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    static String dburl = "jdbc:sqlserver://localhost:1433;databaseName=QLHocVien;encrypt=true;trustServerCertificate=true;";
    static String user = "adminQLHocVien";
    static String pass = "123456";
  
    //nạp driver
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static PreparedStatement getStmt(String sql, Object... args) throws SQLException {
        Connection conn = DriverManager.getConnection(dburl, user, pass);
        PreparedStatement pstmt = null;
        if (sql.trim().startsWith("{")) {
            pstmt = conn.prepareCall(sql); //proc
        } else {
            pstmt = conn.prepareStatement(sql); //SQL
        }
        for (int i = 0; i < args.length; i++) {
            pstmt.setObject(i + 1, args[i]);
        }
        return pstmt;
    }
    
    
    public static int executeUpdate(String sql, Object... args) {
        try {
            PreparedStatement pstmt = getStmt(sql, args);
            try {
                 return pstmt.executeUpdate();
            } finally {
                pstmt.getConnection().close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
     public static ResultSet executeQuery(String sql, Object... args) {
        try {
            PreparedStatement pstmt = getStmt(sql, args);
            try {
                return pstmt.executeQuery();
            } finally {

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
     
    public static Object value(String sql, Object... args){
        try{
            ResultSet rs = executeQuery(sql,args);
            if(rs.next()){
                return rs.getObject(0);
        }
        rs.getStatement().getConnection().close();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return null;
    }
}
