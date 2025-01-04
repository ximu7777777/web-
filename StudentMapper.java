package com.book.dao;

import com.book.entity.Book;
import com.book.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface StudentMapper {

    // 查询所有学生
    @Select("SELECT * FROM student")
    List<Student> getStudentList();

    // 插入新学生
    @Insert("INSERT INTO student(name, sex, identity) VALUES(#{name}, #{sex}, #{identity})")
    void addStudent(@Param("name") String name,
                    @Param("sex") String sex,
                    @Param("identity") String identity);

    // 根据学生ID查询单个学生
    @Select("SELECT * FROM student WHERE sid = #{sid}")
    Student getStudentById(@Param("sid") int sid);

    // 更新学生信息
    @Update("UPDATE student SET name = #{name}, sex = #{sex}, identity = #{identity} WHERE sid = #{sid}")
    void updateStudent(@Param("sid") int sid,
                       @Param("name") String name,
                       @Param("sex") String sex,
                       @Param("identity") String identity);

    // 删除学生
    @Delete("DELETE FROM student WHERE sid = #{sid}")
    void deleteStudent(@Param("sid") int sid);

    @Select("SELECT * FROM student WHERE name LIKE CONCAT('%', #{name}, '%') " +
            "OR sid LIKE CONCAT('%', #{name}, '%') " +
            "OR sex LIKE CONCAT('%', #{name}, '%') " +
            "OR identity LIKE CONCAT('%', #{name}, '%') " )
    List<Student> searchStudentsByName(@Param("name") String name);
}
