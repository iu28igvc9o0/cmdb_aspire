package com.aspire.ums.cmdb.example.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aspire.ums.cmdb.example.entity.User;
import com.aspire.ums.cmdb.example.service.UserService;

@RefreshScope
@RestController
@RequestMapping("/cmdb/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Value("${iconSize}")
    private String iconSize;

    @RequestMapping("/getUsers")
    public List<User> getUsers() {
//        List<User> users = userService.getAll();
        return userService.getAll();
    }

    @RequestMapping("/getUser")
    public User getUser(Long id) {
//        User user = userService.getOne(id);
        return userService.getOne(id);
    }

    @RequestMapping("/add")
    public void save(User user) {
        userService.insert(user);
    }

    @RequestMapping(value = "/update")
    public void update(User user) {
        userService.update(user);
    }

    @RequestMapping(value = "/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

    @RequestMapping("/test")
    public String from() {
        logger.info("请求测试");
        logger.debug("请求测试");
        logger.error("请求测试");
        return "1";
    }
}
