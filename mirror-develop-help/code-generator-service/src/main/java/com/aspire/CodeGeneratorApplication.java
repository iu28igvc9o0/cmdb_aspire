package com.aspire;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class CodeGeneratorApplication {
    public static void main(final String[] args) {
        SpringApplication.run(CodeGeneratorApplication.class, args);
    }
}