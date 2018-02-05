package jzm.micro.study.service.impl;

import jzm.micro.study.dao.UserMapper;
import jzm.micro.study.model.User;
import jzm.micro.study.service.UserService;
import jzm.micro.study.core.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/02/05.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<User> implements UserService {
    @Resource
    private UserMapper userMapper;

}