package com.book.service;

import com.book.entity.Book;
import com.book.entity.Borrow;
import com.book.entity.Student;

import java.util.List;
import java.util.Map;

public interface BookService {
    List<Borrow> getBorrowList();
    void returnBook(String id);
    List<Book> getActiveBookList();
    List<Student> getStudentList();
    void addBorrow(int sid,int bid);
    Map<Book,Boolean> getBookList();
    void deleteBook(int bid);
    void addBook(String title,String desc,double price);
    List<Book> searchBooksByTitle(String searchQuery);

    // 获取所有风之翼的价格总和
    double getTotalRevenue();

    // 新增更新风之翼的方法
    void updateBook(int bid, String title, String desc, double price);



}
