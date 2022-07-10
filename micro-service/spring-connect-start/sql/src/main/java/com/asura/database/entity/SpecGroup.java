package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class SpecGroup {
    private int id;
    private int cid;
    private String name;
}
