package com.book.servlet.manage;

import com.book.service.BookService;
import com.book.service.impl.BookServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete-book")
public class DeleteBookServlet extends HttpServlet {
    BookService service;
    @Override
    public void init() throws ServletException {
        service =new BookServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int bid = Integer.parseInt(req.getParameter("bid"));
//        service.deleteBook(bid);
//        resp.sendRedirect("books");

        try {
            service.deleteBook(bid);
            // 删除成功后重定向到书籍列表页面
            resp.sendRedirect("books");
        } catch (IllegalStateException e) {
            // 捕获异常并返回友好提示
            req.setAttribute("error_message", e.getMessage());
            req.getRequestDispatcher("books").forward(req, resp);
        }
    }
}
