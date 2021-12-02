package com.asura.cache.mapper;

import com.asura.cache.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleDao {
    void save(Role role);
    List<Role> selectAll();

}
