package com.founder.econdaily.common.aspect;

import com.founder.ark.common.utils.bean.ResponseObject;
import com.founder.econdaily.common.annotation.ValidateParam;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.controller.BaseController;
import com.founder.econdaily.common.util.RegxUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.lang.reflect.Modifier;
import java.util.List;

@Aspect
@Component
public class ParamAspect {
    private static final Logger logger = LoggerFactory.getLogger(ParamAspect.class);

    @Pointcut("@annotation(com.founder.econdaily.common.annotation.Validate)")
    public void log() {
    }

    //@Before("log()&&@annotation(validateParam)")
    @Before("log()")
    //public void doBefore(JoinPoint joinPoint) {
    public ResponseObject doBefore(JoinPoint joinPoint) {
        System.out.println("******拦截前的逻辑******");
        System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
        System.out.println("目标方法所属类的简单类名:" + joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult validResult = (BindingResult) arg;
                if (validResult.hasErrors()) {
                    return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, validErrorMsg(validResult));
                }
            }
        }
        /*for (int i = 0; i < args.length; i++) {
            System.out.println("第" + (i + 1) + "个参数为:" + args[i]);
            if (args[i])
        }*/
        return null;
    }

    @Around("log()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("环绕通知：");
        Object result = null;
        result = proceedingJoinPoint.proceed();
        return result;
    }

    @After("log()")
    public void doAfter(JoinPoint joinPoint) {
        System.out.println("******拦截后的逻辑******");
    }

    public String validErrorMsg(BindingResult validResult) {
        List<ObjectError> list = validResult.getAllErrors();
        for (ObjectError objectError : list) {
            logger.info(objectError.toString());
        }
        return RegxUtil.extractTargetBetweenSymbolRange(validResult.toString(), RegxUtil.REG_ZH, RegxUtil.REG_TX);
    }

    protected String extractPath(String code, String dateStr, String rootPath, Boolean isDelete) {
        return rootPath.concat(code).concat(RegxUtil.PATH_SPLIT).concat(RegxUtil.replaceAllToPath(dateStr, isDelete)).
                concat(RegxUtil.PATH_SPLIT);
    }
}
