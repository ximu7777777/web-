package com.book.service;

import jakarta.servlet.http.HttpSession;

public interface UserService {
    boolean auth(String username, String password, HttpSession session);

    boolean updatePassword(String username, String oldPassword, String newPassword, HttpSession session);

    boolean verifyOldPassword(String username, String oldPassword);  // 新增方法用于验证旧密码
}

