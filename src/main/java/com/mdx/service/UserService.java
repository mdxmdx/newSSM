package com.mdx.service;

import com.mdx.entity.User;

public interface UserService {
    void checkUserName(String username);

    void register(User user);

    User login(String username,String password);
}
