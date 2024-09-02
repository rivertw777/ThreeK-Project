package com.ThreeK_Project.api_server.customMockUser;

import com.ThreeK_Project.api_server.domain.user.enums.Role;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithCustomMockUserSecurityContextFactory.class)
public @interface WithCustomMockUser {
    String username() default "username";
    String password() default "123456";
    Role[] roles() default {Role.CUSTOMER};
    String phoneNumber() default "01012345678";
    String address() default "address";
}
