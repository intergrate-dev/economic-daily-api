package com.founder.econdaily.modules.auth.controller;

import cn.licoy.encryptbody.annotation.encrypt.EncryptBody;
import cn.licoy.encryptbody.enums.EncryptBodyMethod;
import com.founder.ark.common.utils.bean.ResponseObject;
import com.founder.econdaily.common.annotation.JwtToken;
import com.founder.econdaily.common.annotation.Validate;
import com.founder.econdaily.common.annotation.ValidateParam;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.modules.auth.entity.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping
    @ResponseBody
    @EncryptBody(value = EncryptBodyMethod.AES)
    @RequestMapping("/entrypt")
    public String entrypt(){
        return "hello world";
    }

    @GetMapping
    @ResponseBody
    @RequestMapping("/test")
    public String test(@JwtToken String username){
        return "hello world ".concat(username);
    }


    @ApiOperation(value = "用户登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @Validate(module = "test", desc = "测试")
    // public ResponseObject login(@Valid User user, @ValidateParam String validResult, BindingResult bindingResult) {
    public ResponseObject login(@Valid User user, String validResult, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, validErrorMsg(validResult));
        }
        /*if (validResult instanceof ResponseObject && validResult != null) {
            return (ResponseObject) validResult;
        }*/
        if (validResult != null) {

        }
        return null;
    }
}
