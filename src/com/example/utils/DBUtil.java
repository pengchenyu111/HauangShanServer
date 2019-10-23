package com.example.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * mysql连接的jar包应该放在tomcat的lib目录下，否则tomcat会因为连接而引起的内存泄漏而报错
 * 但这样做，是否还会内存泄漏就不得而知了->的确还会内存泄漏！！！！
 *
 * 每一个页面请求导致一次数据库访问。连接数据库不仅要开销一定的通信和内存资源，还必须完成用户验证、安全上下文配置这类任务，因为往往成为最为耗时的操作。
 * 由连接池来管理数据库连接的建立和注销。连接池初始化时建立多条连接，供给不同的请求使用，多个请求可以共享同一连接，这样做，就可以减少打开、关闭数据库连接的操作，从而改善了系统的性能。
 *
 */

public class DBUtil {

    public static final String TABLE_USER = "table_user_password";
    public static final String TABLE_USERINFO = "table_user_info";

    private static final String URL = "jdbc:mysql://localhost:3306/first_mysql_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String USER = "root";
    private static final String PASSWORD = "PCY90321";
    //	创建连接池
    public static BasicDataSource ds = new BasicDataSource();
    //	配置连接池属性
    static {
        ds.setDriverClassName(DRIVER);
        ds.setUrl(URL);
        ds.setUsername(USER);
        ds.setPassword(PASSWORD);
        ds.setMaxTotal(20);//设置最大连接数
        ds.setInitialSize(10);//设置初始化连接数
        ds.setMaxIdle(8);//当空闲下载的时候，连接最大数量
        ds.setMinIdle(5);//当空闲时间过长后，变为最小空闲
    }



    //	连接数据库
    public static Connection getConnect() {
        Connection connecter = null;
        try {
            connecter = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connecter;

    }


    //	关闭连接
    public static void close(Connection conn, Statement stat,ResultSet rs) {
        if(rs != null ){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                rs = null;
            }
        }
        if(stat != null ){
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                stat = null;
            }
        }
        if(conn != null ){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }finally{
                conn = null;
            }
        }
    }

}
