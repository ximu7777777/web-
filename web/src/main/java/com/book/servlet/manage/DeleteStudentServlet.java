package com.book.servlet.manage;

import com.book.service.StudentService;
import com.book.service.impl.StudentServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete-student")
public class DeleteStudentServlet extends HttpServlet {
    private StudentService service;

    @Override
    public void init() throws ServletException {
        // 初始化 StudentService 实现类
        service = new StudentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取前端传来的学生 ID 参数
        int studentId = Integer.parseInt(req.getParameter("sid"));

        // 调用 service 层的删除方法删除学生记录
        service.deleteStudent(studentId);

        // 删除成功后重定向到学生列表页面
        resp.sendRedirect("students"); // 假设学生列表页面 URL 是 /students
    }
}
