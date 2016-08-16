package org.springframework.jotdown.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jotdown.dao.entity.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByUsernameAndDeleteFlagFalse(String username);

    UserInfo findByUsernameAndPassword(String username, String password);

}
