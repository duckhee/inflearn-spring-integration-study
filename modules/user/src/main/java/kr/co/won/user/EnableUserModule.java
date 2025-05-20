package kr.co.won.user;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableUserModule.UserModuleConfiguration.class)
public @interface EnableUserModule {

    @Configuration
    @ComponentScan
    class UserModuleConfiguration {
    }
}
