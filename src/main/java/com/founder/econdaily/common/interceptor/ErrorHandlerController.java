package com.founder.econdaily.common.interceptor;

// ;
import com.founder.econdaily.common.util.ResponseObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import springfox.documentation.service.ResponseMessage;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class ErrorHandlerController implements ErrorController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(ErrorHandlerController.class);

    @Override
    public String getErrorPath() {
        return "/error1";
    }

    /*@RequestMapping("/error")
    public ResponseObject errorHandler(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String msg = null;
        if (statusCode == 401) {
            msg =  "/401";
        } else if (statusCode == 404) {
            msg =  "/404";
        } else if (statusCode == 403) {
            msg =  "/403";
        } else{
            msg =  "/500";
        }
        return ResponseObject.newErrorResponseObject(-1, "非法请求！url不正确，或者参数字段缺失");
    }*/

}
