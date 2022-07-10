package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class ItemInfo {
    private String itemId;
    private int stocks;
    private String produceAddress;
    private long produceTime;
    private long validTime;
}
