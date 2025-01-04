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
import java.util.List;

@WebServlet("/searchStudent")
public class SearchStudentServlet extends HttpServlet {

    private StudentService service;

    @Override
    public void init() throws ServletException {
        // 初始化StudentService
        service = new StudentServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        User user = (User) req.getSession().getAttribute("user");
        context.setVariable("nickname",user.getNickname());

        // 获取搜索关键词
        String searchQuery = req.getParameter("searchQuery");

        List<Student> studentList = null;
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // 如果有搜索关键词，调用搜索方法
            studentList = service.searchStudentsByName(searchQuery);
        } else {
            // 如果没有搜索关键词，获取所有学生
            studentList = service.getStudentList();
        }

        // 将列表设置到上下文
        context.setVariable("student_list", studentList);
        context.setVariable("search_query", searchQuery); // 显示当前搜索词
        ThymeLeafUtil.process("students.html", context, resp.getWriter());
    }
}
