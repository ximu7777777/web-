package com.book.servlet.manage;

import com.book.entity.User;
import com.book.service.StudentService;
import com.book.service.impl.StudentServiceImpl;
import com.book.utils.ThymeLeafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;

@WebServlet("/add-student")
public class AddStudentServlet extends HttpServlet {

    private StudentService service;

    @Override
    public void init() throws ServletException {
        // 初始化服务类
        service = new StudentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 创建上下文对象，并传递学生列表或其他需要的数据
        Context context = new Context();
        User user = (User) req.getSession().getAttribute("user");
        context.setVariable("nickname",user.getNickname());

        ThymeLeafUtil.process("add-student.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从请求中获取表单提交的数据
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        String identity = req.getParameter("identity");



        // 检查是否所有字段都有值
        if (name == null || name.trim().isEmpty() || sex == null || sex.trim().isEmpty() || identity == null || identity.trim().isEmpty()) {
            // 打印日志以调试
            System.out.println("接收到的值：name=" + name + ", sex=" + sex + ", identity=" + identity);


            // 如果有任何字段为空，返回一个错误或提示信息
            req.setAttribute("errorMessage", "请填写会员信息！");
            req.setAttribute("name", name); // 将name字段值传回
            req.setAttribute("sex", sex); // 将sex字段值传回
            req.setAttribute("identity", identity); // 将identity字段值传回

            // 创建上下文对象并传递数据
            Context context = new Context();
            context.setVariable("errorMessage", "请填写会员信息！");
            context.setVariable("name", name!= null ? name : "");
            context.setVariable("sex", sex!= null ? sex : "");
            context.setVariable("identity", identity!= null ? identity : "");



            // 使用 forward 转发到 add-student 页面
            ThymeLeafUtil.process("add-student.html", context, resp.getWriter());
            return;
        }

        // 调用服务层的添加学生方法
        service.addStudent(name, sex, identity);

        // 重定向到学生列表页面或其他页面
        resp.sendRedirect("students");
    }
}
