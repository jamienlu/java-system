package com.asura.cache.entity;

import lombok.Data;
import org.springframework.stereotype.Repository;

@Repository("roles")
@Data
public class Role {
    private String username;
    private String role;
}
