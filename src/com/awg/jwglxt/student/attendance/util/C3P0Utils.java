package com.awg.jwglxt.student.attendance.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * C3P0工具类
 * @author AWG
 * 
 */
public class C3P0Utils {

    //定义C3P0连接池对象
    private static ComboPooledDataSource ds;

    //在静态代码块中创建连接池
    static {
        //使用命名配置创建
        ds = new ComboPooledDataSource("MySQL");
    }

    /**
      * 获取连接
     */
    public static Connection getConn() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 关闭连接
     * @param rs 查询结果集对象
     * @param psta 预编译语句对象
     * @param conn 数据库连接对象 ( 在连接池中使用close()方法,是将连接放入连接池中 )
     */
    public static void closeAll(ResultSet rs, PreparedStatement psmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (psmt != null) {
            try {
                psmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
