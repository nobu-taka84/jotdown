package org.springframework.jotdown.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jotdown.constants.ErrorCode;
import org.springframework.jotdown.constants.UserPrivilege;
import org.springframework.jotdown.dao.dto.UserInfoDto;
import org.springframework.jotdown.dao.dto.UserPrivilegeInfoDto;
import org.springframework.jotdown.dao.entity.LoginHistory;
import org.springframework.jotdown.dao.entity.UserInfo;
import org.springframework.jotdown.dao.entity.UserInfoIdGenerator;
import org.springframework.jotdown.dao.entity.UserPrivilegeInfo;
import org.springframework.jotdown.dao.repository.LoginHistoryRepository;
import org.springframework.jotdown.dao.repository.UserInfoIdGeneratorRepository;
import org.springframework.jotdown.dao.repository.UserInfoRepository;
import org.springframework.jotdown.dao.repository.UserPrivilegeInfoRepository;
import org.springframework.jotdown.exception.SystemException;
import org.springframework.jotdown.service.AuthService;
import org.springframework.jotdown.service.UserInfoService;
import org.springframework.jotdown.util.ConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserInfoIdGeneratorRepository userInfoRepositoryIdGeneratorRepository;

    @Autowired
    private UserPrivilegeInfoRepository userPrivilegeInfoRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    /** user_info_id_generatorのpk */
    @Value("${auth.idGeneratorPk}")
    private String pk = "";

    /** パスワード期限(日数) */
    @Value("${auth.passwordValidTerm:0}")
    private int passwordValidTerm;

    @Override
    public boolean isUsernameExist(String userName) {
        return (selectUserInfoByUsername(userName) != null) ? true : false;
    }

    @Override
    public UserInfoDto selectUserInfoByUsername(String userName) {
        UserInfo userInfo = userInfoRepository.findByUsernameAndDeleteFlagFalse(userName);
        if (userInfo == null) {
            return null;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    public UserInfoDto selectUserInfoByUsernameAndPassword(String userName, String password) {
        UserInfo userInfo = userInfoRepository.findByUsernameAndPassword(userName, password);
        if (userInfo == null) {
            return null;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    public List<UserPrivilegeInfoDto> selectUserPrivilegeInfoByUserInfoId(Long userInfoId) {
        List<UserPrivilegeInfo> list = userPrivilegeInfoRepository.findByUserInfoId(userInfoId);
        return (CollectionUtils.isEmpty(list)) ? //
                null : ConvertUtils.copyProperties(list, UserPrivilegeInfoDto.class);

    }

    @Override
    public UserInfoDto createUserInfo(String userName, String password) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        // 発番管理取得
        Long userInfoId = null;
        UserInfoIdGenerator userInfoIdGenerator =
                userInfoRepositoryIdGeneratorRepository.findOne(pk);
        userInfoIdGenerator.setValue(userInfoIdGenerator.getValue() + 1);
        userInfoId = userInfoIdGenerator.getValue();
        userInfoRepositoryIdGeneratorRepository.save(userInfoIdGenerator);
        transactionManager.commit(status);

        status = transactionManager.getTransaction(def);
        UserInfo userInfo = new UserInfo();
        try {
            // パスワードのストレッチ
            String stretchingPassword =
                    authService.getSoltAndStretchingPassword(password, userInfoId);

            // user_info作成
            userInfo.setId(userInfoId);
            userInfo.setUsername(userName);
            userInfo.setPassword(stretchingPassword);
            userInfo.setMissCount(0);
            userInfo.setDeleteFlag(false);
            userInfo.setCreatedBy(userInfoId.toString());
            userInfo.setUpdatedBy(userInfoId.toString());

            // パスワード期限
            if (passwordValidTerm > 0) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, passwordValidTerm);
                userInfo.setPasswordValidTerm(new java.sql.Date(cal.getTime().getTime()));
            }

            userInfoRepository.save(userInfo);

            UserPrivilegeInfo userPrivilegeInfo = new UserPrivilegeInfo();
            userPrivilegeInfo.setUserInfoId(userInfoId);

            // TODO とりあえず１人目は管理者権限
            if (userInfoId.compareTo(1L) == 0) {
                userPrivilegeInfo.setUserPrivilege(UserPrivilege.ADMIN.getUserPrivilege());
            } else {
                userPrivilegeInfo.setUserPrivilege(UserPrivilege.USER.getUserPrivilege());
            }
            userPrivilegeInfoRepository.save(userPrivilegeInfo);
        } catch (Exception e) {
            transactionManager.rollback(status);
            LOGGER.error("■ ユーザー作成失敗 : " + e);

            throw new SystemException(ErrorCode.SYSTEMERROR_GENERAL.name(),
                    "ユーザー作成に失敗しました [" + e.getMessage() + "]");
        }
        transactionManager.commit(status);

        UserInfoDto userInfoDto = new UserInfoDto();
        BeanUtils.copyProperties(userInfo, userInfoDto);
        return userInfoDto;
    }

    @Override
    public void updatePassword(String userName, String password) {
        UserInfoDto userInfoDto = selectUserInfoByUsername(userName);
        if (userInfoDto == null) {
            throw new SystemException(ErrorCode.CONSISTENCYERROR_GENERAL.name(), "ユーザーが存在していません");
        }

        try {
            // パスワードのストレッチ
            String stretchingPassword =
                    authService.getSoltAndStretchingPassword(password, userInfoDto.getId());

            // user_info作成
            UserInfo userInfo = new UserInfo();
            userInfo.setId(userInfoDto.getId());
            userInfo.setPassword(stretchingPassword);

            // パスワード期限
            if (passwordValidTerm > 0) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, passwordValidTerm);
                userInfo.setPasswordValidTerm(new java.sql.Date(cal.getTime().getTime()));
            }

            userInfoRepository.save(userInfo);

        } catch (Exception e) {
            LOGGER.error("■ パスワード更新失敗 : " + e);

            throw new SystemException(ErrorCode.SYSTEMERROR_GENERAL.name(),
                    "パスワード更新に失敗しました [" + e.getMessage() + "]");
        }
    }

    @Override
    public void updateLastLoginedAt(Long userId) {
        UserInfo userInfo = userInfoRepository.findOne(userId);
        userInfo.setLastLoginedAt(new Timestamp(System.currentTimeMillis()));
        userInfoRepository.save(userInfo);
    }

    @Override
    public void updateLoginHistory(LoginHistory loginHistory) {
        loginHistoryRepository.save(loginHistory);
    }

    @Override
    public void updateMissCount(Long userId) {
        UserInfo userInfo = userInfoRepository.findOne(userId);
        userInfo.setMissCount(userInfo.getMissCount() + 1);
        userInfoRepository.save(userInfo);
    }

}
