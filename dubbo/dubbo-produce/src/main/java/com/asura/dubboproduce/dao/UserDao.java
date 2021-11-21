package com.asura.dubboproduce.dao;

import com.asura.exchangemoney.api.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int checkUserAccountNum(int userId, int currencyType) {
        String sql = "select sum(num) from asura_exchange_user_account where user_id =? and currency_type = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{userId, currencyType});
    }

    public int insertUserAccount(int userId, int currencyType, int num) {
        String sql = "insert into asura_exchange_user_account(user_id,currency_type,num,start_time) values(?,?,?,?)";
        return jdbcTemplate.update(sql, new Object[]{userId, currencyType, num, new Date()});
    }

    public int selectRate(int currencyType) {
        String sql = "select rate from asura_exchange_account where type = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{currencyType});
    }

    public List<UserAccount> queryUserAccounts(int userId) {
        String sql = "select user_id, currency_type, sum(num) as num from asura_exchange_user_account where user_id = ? group by user_id,currency_type";
        List<UserAccount> userAccounts = new ArrayList<>();
        jdbcTemplate.queryForList(sql, new Object[]{userId}).forEach(x -> {
            UserAccount userAccount = new UserAccount();
            userAccount.setUserId(new BigDecimal((x.get("user_id").toString())).longValue());
            userAccount.setNum(new BigDecimal(x.get("num").toString()).floatValue());
            userAccount.setCurrencyType((int)x.get("currency_type"));
            userAccounts.add(userAccount);
        });
        return userAccounts;
    }
}
