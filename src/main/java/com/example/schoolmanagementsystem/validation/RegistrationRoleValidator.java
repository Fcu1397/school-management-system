package com.example.schoolmanagementsystem.validation;

import com.example.schoolmanagementsystem.dto.request.UserRegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * 自訂驗證器實作
 * 根據角色來驗證 studentId 或 teacherId
 */
public class RegistrationRoleValidator implements ConstraintValidator<ValidRegistrationRole, UserRegistrationRequest> {

    @Override
    public void initialize(ValidRegistrationRole constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserRegistrationRequest request, ConstraintValidatorContext context) {
        boolean isValid = true;

        if (request.getRoleName() == null) {
            return false; // 角色為空，由 @NotBlank 處理
        }

        context.disableDefaultConstraintViolation(); // 禁用預設的錯誤訊息

        if ("STUDENT".equals(request.getRoleName())) {
            if (!StringUtils.hasText(request.getStudentId())) {
                context.buildConstraintViolationWithTemplate("學號不可為空")
                        .addPropertyNode("studentId").addConstraintViolation();
                isValid = false;
            } else if (!request.getStudentId().matches("^[A-Z]\\d{8}$")) {
                context.buildConstraintViolationWithTemplate("學號格式不正確（例：B10901234）")
                        .addPropertyNode("studentId").addConstraintViolation();
                isValid = false;
            }
        } else if ("TEACHER".equals(request.getRoleName())) {
            if (!StringUtils.hasText(request.getTeacherId())) {
                context.buildConstraintViolationWithTemplate("教職員編號不可為空")
                        .addPropertyNode("teacherId").addConstraintViolation();
                isValid = false;
            } else if (!request.getTeacherId().matches("^T\\d{6}$")) {
                context.buildConstraintViolationWithTemplate("教職員編號格式不正確（例：T123456）")
                        .addPropertyNode("teacherId").addConstraintViolation();
                isValid = false;
            }
        }

        return isValid;
    }
}