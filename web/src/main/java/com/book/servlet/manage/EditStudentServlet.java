package com.book.servlet.manage;

import com.book.entity.Student;
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

@WebServlet("/edit-student")
public class EditStudentServlet extends HttpServlet {

    private StudentService service;

    @Override
    public void init() throws ServletException {
        // 初始化服务类
        service = new StudentServiceImpl();
    }

    // 处理GET请求，显示修改页面
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取学生ID，查询该学生信息
        String sidParam = req.getParameter("sid");

        if (sidParam == null || sidParam.trim().isEmpty()) {
            resp.sendRedirect("students");
            return;
        }

        int sid;
        try {
            sid = Integer.parseInt(sidParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("students");
            return;
        }

        Student student = service.getStudentById(sid);

        if (student == null) {
            // 如果没有找到该学生，重定向到学生列表页面
            resp.sendRedirect("students");
            return;
        }

        // 将学生信息传递到修改页面
        Context context = new Context();
        User user = (User) req.getSession().getAttribute("user");
        context.setVariable("nickname",user.getNickname());

        context.setVariable("student", student); // 将学生对象传递到页面
        ThymeLeafUtil.process("edit-student.html", context, resp.getWriter());
    }

    // 处理POST请求，提交修改后的数据
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        // 获取表单提交的数据
        String sidParam = req.getParameter("sid");
        String name = req.getParameter("name");
        String sex = req.getParameter("sex");
        String identity = req.getParameter("identity");

        // 添加日志输出确认
        System.out.println("sid: " + sidParam + ", name: " + name + ", sex: " + sex + ", identity: " + identity);

        // 校验 sid 是否有效
        if (sidParam == null || sidParam.trim().isEmpty()) {
            resp.sendRedirect("students");
            return;
        }

        int sid;
        try {
            sid = Integer.parseInt(sidParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("students");
            return;
        }

        // 检查是否所有字段都有值
        if (name == null || name.trim().isEmpty() || sex == null || sex.trim().isEmpty() || identity == null || identity.trim().isEmpty()) {
            // 如果有任何字段为空，返回一个错误或提示信息
            req.setAttribute("errorMessage", "请填写完整的学生信息！");
            req.setAttribute("sid", sid); // 将学生ID传回页面
            req.setAttribute("name", name); // 将name字段值传回
            req.setAttribute("sex", sex); // 将sex字段值传回
            req.setAttribute("identity", identity); // 将identity字段值传回

            // 使用 forward 转发到 edit-student 页面
            Context context = new Context();
            context.setVariable("errorMessage", "请填写完整的学生信息！");
            context.setVariable("sid", sid);
            context.setVariable("name", name != null ? name : "");
            context.setVariable("sex", sex != null ? sex : "");
            context.setVariable("identity", identity != null ? identity : "");
            ThymeLeafUtil.process("edit-student.html", context, resp.getWriter());
            return;
        }

        // 调用服务层的更新学生信息方法
        service.updateStudent(sid, name, sex, identity);

        // 修改成功，重定向到学生列表页面
        resp.sendRedirect("students");
    }
}
