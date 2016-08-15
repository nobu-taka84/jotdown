package org.springframework.jotdown.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jotdown.dao.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByUserInfoIdOrderBySortorder(Long userInfoId);

    @Query(value = "SELECT MAX(sortorder) FROM item WHERE user_info_id = ?1", nativeQuery = true)
    Integer selectMaxSortorder(Long userInfoId);

}
