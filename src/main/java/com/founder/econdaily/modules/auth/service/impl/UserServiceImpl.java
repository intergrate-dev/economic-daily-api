package com.founder.econdaily.modules.auth.service.impl;

import com.founder.econdaily.modules.auth.entity.User;
import com.founder.econdaily.modules.auth.service.UserService;
import com.mysql.jdbc.PreparedStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insertByBatch(List<User> users) throws Exception{
        final List<User> tempBpplist = users;
        String sql = "insert into user(id, username, password, description) " +
                "values(null, ?, ?, ?) ";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(java.sql.PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, tempBpplist.get(i).getUsername());
                ps.setString(2, tempBpplist.get(i).getPassword());
                ps.setString(3, tempBpplist.get(i).getDescription());
            }

            @Override
            public int getBatchSize() {
                return tempBpplist.size();
            }

        });
    }
}
