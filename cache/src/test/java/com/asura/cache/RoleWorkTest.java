package com.asura.cache;

import com.alibaba.fastjson.JSONObject;
import com.asura.cache.entity.Role;
import com.asura.cache.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleWorkTest {
    @Autowired
    private RoleService roleService;
    @Test
    public void testInsert() {
        Role role = new Role();
        role.setUsername("asura");
        role.setRole("admin");
        IntStream.range(0,10).parallel().forEach( x -> roleService.insert(role));
    }

    @Test
    public void testSelect() {
        IntStream.range(0,10).parallel().forEach( x ->  System.out.println(JSONObject.toJSONString(roleService.selectAll())));
    }

    @Test
    public void consumer() {
        IntStream.range(0,100).parallel().forEach( x ->  roleService.consumer());
    }
}
