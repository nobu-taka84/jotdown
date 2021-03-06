package org.springframework.jotdown.model;

import java.util.List;

import org.springframework.jotdown.dao.dto.UserInfoDto;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginUser extends User {
    private static final long serialVersionUID = 1L;

    private UserInfoDto userInfo;

    /**
     * コンストラクタ
     * 
     * @param user
     */
    public LoginUser(UserInfoDto userInfo, List<SimpleGrantedAuthority> authorityList) {
        super(userInfo.getUsername(), userInfo.getPassword(), authorityList);
        this.userInfo = userInfo;
    }

    /**
     * @return userInfo
     */
    public UserInfoDto getUserInfo() {
        return userInfo;
    }

    /**
     * @param userInfo セットする userInfo
     */
    public void setUserInfo(UserInfoDto userInfo) {
        this.userInfo = userInfo;
    }

}
