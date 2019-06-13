package com.founder.econdaily.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateParam {
    String module()  default "日志模块";

    String desc()  default "无动作";
}
