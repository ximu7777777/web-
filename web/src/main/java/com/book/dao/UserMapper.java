package com.book.dao;

import com.book.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface UserMapper {

    @Select("select * from admin where username = #{username} and password = #{password}")
    User getUser(@Param("username") String username, @Param("password") String password);

    // Check if username exists
    @Select("select * from admin where username = #{username}")
    User getUserByUsername(@Param("username") String username);

    // Insert a new user
    @Insert("insert into admin (username, nickname, password) values (#{username}, #{nickname}, #{password})")
    int insertUser(@Param("username") String username, @Param("nickname") String nickname, @Param("password") String password);


    @Update("UPDATE admin SET password = #{password} WHERE username = #{username}")
    int updatePassword(@Param("username") String username, @Param("password") String password);
}

