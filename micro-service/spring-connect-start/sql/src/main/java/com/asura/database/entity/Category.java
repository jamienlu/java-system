package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class Category {
    private int id;
    private String name;
    private int parentId;
    private boolean isParent;
    private int sort;
}
