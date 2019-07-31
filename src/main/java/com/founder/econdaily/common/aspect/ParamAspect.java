package com.founder.econdaily.common.aspect;

// import com.founder.ark.common.utils.bean.ResponseObject;
import com.founder.econdaily.common.constant.SystemConstant;
import com.founder.econdaily.common.util.DateParseUtil;
import com.founder.econdaily.common.util.RegxUtil;
import com.founder.econdaily.common.util.ResponseObject;
import com.founder.econdaily.modules.magazine.entity.BaseParam;
import com.founder.econdaily.modules.magazine.entity.CommonParam;
import com.founder.econdaily.modules.magazine.entity.MagazineParamDL;
import com.founder.econdaily.modules.magazine.entity.NewsPaperParamDL;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Aspect
@Component
public class ParamAspect {
    private static final Logger logger = LoggerFactory.getLogger(ParamAspect.class);

    @Pointcut("@annotation(com.founder.econdaily.common.annotation.Validate)")
    public void log() {
    }

    //@Before("log()&&@annotation(validateParam)")
    // @Before("log()")
    //public void doBefore(JoinPoint joinPoint) {
    public ResponseObject doBefore(JoinPoint joinPoint) {
        return null;
    }

    @Around("log()")
    public Object doAround(ProceedingJoinPoint proceeJoinPoint) throws Throwable {
        logger.info("=============== before, target className: {},  method: {} ======================== ", proceeJoinPoint.getSignature().getDeclaringType()
                .getSimpleName(), proceeJoinPoint.getSignature().getName());
        Object[] args = proceeJoinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof BindingResult) {
                BindingResult validResult = (BindingResult) arg;
                if (validResult.hasErrors()) {
                    return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, validErrorMsg(validResult));
                }
            }
            if (arg instanceof BaseParam){
                BaseParam cp = (BaseParam) arg;
                // String regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}\\s+([0-1]?[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])$";
                String regex = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";
                if (!StringUtils.isEmpty(cp.getBeginTime())) {
                    if (!Pattern.matches(regex,cp.getBeginTime())) {
                        return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, "参数: startTime 日期格式不正确！");
                    }
                }
                if (!StringUtils.isEmpty(cp.getEndTime())) {
                    if (!Pattern.matches(regex,cp.getEndTime())) {
                        return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, "参数: endTime 日期格式不正确！");
                    }
                }
                if (!StringUtils.isEmpty(cp.getBeginTime()) && !StringUtils.isEmpty(cp.getEndTime())) {
                    try {
                        Date beginDate = DateParseUtil.stringToDate(cp.getBeginTime());
                        Date endDate = DateParseUtil.stringToDate(cp.getEndTime());
                        if (beginDate.getTime() > endDate.getTime()) {
                            return ResponseObject.newErrorResponseObject(SystemConstant.REQ_ILLEGAL_CODE, "结束日期不能小于开始日期！");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return proceeJoinPoint.proceed();
    }

    // @After("log()")
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
