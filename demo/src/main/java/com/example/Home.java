package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@EnableAutoConfiguration
public class Home {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(greetingH.class, args);
    }
}