package com.book.servlet.manage;

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

@WebServlet("/add-book")
public class AddBookServlet extends HttpServlet {
    BookService service;
    @Override
    public void init() throws ServletException {
        service =new BookServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Context context = new Context();
        User user = (User) req.getSession().getAttribute("user");
        context.setVariable("nickname",user.getNickname());
        ThymeLeafUtil.process("add-book.html",context,resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String title = req.getParameter("title");
        String desc = req.getParameter("desc");
        String priceStr = req.getParameter("price");

        // 创建上下文对象
        Context context = new Context();

        // 检查价格是否为有效数字
        double price = 0;
        boolean validPrice = false;

        try {
            // 尝试解析价格字段为数字
            price = Double.parseDouble(priceStr);
            validPrice = true; // 标记为有效数字
        } catch (NumberFormatException e) {
            // 如果价格不是有效的数字，忽略回显
            validPrice = false;
        }

        // 如果是有效数字，回显价格
        if (validPrice) {
            context.setVariable("price", priceStr); // 只回显有效的价格
        } else {
            context.setVariable("price", null); // 如果价格无效，不回显
        }

        // 传递其他字段
        context.setVariable("title", title);
        context.setVariable("desc", desc);

        // 检查必填字段是否为空
        if (title == null || title.trim().isEmpty() || desc == null || desc.trim().isEmpty() || !validPrice) {
            context.setVariable("errorMessage", "请填写完整的信息！");
            // 使用 forward 转发到 add-book 页面
            ThymeLeafUtil.process("add-book.html", context, resp.getWriter());
            return;
        }

        // 调用服务层的方法进行数据处理
        service.addBook(title, desc, price);

        // 重定向到书籍列表页面
        resp.sendRedirect("books");
    }


}
