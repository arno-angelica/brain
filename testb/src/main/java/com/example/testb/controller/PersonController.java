package com.example.testb.controller;

import com.example.testb.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName PersonController
 * @Description
 * @Author lingquan.liu@quvideo.com
 * @Date 2019/8/15 10:58
 */
@RestController
@RequestMapping("person")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("create/{code}")
    public String create(@PathVariable("code") String code) {
        personService.add(code);
        return "success";
    }

}
