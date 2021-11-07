package com.asura.database;

import com.asura.database.util.CollectionUtil;
import com.asura.database.util.TimeUtil;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Work1Test {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @After
    public void clear() throws SQLException {
        String sql = "TRUNCATE table asura_user";
        jdbcTemplate.update(sql);
    }

    @Test
    /**
     * 一句一句插入大约100min
     */
    public void AddUser() {
        String sql = "insert into asura_user (name,create_time,update_time,phone,card_id,real_name,statue) values(?,?,?,?,?,?,?)";
        long time1 = System.currentTimeMillis();
        IntStream.range(1,10000).forEach(serNo -> {
            Object[] obj = new Object[]{"name" + serNo, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    "18908222656", "513623199310189966","asura", true};
            jdbcTemplate.update(sql, obj);
        });
        System.out.println("AddUser cost time:" + (System.currentTimeMillis() - time1)/1000);
    }

    @Test
    /**
     * 批量插入对象26s (实验得出jdbcTemplate batchUpdate传入预编译参数后实现一致)
     */
    public void addUserBatch() {
        String sql = "insert into asura_user (name,create_time,update_time,phone,card_id,real_name,statue) values(?,?,?,?,?,?,?)";
        long time1 = System.currentTimeMillis();
        List<Object[]> batchObjects = new ArrayList<>();
        IntStream.range(1,1000000).forEach(serNo -> {
            Object[] obj = new Object[]{"name" + serNo, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()),
                    "18908222656", "513623199310189966","asura", true};
            batchObjects.add(obj);
        });
        jdbcTemplate.batchUpdate(sql, batchObjects);
        System.out.println("addUserBatch cost time:" + (System.currentTimeMillis() - time1)/1000);
    }

    @Test
    /**
     * 批量插入对象preparedstatement 26s
     */
    public void addUserBatchPrepare() {
        String sql = "insert into asura_user (id, name,create_time,update_time,phone,card_id,real_name,statue) values(?,?,?,?,?,?,?,?)";
        long time1 = System.currentTimeMillis();
        List<Object[]> batchObjects = new ArrayList<>();
        IntStream.range(1,1000000).forEach(serNo -> {
            Object[] obj = new Object[]{serNo, "name" + serNo, System.currentTimeMillis(), System.currentTimeMillis(),
                    "18908222656", "513623199310189966","asura", true};
            batchObjects.add(obj);
        });
        List<List<Object[]>> strLists = CollectionUtil.splitCollections(batchObjects, 50000);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, (int) batchObjects.get(i)[0]);
                preparedStatement.setString(2,  (String) batchObjects.get(i)[1]);
                preparedStatement.setDate(3, new java.sql.Date((long)batchObjects.get(i)[2]));
                preparedStatement.setDate(4, new java.sql.Date((long)batchObjects.get(i)[3]));
                preparedStatement.setString(5, (String) batchObjects.get(i)[4]);
                preparedStatement.setString(6, (String) batchObjects.get(i)[5]);
                preparedStatement.setString(7, (String) batchObjects.get(i)[6]);
                preparedStatement.setBoolean(8, (boolean) batchObjects.get(i)[7]);
            }
            @Override
            public int getBatchSize() {
                return batchObjects.size();
            }
        });
        System.out.println("addUserBatchPrepare cost time:" + (System.currentTimeMillis() - time1)/1000);
    }

    @Test
    /**
     * sql为批量添加sql一次插入1W执行50次 6核并行插入耗时6s 总共耗时14s
     */
    public void addUserBatchAndMatch() throws InterruptedException {
        String sql = "insert into asura_user (name,create_time,update_time,phone,card_id,real_name,statue) values ";
        ExecutorService executorService = Executors.newFixedThreadPool(12);
        long time1 = System.currentTimeMillis();
        List<String> statements = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            StringBuilder statement = new StringBuilder("(");
            statement.append("'name" + i + "'").append(",")
                    .append("'" + TimeUtil.parseStr(System.currentTimeMillis(),"")+ "'").append(",")
                    .append("'" +TimeUtil.parseStr(System.currentTimeMillis(),"")+ "'").append(",")
                    .append("'" +"18908222656"+ "'").append(",")
                    .append("'" +"513623199310189966"+ "'").append(",")
                    .append("'" +"asura"+ "'").append(",")
                    .append("true").append(")");
            statements.add(statement.toString());
        }
        long time2 = System.currentTimeMillis();
        List<List<String>> strLists = CollectionUtil.splitCollections(statements, 50000);
        List<Future> futures = new ArrayList<>();
        strLists.forEach(list -> {
            futures.add(executorService.submit(() -> jdbcTemplate.execute(sql + String.join(",",list))));
        });
        futures.forEach(x -> {
            try {
                x.get(3500, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        System.out.println("addUserBatch cost time:" + (System.currentTimeMillis() - time1) / 1000);
        System.out.println("addUserBatch execute cost time:" + (System.currentTimeMillis() - time2) / 1000);
    }

    private static Connection createConn(String url, String username, String password) throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    /**
     * 异步执行 遍历一条数据加载进sql进行批处理 批处理达到预定规模异步执行sql,数据遍历和插入同时进行，遍历后sql耗时2s 总共耗时7s
     *
     * @param args
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8&autoReconnect=true&rewriteBatchedStatements=true&useSSL=true&serverTimezone=UTC";
        String username = "root";
        String password = "hello_123456789";
        String preSql = "insert into asura_user (name,create_time,update_time,phone,card_id,real_name,statue) values ";
        int index = 0;
        long time1 = System.currentTimeMillis();
        List<Connection> connections =  IntStream.range(0, 10).mapToObj(num -> {
                    try {
                        return createConn(url, username, password);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    return null;
                }
        ).collect(Collectors.toList());
        List<PreparedStatement> statements = new ArrayList<>(connections.size());
        long time2 = System.currentTimeMillis();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future> futures = new ArrayList<>();
        StringBuilder sqlBuider = new StringBuilder(preSql);
        int batch = 0;
        for (int i = 1; i <= 1000000; i++) {
            sqlBuider.append("('name" + i + "'").append(",")
                    .append("'" + TimeUtil.parseStr(System.currentTimeMillis(),"")+ "'").append(",")
                    .append("'" +TimeUtil.parseStr(System.currentTimeMillis(),"")+ "'").append(",")
                    .append("'" +"18908222656"+ "'").append(",")
                    .append("'" +"513623199310189966"+ "'").append(",")
                    .append("'" +"asura"+ "'").append(",")
                    .append("true").append("),");
            if (i % 10000 == 0 && i != 0) {
                try {
                    if (batch == 0) {
                        Connection connection = connections.get(index);
                        connection.setAutoCommit(false);
                        PreparedStatement statement = connection.prepareStatement(sqlBuider.deleteCharAt(sqlBuider.length() - 1).toString());
                        statement.addBatch();
                        statements.add(statement);
                    } else {
                        statements.get(index).addBatch(sqlBuider.deleteCharAt(sqlBuider.length() - 1).toString());
                    }
                    batch++;
                    sqlBuider = new StringBuilder(preSql);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (batch == 10) {
                PreparedStatement statement = statements.get(index);
                Connection connection = connections.get(index);
                futures.add(executorService.submit(() -> {
                    try {
                        statement.executeBatch();
                        connection.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }));

                index++;
                batch = 0;
            }
        }
        System.out.println("addUserBatch get connect cost time:" + (time2 - time1) + "ms");
        System.out.println("addUserBatch foreach cost time:" + (System.currentTimeMillis() - time2) + "ms");
        long time3 = System.currentTimeMillis();
        futures.forEach(x -> {
            try {
               x.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executorService.shutdown();
        System.out.println("addUserBatch execute sql cost time:" + (System.currentTimeMillis() - time3) + "ms");
        System.out.println("addUserBatch execute all cost time:" + (System.currentTimeMillis() - time2)  + "ms");
    }

}
