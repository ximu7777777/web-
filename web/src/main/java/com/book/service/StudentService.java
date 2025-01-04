package com.book.service;

import com.book.entity.Student;

import java.util.List;

public interface StudentService {

    // 获取所有学生列表
    List<Student> getStudentList();

    // 根据学生ID查询学生信息
    Student getStudentById(int sid);

    // 添加新学生
    void addStudent(String name, String sex, String identity);

    // 更新学生信息
    void updateStudent(int sid, String name, String sex, String identity);

    // 删除学生
    void deleteStudent(int sid);

    List<Student> searchStudentsByName(String searchQuery);
}
