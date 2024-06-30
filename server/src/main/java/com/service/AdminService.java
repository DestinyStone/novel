package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entity.pojo.Admin;


public interface AdminService extends IService<Admin> {
    Admin login(String account, String password);
}
