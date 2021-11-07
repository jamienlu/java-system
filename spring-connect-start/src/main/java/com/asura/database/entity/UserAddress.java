package com.asura.database.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

@Repository
@Getter
@Setter
public class UserAddress {
    private String userId;
    private String country;
    private String province;
    private String city;
    private String area;
}
