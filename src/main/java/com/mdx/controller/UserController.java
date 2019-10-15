package com.mdx.controller;

import static com.mdx.constant.SsmConstant.*;
import com.mdx.entity.User;
import static com.mdx.enums.ExceptionInfoEnum.*;
import com.mdx.exception.SsmException;
import com.mdx.service.UserService;
import com.mdx.utils.SendSMSUtil;
import com.mdx.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SendSMSUtil sendSMSUtil;

    @RequestMapping("/register-ui")
    public String registerUI(){
        return "user/register";
    }

    @PostMapping("/checkUserName")
    @ResponseBody
    public ResultVo checkUserName(@RequestBody User user){
        String username = user.getUsername();
        userService.checkUserName(username);
        return new ResultVo(0,"成功",null);
    }

    @PostMapping("/send-sms")
    @ResponseBody
    public ResultVo sendSms(String phone, HttpSession session){
        if (phone==null||phone.length()!=11){
            log.info("【发送短信】 发送短信失败 phone={}",phone);
            throw new SsmException(PARAM_ERROR.getCode(),"发送短信失败");
        }
        ResultVo vo = sendSMSUtil.senSMS(phone, session);
        return vo;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResultVo register(@Valid User user, BindingResult bindingResult,String registerCode,HttpSession session){

        if (bindingResult.hasErrors()){
            String msg = bindingResult.getFieldError().getDefaultMessage();
            log.info("【参数校验】 参数校验失败 user={},msg={}",user,msg);
            throw new SsmException(PARAM_ERROR.getCode(),msg);
        }

        String trueCode = (String) session.getAttribute(USER_CODE);
        if (registerCode==null||!registerCode.equals(trueCode)){
            log.info("【参数校验】 验证码校验失败 registerCode={},trueCode={}",registerCode,trueCode);
            throw new SsmException(PARAM_ERROR.getCode(),"验证码不正确！");
        }

//        4. 调用service执行注册.
        userService.register(user);
//        5. 响应数据.
        return new ResultVo(0,"成功",null);
    }

    @GetMapping("/login-ui")
    public String loginUI(){
        return "/user/login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResultVo login(String username,String password,HttpSession session){
        if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            log.info("【登录功能】 参数不合法!! username = {},password = {}",username,password);
            throw new SsmException(PARAM_ERROR.getCode(),"用户名和密码皆不能为空！");
        }
        User user = userService.login(username, password);
        session.setAttribute("user",user);
        return new ResultVo(0,"成功",null);
    }
}
