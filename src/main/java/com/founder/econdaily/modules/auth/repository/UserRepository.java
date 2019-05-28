package com.founder.econdaily.modules.auth.repository;

import com.founder.econdaily.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUsername(String username);
}
