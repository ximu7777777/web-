package com.book.service.impl;

import com.book.dao.UserMapper;
import com.book.entity.User;
import com.book.service.UserService;
import com.book.utils.MybatisUtil;
import jakarta.servlet.http.HttpSession;
import org.apache.ibatis.session.SqlSession;

public class UserServiceImpl implements UserService {

    @Override
    public boolean auth(String username, String password, HttpSession session) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUser(username, password);  // 直接查询用户名和密码匹配的用户
            if (user == null) return false;
            session.setAttribute("user", user);
            return true;
        }
    }

    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword, HttpSession session) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            // 根据用户名查询用户
            User user = mapper.getUserByUsername(username);
            if (user == null) {
                return false;  // 用户不存在
            }

            // 比较当前输入的旧密码与数据库中的密码是否匹配
            if (!oldPassword.equals(user.getPassword())) {
                return false;  // 旧密码不匹配
            }

            // 更新密码
            int result = mapper.updatePassword(username, newPassword);  // 直接更新明文密码
            return result > 0;
        }
    }

    /**
     * 用于验证旧密码是否正确
     * @param username 用户名
     * @param oldPassword 旧密码
     * @return 是否正确
     */
    public boolean verifyOldPassword(String username, String oldPassword) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User user = mapper.getUserByUsername(username);
            if (user == null) {
                return false;  // 用户不存在
            }

            // 直接比较明文旧密码
            return oldPassword.equals(user.getPassword());
        }
    }
}
