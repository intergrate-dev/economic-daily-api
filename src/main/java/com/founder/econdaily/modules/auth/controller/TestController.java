package com.founder.econdaily.modules.auth.controller;

import cn.licoy.encryptbody.annotation.encrypt.EncryptBody;
import cn.licoy.encryptbody.enums.EncryptBodyMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
// ;
import com.founder.econdaily.common.util.ResponseObject;

import com.founder.econdaily.common.annotation.JwtToken;
import com.founder.econdaily.common.annotation.Validate;
import com.founder.econdaily.common.annotation.ValidateParam;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.modules.auth.entity.User;
import com.founder.econdaily.modules.auth.repository.UserRepository;
import com.founder.econdaily.modules.auth.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController {

    private static Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    UserService userService;

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
    @Validate
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

    @ApiOperation(value = "批量插入")
    @RequestMapping(value = "/batchInsert", method = RequestMethod.POST)
    public ResponseObject batchInsert(Integer batch) {
        Map<String, String> map = new HashMap<>();
        List<User> users = new ArrayList<>();
        Integer NO = null;
        User user = null;
        for (int i = 0; i < batch; i++) {
            NO = i + 1;
            user = new User();
            user.setId(NO);
            user.setUsername("user_" + NO);
            user.setPassword("pwd: " + NO);
            user.setDescription("default ...");
            users.add(user);
        }
        try {
            userService.insertByBatch(users);
        } catch (Exception e) {
            logger.error("error: {}", e.getMessage());
            map.put("result", "fail");
            e.printStackTrace();
            return ResponseObject.newErrorResponseObject(SystemConstant.JWT_ERRCODE_FAIL, "batch insert fail");
        }
        map.put("result", "success");
        return ResponseObject.newSuccessResponseObject(map, "batch insert success");
    }

    public static void main(String[] args) {
        
    }
}
