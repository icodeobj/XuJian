package com.kingland.neusoft.course.service;

import com.kingland.neusoft.course.config.PasswordEncoderConfig;
import com.kingland.neusoft.course.mapper.UserMapper;
import com.kingland.neusoft.course.mapper.dao.UserModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RService implements RRService {
    @Resource
    private UserMapper userMapper;
    public List<UserModel> searchUserFuzzy(String keyword) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        logger.info("尝试根据关键词搜索用户");
        List<User> userList = userMapper.selUserFuzzy(keyword);
        if (userList != null) {
            return userList;
        }
        return null;
    }
    @Override
    public int modifyUserLastLoginTime(User user) {
        Logger logger = Logger.getLogger(UserServiceImpl.class);
        // 先检查用户是否存在
        logger.info("检查用户是否存在，id：" + user.getUser_id());
        if (userMapper.selUserByUserId(user.getUser_id()) == null) {
            return -1;
        }
        logger.info("更新用户最近登录时间，用户id【" + user.getUser_id() + "】最近登录时间：" + user.getUser_lastLoginTime());
        if (userMapper.updUserLastLoginTime(user) <= 0) {
            return -2;
        } else {
            return 0;
        }
    }
}
