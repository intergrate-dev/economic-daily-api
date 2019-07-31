package com.founder.econdaily.modules.auth.service;

import com.founder.econdaily.modules.auth.entity.User;

import java.util.List;

public interface UserService {

    void insertByBatch(List<User> users) throws Exception;
}
