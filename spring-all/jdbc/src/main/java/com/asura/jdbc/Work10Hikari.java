package com.asura.jdbc;

import com.asura.jdbc.bean.Cell;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Work10Hikari {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testAddUser() {
        String sql = "select id,name,count from cell";
        List<Map<String,Object>> maps = jdbcTemplate.queryForList(sql);
        List<Cell> cellList = maps.stream().map(bo -> {
            Cell cell = new Cell();
            cell.setId((int)bo.get("id"));
            cell.setName((String)bo.get("name"));
            cell.setCount((int)bo.get("count"));
            return cell;
        }).collect(Collectors.toList());
        System.out.println(cellList);
    }
}
