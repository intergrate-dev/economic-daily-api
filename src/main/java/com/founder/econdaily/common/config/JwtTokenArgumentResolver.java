package com.founder.econdaily.common.config;

import com.founder.econdaily.common.annotation.JwtToken;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.common.util.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;

public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        /*HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authorization = request.getHeader("Authorization");
        String result = null;
        JwtToken token = null;
        if(authorization!=null){
            Annotation[] methodAnnotations = parameter.getParameterAnnotations();
            for (Annotation methodAnnotation : methodAnnotations) {
                if(methodAnnotation instanceof JwtToken){
                    token = (JwtToken) methodAnnotation;
                    break;
                }
            }
            if(token!=null){
                // result = JwtUtils.get(authorization,token.value());
            }
        }*/
        Method method = parameter.getMethod();
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (Class<?> parameterType : parameterTypes) {
            String name = parameterType.getName();
        }
        BindingResult validResult = null;
        Annotation[] methodAnnotations = parameter.getParameterAnnotations();
        for (Annotation methodAnnotation : methodAnnotations) {
            /*if (methodAnnotation instanceof ValidateParam) {
                //validResult = (BindingResult) methodAnnotation;
                break;
            }*/
        }
        if (validResult != null) {
            if (validResult.hasErrors()) {
                return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, this.validErrorMsg(validResult));
            }
        }
        return "ijijijijiji";
        /*Map<String, String> map = new HashMap<String, String>();
        map.put("data", "45435r34retert");
        return JSON.toJSON(map).toString();*/
    }


    public String validErrorMsg(BindingResult validResult) {
        List<ObjectError> list = validResult.getAllErrors();
        for (ObjectError objectError : list) {
            logger.info(objectError.toString());
        }
        return RegxUtil.extractTargetBetweenSymbolRange(validResult.toString(), RegxUtil.REG_ZH, RegxUtil.REG_TX);
    }
}
