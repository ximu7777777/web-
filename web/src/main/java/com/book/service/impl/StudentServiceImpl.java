package com.book.service.impl;

import com.book.dao.StudentMapper;
import com.book.entity.Student;
import com.book.service.StudentService;
import com.book.utils.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class StudentServiceImpl implements StudentService {

    /**
     * 获取所有学生信息列表
     * @return 学生信息列表
     */
    @Override
    public List<Student> getStudentList() {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            return mapper.getStudentList();
        }
    }

    /**
     * 根据学生ID获取学生信息
     * @param sid 学生ID
     * @return 学生对象
     */
    @Override
    public Student getStudentById(int sid) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            return mapper.getStudentById(sid);
        }
    }

    /**
     * 添加新学生
     * @param name 学生姓名
     * @param sex 学生性别
     * @param identity 学生身份证号
     */
    @Override
    public void addStudent(String name, String sex, String identity) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            mapper.addStudent(name, sex, identity);
        }
    }

    /**
     * 更新学生信息
     * @param sid 学生ID
     * @param name 新的学生姓名
     * @param sex 新的学生性别
     * @param identity 新的学生身份证号
     */
    @Override
    public void updateStudent(int sid, String name, String sex, String identity) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            mapper.updateStudent(sid, name, sex, identity);

            // 确保事务提交
            sqlSession.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }



    /**
     * 删除学生信息
     * @param sid 学生ID
     */
    @Override
    public void deleteStudent(int sid) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            mapper.deleteStudent(sid);
        }
    }

    @Override
    public List<Student> searchStudentsByName(String name) {
        try (SqlSession sqlSession = MybatisUtil.getSession()) {
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            return mapper.searchStudentsByName(name);
        }
    }

}
