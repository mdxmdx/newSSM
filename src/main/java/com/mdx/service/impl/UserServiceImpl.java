package com.mdx.service.impl;

import static com.mdx.constant.SsmConstant.*;
import com.mdx.entity.User;
import static com.mdx.enums.ExceptionInfoEnum.*;
import com.mdx.exception.SsmException;
import com.mdx.mapper.UserMapper;
import com.mdx.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void checkUserName(String username) {
        User user=new User();
        user.setUsername(username);
        int count = userMapper.selectCount(user);
        if (count!=0){
            log.info("【异步校验用户名】 用户名不可用 username = {}",username);
            throw new SsmException(CHECKUSERNAME_ERROR);
        }
    }

    @Override
    public void register(User user) {
//        1. 生成一个盐
        String salt = UUID.randomUUID().toString();
//        2. 对密码进行和加盐.			->		shiro-core-1.4.0
        String newPassword=new Md5Hash(user.getPassword(),salt, HASH_ITERATIONS).toString();
//        3. 封装数据.
        user.setSalt(salt);
        user.setPassword(newPassword);
//        4. 执行添加.
        int count = userMapper.insertSelective(user);
//        5. 判断注册是否成功.
        if (count!=1){
            log.info("【注册】 注册失败，user={}",user);
            throw new SsmException(REGISTER_ERROR);
        }
    }

    @Override
    public User login(String username, String password) {
//        1. 根据用户名查询用户信息.
        User userCheck=new User();
        userCheck.setUsername(username);
        User user = userMapper.selectOne(userCheck);
//        2. 如果用户信息为null -> 用户名不正确.
        if (user==null){
            log.info("【登录】 登录失败,用户名不正确. username={}",username);
            throw new SsmException(LOGIN_USERNAME_ERROR);
        }
//        3. 将用户输入的密码进行加密和加盐.
        String newPassword=new Md5Hash(password,user.getSalt(),HASH_ITERATIONS).toString();
//        4. 判断密码是否正确(不正确) -> 密码不正确.
        if (!user.getPassword().equals(newPassword)){
            log.info("【登录】 登录失败,密码不正确. password={}",password);
            throw new SsmException(LOGIN_PASSWORD_ERROR);
        }
//        5. 返回用户信息.
        return user;
    }
}
