package com.asura.dubboconsumer.test;

import com.asura.exchangemoney.api.UserAccount;
import com.asura.exchangemoney.api.UserService;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class Test {

    @DubboReference(version = "1.0.0")
    private UserService userService;

    public void test() {
        String uid = UUID.randomUUID().toString();
        List<UserAccount> userAccounts = userService.exchangeCurrency(uid, 2,0,1,100);
        log.info("userAccounts1:" + userAccounts);
        List<UserAccount> userAccounts2 = userService.exchangeCurrency(uid, 3,1,0,700);
        log.info("userAccounts2:" + userAccounts2);
    }

}
