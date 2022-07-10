package com.asura.jdbc;

import com.asura.jdbc.bean.Cell;
import com.asura.jdbc.mysql.PoolsUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Work10 {
    public static void main(String[] args) throws SQLException {
        PoolsUtil poolsUtil = PoolsUtil.getInstance();
        Connection connection = poolsUtil.getConnection();
        test1(connection);
        test2(connection);
        test3(connection);
        test4(connection);
        System.out.println(test5(connection));
    }

    public static void test1(Connection connection) throws SQLException {
        String sql = "DROP TABLE IF EXISTS `cell`";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
        }
    }

    public static void test2(Connection connection) throws SQLException {
        String sql = "CREATE TABLE `cell`  (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,\n" +
                "  `count` int(11) NULL DEFAULT NULL,\n" +
                "  PRIMARY KEY (`id`) USING BTREE\n" +
                ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic";
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
        }
    }

    public static void test3(Connection connection) throws SQLException {
        String sql = "insert into cell(name,count) values('name',0) ";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            statement.close();
        }
    }

    public static void test4(Connection connection) throws SQLException {
        String sql = "insert into cell(name,count) values(?,?) ";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, "name1");
            preparedStatement.setInt(2, 10);
            preparedStatement.addBatch();
            preparedStatement.setString(1, "name2");
            preparedStatement.setInt(2, 100);
            preparedStatement.addBatch();
            preparedStatement.setString(1, "name3");
            preparedStatement.setInt(2, 1000);
            preparedStatement.addBatch();
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            preparedStatement.close();
        }
    }

    public static List<Cell> test5(Connection connection) throws SQLException {
        List<Cell> cellList = new ArrayList<>();
        String sql = "select * from cell";
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Cell cell = new Cell();
                cell.setId( resultSet.getInt("id"));
                cell.setName( resultSet.getString("name"));
                cell.setCount( resultSet.getInt("count"));
                cellList.add(cell);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            preparedStatement.close();
        }
        return cellList;
    }
}
