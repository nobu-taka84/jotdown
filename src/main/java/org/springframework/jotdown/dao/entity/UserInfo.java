package org.springframework.jotdown.dao.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String password;

    private Timestamp lastLoginedAt;

    private Date passwordValidTerm;

    private Short missCount;

    private List<String> userPrivilegeInfoList;

    private Boolean deleteFlag;

    private String createdBy;

    private String updatedBy;

    public UserInfo() {}

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id セットする id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username セットする username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password セットする password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return lastLoginedAt
     */
    public Timestamp getLastLoginedAt() {
        return lastLoginedAt;
    }

    /**
     * @param lastLoginedAt セットする lastLoginedAt
     */
    public void setLastLoginedAt(Timestamp lastLoginedAt) {
        this.lastLoginedAt = lastLoginedAt;
    }

    /**
     * @return passwordValidTerm
     */
    public Date getPasswordValidTerm() {
        return passwordValidTerm;
    }

    /**
     * @param passwordValidTerm セットする passwordValidTerm
     */
    public void setPasswordValidTerm(Date passwordValidTerm) {
        this.passwordValidTerm = passwordValidTerm;
    }

    /**
     * @return missCount
     */
    public Short getMissCount() {
        return missCount;
    }

    /**
     * @param missCount セットする missCount
     */
    public void setMissCount(Short missCount) {
        this.missCount = missCount;
    }

    /**
     * @return userPrivilegeInfoList
     */
    public List<String> getUserPrivilegeInfoList() {
        return userPrivilegeInfoList;
    }

    /**
     * @param userPrivilegeInfoList セットする userPrivilegeInfoList
     */
    public void setUserPrivilegeInfoList(List<String> userPrivilegeInfoList) {
        this.userPrivilegeInfoList = userPrivilegeInfoList;
    }

    /**
     * @return deleteFlag
     */
    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    /**
     * @param deleteFlag セットする deleteFlag
     */
    public void setDeleteFlag(Boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    /**
     * @return createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy セットする createdBy
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return updatedBy
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * @param updatedBy セットする updatedBy
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

}
