package com.book.servlet.auth;

import com.book.dao.UserMapper;
import com.book.entity.User;
import com.book.service.UserService;
import com.book.service.impl.UserServiceImpl;
import com.book.utils.MybatisUtil;
import com.book.utils.ThymeLeafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.ibatis.session.SqlSession;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    UserService service;

    @Override
    public void init() throws ServletException {
        service = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 如果用户已登录，直接跳转到首页
        if (req.getSession().getAttribute("user") != null) {
            resp.sendRedirect("index");
            return;
        }

        // 传递错误信息（如果有）
        String errorMessage = (String) req.getAttribute("error");

        // 准备Thymeleaf模板上下文
        Context context = new Context();
        context.setVariable("errorMessage", errorMessage); // 将错误信息传递给模板
        ThymeLeafUtil.process("register.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");

        String username = req.getParameter("username");
        String nickname = req.getParameter("nickname");
        String password = req.getParameter("password");

        // 验证输入数据
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "用户名和密码不能为空");
            doGet(req, resp);
            return;
        }

        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);

            // 检查用户名是否已存在
            User existingUser = mapper.getUserByUsername(username);
            if (existingUser != null) {
                req.setAttribute("error", "用户名已存在");
                doGet(req, resp);
                return;
            }

            // 创建新用户并保存到数据库
            mapper.insertUser(username, nickname, password); // 确保 `insertUser` 方法已实现
            sqlSession.commit(); // 确保事务提交

            // 注册成功后跳转到登录页面
            resp.sendRedirect("login");
        }
    }
}
