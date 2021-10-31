package com.asura.springconnectstart.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class Item {
    private String itemId;
    private String name;
    private String type;
    private double price;
}
