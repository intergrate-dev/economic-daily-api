package com.founder.econdaily.modules.auth.controller;

import com.founder.ark.common.utils.bean.ResponseObject;
import com.founder.econdaily.common.annotation.Validate;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.controller.BaseController;
import com.founder.econdaily.common.entity.DataResult;
import com.founder.econdaily.common.util.JwtUtils;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.modules.auth.entity.User;
import com.founder.econdaily.modules.auth.entity.VUserEntity;
import com.founder.econdaily.modules.auth.repository.UserRepository;
import com.founder.econdaily.modules.auth.repository.VUserRepository;
import io.github.swagger2markup.internal.utils.RegexUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "用户认证")
@RestController
@RequestMapping(value = "/auth")
public class AuthController extends BaseController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    VUserRepository vUserRepository;

    @ApiOperation(value = "用户登陆")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    @Validate(module = "test", desc = "测试")
    /*public ResponseObject login(@ApiParam("用户名") @RequestParam(name = "username", required = true, defaultValue = "333333@qq.com") String username,
                                @ApiParam("密码") @RequestParam(name = "password", required = true, defaultValue = "123456") String password) {*/
    /*public ResponseObject login(@RequestParam(name = "username", required = true) String username,
                                @RequestParam(name = "password", required = true) String password) {*/
    public ResponseObject login(@Valid User user, BindingResult validResult) {
        /*if (validResult.hasErrors()) {
            return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, validErrorMsg(validResult));
        }*/

        User userQuery = userRepository.findByUsername(user.getUsername());
        if (user != null) {
            if (user.getPassword().equals(userQuery.getPassword())) {
                //把token返回给客户端-->客户端保存至cookie-->客户端每次请求附带cookie参数
                String JWT = JwtUtils.createJWT("1", user.getUsername(), SystemConstant.JWT_TTL);
                System.setProperty("jwt_token", JWT);
                Map<String, String> map = new HashMap<String, String>();
                map.put("token", JWT);
                return ResponseObject.newSuccessResponseObject(map, "登陆成功");
            } else {
                return ResponseObject.newErrorResponseObject(SystemConstant.JWT_ERRCODE_FAIL, "密码错误");
            }
        } else {
            return ResponseObject.newErrorResponseObject(SystemConstant.JWT_ERRCODE_FAIL, "用户不存在");
        }
    }

}
