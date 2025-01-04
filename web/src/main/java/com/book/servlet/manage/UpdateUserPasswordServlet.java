package com.book.servlet.manage;

import com.book.entity.User;
import com.book.service.UserService;
import com.book.service.impl.StudentServiceImpl;
import com.book.service.impl.UserServiceImpl;
import com.book.utils.ThymeLeafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet("/updatePassword")
public class UpdateUserPasswordServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        // 初始化服务类
        userService = new UserServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取当前登录的用户
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
        User user = (User) request.getSession().getAttribute("user");

        if (user == null) {
            // 如果用户未登录，重定向到登录页面
            response.sendRedirect("login");
            return;
        }


        // 清除错误信息
        HttpSession session = request.getSession();
        session.removeAttribute("errorMessage");

        // 已登录，显示修改密码页面
        Context context = new Context();

        context.setVariable("nickname",user.getNickname());

        ThymeLeafUtil.process("updatePassword.html", context, response.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取前端传来的数据
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");  // 获取确认密码
        HttpSession session = request.getSession();

        // 获取当前登录的用户
        User user = (User) session.getAttribute("user");
        Context context = new Context();


        if (user == null) {
            // 如果用户未登录，重定向到登录页面
            response.sendRedirect("login");
            return;
        }

        String username = user.getUsername();  // 从 User 对象中获取用户名

        // 检查新密码和确认密码是否一致
        if (!newPassword.equals(confirmPassword)) {


            System.out.println("password not equal");
            request.setAttribute("errorMessage", "新密码和确认密码不一致！");


            response.sendRedirect("updatePassword");
            return;
        }

        // 调用 UserService 来验证旧密码是否正确
        boolean oldPasswordCorrect = userService.verifyOldPassword(username, oldPassword);
        if (!oldPasswordCorrect) {

            System.out.println("Old password is incorrect");
            request.setAttribute("errorMessage", "旧密码错误！");


            response.sendRedirect("updatePassword");
            return;
        }





        // 调用 UserService 来更新密码
        boolean success = userService.updatePassword(username, oldPassword, newPassword, session);

        // 根据更新结果返回相应的反馈
        if (success) {
            // 更新密码成功，清除 session，跳转到登录页面
            session.invalidate();  // 清除 session 中的用户信息
            response.sendRedirect("login");  // 跳转到登录页面
        } else {
            System.out.println("Password update failed");
            request.setAttribute("errorMessage", "密码更新失败，请稍后再试！");


            response.sendRedirect("updatePassword");


        }
    }
}

