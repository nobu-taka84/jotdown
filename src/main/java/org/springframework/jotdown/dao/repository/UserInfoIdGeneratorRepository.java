package org.springframework.jotdown.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jotdown.dao.entity.UserInfoIdGenerator;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoIdGeneratorRepository extends JpaRepository<UserInfoIdGenerator, String> {

}
