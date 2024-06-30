package com.controller;

import com.common.api.R;
import com.entity.pojo.Admin;
import com.entity.vo.ReportVO;
import com.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author zhou xiao fen
 * @Date 2024/2/17 15:34
 **/
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AdminService adminService;

    @Value("${spring.mail.nickname}")
    private String nickname;

    /**
     * 发送的邮箱地址
     */
    @Value("${spring.mail.username}")
    private String from;

    @PostMapping("/submit")
    public R submit(@RequestBody ReportVO vo) {
        List<Admin> list = adminService.list();
        for (Admin admin : list) {
            Runnable runnable = () -> {
                //创建SimpleMailMessage对象
                SimpleMailMessage message = new SimpleMailMessage();
                //邮件发送人
                message.setFrom(nickname + '<' + from + '>');
                //邮件接收人
                message.setTo(admin.getEmail());
                //邮件主题
                message.setSubject("评论举报");
                //邮件内容
                String content = "评论举报：\n" +
                        "小说ID: %s\n" +
                        "小说标题: %s\n" +
                        "评论用户: %s\n" +
                        "举报评论: %s\n" +
                        "举报原因: %s\n"
                        ;
                content = String.format(content,
                        vo.getNovelId(),
                        vo.getTitle(),
                        vo.getAccount(),
                        vo.getContent(),
                        vo.getRemark()
                );
                message.setText(content);
                //发送邮件
                mailSender.send(message);
            };
            new Thread(runnable).start();
        }

        return R.success("");
    }


}
