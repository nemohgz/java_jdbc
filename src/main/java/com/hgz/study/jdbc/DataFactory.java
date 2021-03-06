package com.hgz.study.jdbc;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**
 * @Author: HGZ
 * @Date: 2017-12-28
 * @Time: 14:47
 * @Description:
 * @Modified by:
 */
public class DataFactory {
    private static final String PROPERTIES = "mysql.properties";
    private String jdbcDriver =  "";
    private String dbURL      =  "";
    private String userName   =  "";
    private String passWord   =  "";
    private Connection conn;
    public DataFactory() {
        readConfig();
        init(jdbcDriver, dbURL, userName, passWord);
    }
    public  DataFactory(String jdbcDriver, String dbURL, String userName, String userPwd) {
        init(jdbcDriver, dbURL, userName, userPwd);
    }
    public void init(String jdbcDriver, String dbURL, String userName, String userPwd) {
        conn = null;
        try {
            Class.forName(jdbcDriver);
            conn = DriverManager.getConnection(dbURL,userName,userPwd);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("cannot connect " + dbURL);
        }
    }
    public void test() {
        readDbTest();
        writeDbTest();
    }
    private void readDbTest() {
        if (conn == null) {
            System.out.println("no connect");
            return;
        }
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT employeeNumber,  lastName, firstName FROM employees" +
                         " WHERE officeCode=5";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                //Retrieve by column name
                int employeeNumber  = rs.getInt("employeeNumber");
                String lastName = rs.getString("lastName");
                String firstName = rs.getString("firstName");
                //Display values
                System.out.print("employeeNumber: " + employeeNumber);
                System.out.print(", lastName: " + lastName);
                System.out.println(", firstName: " + firstName);
            }
            System.out.println("readDbTest passed");
            rs.close();
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println("readDbTest occurs errors");
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }
    private void writeDbTest() {
        if (conn == null) {
            System.out.println("no connect");
            return;
        }

    }
    public void close() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    private void readConfig() {
        try {
            Configuration config = new PropertiesConfiguration(PROPERTIES);
            jdbcDriver = config.getString("jdbc.driver");
            dbURL = config.getString("jbdc.db.url");
            userName = config.getString("jdbc.db.username");
            passWord = config.getString("jdbc.db.password");
        } catch (ConfigurationException ce) {
            ce.printStackTrace();
        }
    }
}
