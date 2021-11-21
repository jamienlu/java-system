package com.asura.exchangemoney.api;

import java.util.List;

public interface UserService {
    List<UserAccount> exchangeCurrency(String uuid, int userId, int srcType, int targetType, int num);
}
