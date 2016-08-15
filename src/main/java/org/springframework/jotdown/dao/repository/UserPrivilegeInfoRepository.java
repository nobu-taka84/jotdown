package org.springframework.jotdown.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jotdown.dao.entity.UserPrivilegeInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrivilegeInfoRepository extends JpaRepository<UserPrivilegeInfo, Long> {

    /**
     * where user_info_id = [userInfoId]
     * 
     * @param userInfoid
     * @return List<{@link UserPrivilegeInfo}>
     */
    List<UserPrivilegeInfo> findByUserInfoId(Long userInfoId);

}
