package com.springboot.study.security_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping
public class SecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityDemoApplication.class, args);
    }

    @GetMapping
    public String index(){
        return "Welcome !";
    }

    @GetMapping("role")
    @PreAuthorize("hasRole('USER')")
    public String role(){
        return "role !";
    }

    @GetMapping("permission")
    @PreAuthorize("hasRole('asdasd')")
    public String permission(){
        return "permission !";
    }

    @GetMapping("test")
   // @PreAuthorize("hasRole('qweqwe')")
    public String roleAndPermission(){
        return "roleAndPermission !";
    }
}

