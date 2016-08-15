package org.springframework.jotdown.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jotdown.dao.entity.LoginHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

}
