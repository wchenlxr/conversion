package com.adtech.cn.controller;

import com.adtech.cn.service.impl.MenuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2018/3/21.
 */
@RestController
public class MenuController {

    @Autowired
    private MenuServiceImpl menuService;

    /**
     * 获取菜单导航
     * @return
     */
    @RequestMapping("/getMenu")
    public String getMenu() {
        return menuService.getMenu();
    }
}
