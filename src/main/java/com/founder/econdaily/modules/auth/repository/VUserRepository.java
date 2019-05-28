package com.founder.econdaily.modules.auth.repository;

import com.founder.econdaily.modules.auth.entity.VUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VUserRepository extends JpaRepository<VUserEntity, Integer> {
	VUserEntity findByUname(String username);
}
