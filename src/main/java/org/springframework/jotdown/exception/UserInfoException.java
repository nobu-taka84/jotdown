package org.springframework.jotdown.exception;

import org.springframework.jotdown.constants.UserInfoErrorType;

public class UserInfoException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * 認証/ユーザ情報エラーステータス
     */
    private UserInfoErrorType userInfoErrorType;

    /**
     * コンストラクタ
     * 
     * @param userInfoErrorType
     */
    public UserInfoException(UserInfoErrorType userInfoErrorType) {
        this.userInfoErrorType = userInfoErrorType;
    }

    /**
     * 認証/ユーザ情報エラーステータスを取得する
     * 
     * @return userInfoErrorType
     */
    public UserInfoErrorType getUserInfoErrorType() {
        return this.userInfoErrorType;
    }
}
