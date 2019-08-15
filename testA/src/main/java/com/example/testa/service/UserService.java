package com.example.testa.service;

import com.arno.manager.annotation.Distributed;
import com.arno.manager.util.HttpClient;
import com.example.testa.dao.mapper.UserInfoMapper;
import com.example.testa.dao.model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @ClassName UserService
 * @Description
 * @Author lingquan.liu@quvideo.com
 * @Date 2019/8/15 10:14
 */
@Slf4j
@Service
public class UserService {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Distributed(isStart = true)
    @Transactional
    public void test1(String name) {
        UserInfo info = new UserInfo();
        info.setName(name);
        userInfoMapper.insert(info);
        String resp = HttpClient.get("http://localhost:8015/person/create/" + name);
        log.info("response => {}", resp);
    }

}
