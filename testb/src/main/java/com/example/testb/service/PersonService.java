package com.example.testb.service;

import com.arno.manager.annotation.Distributed;
import com.example.testb.dao.mapper.PersonMapper;
import com.example.testb.dao.model.Person;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName PersonService
 * @Description
 * @Author lingquan.liu@quvideo.com
 * @Date 2019/8/15 10:55
 */
@Service
public class PersonService {

    @Resource
    private PersonMapper personMapper;

    @Distributed(isEnd = true)
    @Transactional
    public void add(String name) {
        Person p = new Person();
        p.setCode(name);
        personMapper.insert(p);
//        int i = 100/0;
    }

}
