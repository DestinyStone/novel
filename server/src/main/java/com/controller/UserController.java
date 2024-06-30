package com.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.api.R;
import com.common.exception.ServiceException;
import com.common.exception.UserExistException;
import com.common.utils.AuthUtil;
import com.entity.pojo.User;
import com.entity.vo.RegisterFormBean;
import com.entity.vo.UploadPassword;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/save")
    public R save(@RequestBody RegisterFormBean formBean, HttpServletRequest request, HttpServletResponse response) {

        try {
            // 表单数据拷贝
            User user = BeanUtil.copyProperties(formBean, User.class);
            user.setCreateTime(new Date());
            user.setStatus(1);
            user.setPassword(AuthUtil.codePassword(user.getPassword()));
            userService.register(user);
            return R.success("注册成功！！");
        } catch (UserExistException e) {
            return R.fail("用户名重复,请更换一个用户名！！");
        } catch (Exception e) {
            return R.fail("对不起，注册失败！！");
        }
    }

    @RequestMapping("/update/{id}")
    public R update(@PathVariable("id") Integer id, @RequestBody User dto) {
        User user = BeanUtil.copyProperties(dto, User.class);
        user.setUserId(id);
        if (StrUtil.isNotBlank(user.getPassword())) {
            user.setPassword(AuthUtil.codePassword(user.getPassword()));
        }
        userService.updateById(user);

        return R.success("更新成功！！");
    }

    @RequestMapping("/update-info")
    public R updateInfo(@RequestBody User dto) {
        User user = BeanUtil.copyProperties(dto, User.class);
        user.setUserId(AuthUtil.getUserId());
        userService.updateById(user);

        return R.success("更新成功！！");
    }

    @RequestMapping("/update-password")
    public R updatePassword(@RequestBody UploadPassword uploadPassword) {
        User user = userService.getById(AuthUtil.getUserId());
        uploadPassword.setNewPassword(AuthUtil.codePassword(uploadPassword.getNewPassword()));
        uploadPassword.setCurrentPassword(AuthUtil.codePassword(uploadPassword.getCurrentPassword()));
        if (!Objects.equals(user.getPassword(), uploadPassword.getCurrentPassword())) {
            throw new ServiceException("当前密码输入有误");
        }
        user.setPassword(uploadPassword.getNewPassword());
        userService.updateById(user);
        AuthUtil.clear();
        return R.success("更新成功！！");
    }

    @RequestMapping("/delete")
    public R delete(@RequestParam("id") Integer id) {
        userService.removeById(id);
        return R.success("操作成功！！");
    }

    @RequestMapping("/list")
    public R list(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(user.getAccount()), User::getAccount, user.getAccount());
        wrapper.like(StrUtil.isNotBlank(user.getEmail()), User::getEmail, user.getEmail());
        wrapper.eq(user.getStatus() != null, User::getStatus, user.getStatus());
        List<User> list = userService.list(wrapper);
        return R.data(list);
    }

    @RequestMapping("/id")
    public R id(@RequestParam("id") Integer id) {
        return R.data(userService.getById(id));
    }
}
