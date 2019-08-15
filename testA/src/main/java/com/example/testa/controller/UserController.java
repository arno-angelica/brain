package com.example.testa.controller;

import com.example.testa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description
 * @Author lingquan.liu@quvideo.com
 * @Date 2019/8/15 10:20
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("add/{name}")
    public String add(@PathVariable("name") String name) {
        userService.test1(name);
        return "success";
    }

}
