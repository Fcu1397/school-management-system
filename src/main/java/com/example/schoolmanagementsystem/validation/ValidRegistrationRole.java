package com.example.schoolmanagementsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自訂驗證註解
 * 用於驗證使用者註冊時，根據不同角色（學生、教師）檢查對應的 ID 是否符合規則
 */
@Constraint(validatedBy = RegistrationRoleValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRegistrationRole {
    String message() default "無效的註冊資料：學號或教職員編號不符合要求";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
