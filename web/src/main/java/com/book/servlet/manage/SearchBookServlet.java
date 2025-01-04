package com.book.servlet.manage;

import com.book.entity.Book;
import com.book.entity.User;
import com.book.service.BookService;
import com.book.service.impl.BookServiceImpl;
import com.book.utils.ThymeLeafUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/search")
public class SearchBookServlet extends HttpServlet {

    private BookService service;

    @Override
    public void init() throws ServletException {
        service = new BookServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        User user = (User) req.getSession().getAttribute("user");
        context.setVariable("nickname",user.getNickname());

        // 获取搜索关键词
        String searchQuery = req.getParameter("searchQuery");


        List<Book> bookList = null;
        if (searchQuery != null) {
            // 调用搜索方法
            bookList = service.searchBooksByTitle(searchQuery);
        } else {
            // 如果没有搜索关键词，获取所有书籍
            bookList = service.getBookList().keySet().stream().toList();
        }

        // 将书籍列表设置到上下文
        context.setVariable("book_list", bookList);
        context.setVariable("book_list_status",new ArrayList<>(service.getBookList().values()));
        context.setVariable("search_query", searchQuery); // 显示当前搜索词
        ThymeLeafUtil.process("books.html", context, resp.getWriter());
    }

}
