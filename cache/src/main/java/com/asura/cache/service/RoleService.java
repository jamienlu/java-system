package com.asura.cache.service;

import com.asura.cache.entity.Role;
import org.springframework.cache.annotation.CacheConfig;

import java.util.List;

@CacheConfig(cacheNames = "roles")
public interface RoleService {
    void insert(Role role);
    List<Role> selectAll();
    void consumer();
}

