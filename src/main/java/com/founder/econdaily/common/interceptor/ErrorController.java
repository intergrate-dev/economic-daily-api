package com.founder.econdaily.common.interceptor;

import com.founder.ark.common.utils.bean.ResponseObject;
import com.founder.econdaily.common.constant.SystemConstant;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ErrorController {
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

    @GetMapping(value = "/error")
    //@RequestMapping(value = "/error", method = RequestMethod.POST)
    //@ResponseBody
    public ResponseObject login(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, SystemConstant.REQ_ILLEGAL);
    }
}
