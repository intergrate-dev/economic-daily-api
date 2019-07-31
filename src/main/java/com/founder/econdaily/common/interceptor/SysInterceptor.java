package com.founder.econdaily.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.entity.CheckResult;
import com.founder.econdaily.common.entity.DataResult;
import com.founder.econdaily.common.util.JwtUtils;
import com.founder.econdaily.common.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class SysInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SysInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        JSONObject json = new JSONObject();
        json.put("data", new Object[]{});
        String requestURI = request.getRequestURI();
        if (handler instanceof HandlerMethod) {
            String authHeader = "prod".equals(SpringContextUtil.getActiveProfile()) ?
                    request.getHeader("token") : System.getProperty("jwt_token");
            if (StringUtils.isEmpty(authHeader)) {
                logger.info("签名不能为空");
                json.put("status", -1);
                json.put("message", "签名不能为空");
                print(response, json);
                return false;
            } else {
                //验证JWT的签名，返回CheckResult对象
                CheckResult checkResult = JwtUtils.validateJWT(authHeader);
                if (checkResult.isSuccess()) {
                    return true;
                } else {
                    switch (checkResult.getErrCode()) {
                        // 签名验证不通过
                        case SystemConstant.JWT_ERRCODE_FAIL:
                            logger.info("签名错误");
                            json.put("status", -1);
                            json.put("message", "签名错误");
                            print(response, json);
                            break;
                        // 签名过期，返回过期提示码
                        case SystemConstant.JWT_ERRCODE_EXPIRE:
                            logger.info("签名过期");
                            json.put("status", -1);
                            json.put("message", "签名过期");
                            print(response, json);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            }
        } else {
            return true;
        }
    }

    public void print(HttpServletResponse response, Object message) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setHeader("Cache-Control", "no-cache, must-revalidate");
            PrintWriter writer = response.getWriter();
            writer.write(message.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (response.getStatus() == 500) {
            //modelAndView.setViewName("/error/500");
        } else if (response.getStatus() == 404) {
            //modelAndView.setViewName("/error/404");
        }
        logger.info("uri: {}, state: {}", request.getServletPath(), response.getStatus());
        //response.sendRedirect("");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}  