package com.example.springbootmall.dao;

import com.example.springbootmall.dto.UserRegisterRequest;
import com.example.springbootmall.model.User;

public interface UserDao {

    User getUserById(Integer userId);

    User getUserByEmail(String email);

    Integer createUser(UserRegisterRequest userRegisterRequest);
}
