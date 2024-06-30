package com.controller;

import com.common.api.R;
import com.common.exception.ServiceException;
import com.common.utils.AuthUtil;
import com.entity.pojo.Admin;
import com.entity.pojo.User;
import com.entity.vo.Auth;
import com.entity.vo.LoginFormBean;
import com.service.AdminService;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/current")
    public R current() {
        try {
            Auth user = AuthUtil.getUser();
            return R.data(user);
        }catch (Exception e) {
            return R.data(null);
        }
    }

    @GetMapping("/loginOut")
    public R loginOut() {
        AuthUtil.clear();
        return R.success("操作成功");
    }

    @PostMapping("/login")
    public R login(@RequestBody LoginFormBean formBean) {
        AuthUtil.clear();
        formBean.setPassword(AuthUtil.codePassword(formBean.getPassword()));
        // 登录操作
        boolean isLogin = false;
        if (formBean.getStatus().equals("user")) {
            isLogin = userLogin(formBean);
        } else if (formBean.getStatus().equals("admin")) {
            isLogin = adminLogin(formBean);
        }

        return isLogin ? R.success("登录成功") : R.fail("用户名或密码错误");
    }


    /**
     * 管理员登录
     */
    private boolean adminLogin(LoginFormBean formBean) {
        Admin admin = adminService.login(formBean.getAccount(), formBean.getPassword());

        if (admin != null) {
            Auth auth = new Auth();
            auth.setId(admin.getAdminId());
            auth.setAccount(formBean.getAccount());
            auth.setPassword(formBean.getPassword());
            auth.setRole(formBean.getStatus());
            AuthUtil.setToken(auth);
            return true;
        }

        return false;
    }

    /**
     * 用户登录
     */
    private boolean userLogin(LoginFormBean formBean) {
        User user = userService.login(formBean.getAccount(), formBean.getPassword());

        if (user == null) {
            return false;
        }

        if (Objects.equals(user.getStatus(), 0)) {
            throw new ServiceException("用户已被禁用， 无法登录， 解禁请联系管理员");
        }

        Auth auth = new Auth();
        auth.setId(user.getUserId());
        auth.setAccount(formBean.getAccount());
        auth.setPassword(formBean.getPassword());
        auth.setRole(formBean.getStatus());
        AuthUtil.setToken(auth);
        return true;
    }

}
