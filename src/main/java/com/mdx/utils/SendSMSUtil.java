package com.mdx.utils;

import com.mdx.constant.SsmConstant;
import com.mdx.vo.ResultVo;
import com.yunpian.sdk.YunpianClient;
import com.yunpian.sdk.model.Result;
import com.yunpian.sdk.model.SmsSingleSend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Component
public class SendSMSUtil {

    @Value("${yunpian.apikey}")
    private String apikey;



    public ResultVo senSMS(String phone, HttpSession session){
        YunpianClient clnt=new YunpianClient(apikey).init();
        int code= (int) ((Math.random()*900000)+100000);
        session.setAttribute(SsmConstant.USER_CODE,code+"");
        Map<String, String> param = clnt.newParam(2);
        param.put(YunpianClient.MOBILE, phone);
        param.put(YunpianClient.TEXT, "【云片网】您的验证码是" + code);
        Result<SmsSingleSend> r = clnt.sms().single_send(param);
        ResultVo vo=new ResultVo(r.getCode(),r.getMsg(),null);
        clnt.close();
        return vo;
    }

}
