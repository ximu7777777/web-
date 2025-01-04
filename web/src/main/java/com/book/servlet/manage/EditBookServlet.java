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
import java.util.Map;

@WebServlet("/edit-book")
public class EditBookServlet extends HttpServlet {

    private BookService service;

    @Override
    public void init() throws ServletException {
        // 初始化服务类
        service = new BookServiceImpl();
    }

    // 处理GET请求，显示修改页面
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取书籍ID，查询该书籍信息
        String bidParam = req.getParameter("bid");

        if (bidParam == null || bidParam.trim().isEmpty()) {
            resp.sendRedirect("books");
            return;
        }

        int bid;
        try {
            bid = Integer.parseInt(bidParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("books");
            return;
        }

        // 获取书籍列表（Map<Book, Boolean>）
        Map<Book, Boolean> bookMap = service.getBookList();

        // 查找指定bid的书籍
        Book book = null;
        for (Map.Entry<Book, Boolean> entry : bookMap.entrySet()) {
            if (entry.getKey().getBid() == bid) {
                book = entry.getKey();
                break;
            }
        }


        if (book == null) {
            // 如果没有找到该书籍，重定向到书籍列表页面
            resp.sendRedirect("books");
            return;
        }

        // 将书籍信息传递到修改页面
        Context context = new Context();
        User user = (User) req.getSession().getAttribute("user");
        context.setVariable("nickname",user.getNickname());

        context.setVariable("book", book); // 将书籍对象传递到页面
        ThymeLeafUtil.process("edit-book.html", context, resp.getWriter());
    }

    // 处理POST请求，提交修改后的数据
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取表单提交的数据
        String bidParam = req.getParameter("bid");
        String title = req.getParameter("title");
        String desc = req.getParameter("desc");
        String priceParam = req.getParameter("price");

        // 添加日志输出确认
        System.out.println("bid: " + bidParam + ", title: " + title + ", desc: " + desc + ", price: " + priceParam);

        // 校验 bid 是否有效
        if (bidParam == null || bidParam.trim().isEmpty()) {
            resp.sendRedirect("books");
            return;
        }

        int bid;
        try {
            bid = Integer.parseInt(bidParam);
        } catch (NumberFormatException e) {
            resp.sendRedirect("books");
            return;
        }

        // 校验价格是否有效
        double price = 0;
        try {
            price = Double.parseDouble(priceParam);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "请输入有效的价格！");
            req.setAttribute("bid", bid); // 将书籍ID传回页面
            req.setAttribute("title", title); // 将title字段值传回
            req.setAttribute("desc", desc); // 将desc字段值传回
            req.setAttribute("price", priceParam); // 将price字段值传回

            // 使用 forward 转发到 edit-book 页面
            Context context = new Context();
            context.setVariable("errorMessage", "请输入有效的价格！");
            context.setVariable("bid", bid);
            context.setVariable("title", title != null ? title : "");
            context.setVariable("desc", desc != null ? desc : "");
            context.setVariable("price", priceParam != null ? priceParam : "");
            ThymeLeafUtil.process("edit-book.html", context, resp.getWriter());
            return;
        }

        // 校验是否所有字段都有值
        if (title == null || title.trim().isEmpty() || desc == null || desc.trim().isEmpty()) {
            // 如果有任何字段为空，返回一个错误或提示信息
            req.setAttribute("errorMessage", "请填写完整的书籍信息！");
            req.setAttribute("bid", bid); // 将书籍ID传回页面
            req.setAttribute("title", title); // 将title字段值传回
            req.setAttribute("desc", desc); // 将desc字段值传回
            req.setAttribute("price", priceParam); // 将price字段值传回

            // 使用 forward 转发到 edit-book 页面
            Context context = new Context();
            context.setVariable("errorMessage", "请填写完整的书籍信息！");
            context.setVariable("bid", bid);
            context.setVariable("title", title != null ? title : "");
            context.setVariable("desc", desc != null ? desc : "");
            context.setVariable("price", priceParam != null ? priceParam : "");
            ThymeLeafUtil.process("edit-book.html", context, resp.getWriter());
            return;
        }

        // 调用服务层的更新书籍信息方法
        service.updateBook(bid, title, desc, price);

        // 修改成功，重定向到书籍列表页面
        resp.sendRedirect("books");
    }
}
