package com.bishe.visualizationsystem.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bishe.visualizationsystem.admin.bean.Users;
import com.bishe.visualizationsystem.admin.mapper.UsersMapper;
import com.bishe.visualizationsystem.admin.service.UsersService;
import org.springframework.stereotype.Service;

/**
 * @author Joseph
 * @date 2021/12/25
 * @apiNote
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
