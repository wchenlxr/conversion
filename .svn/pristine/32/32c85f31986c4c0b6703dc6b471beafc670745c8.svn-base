package com.adtech.cn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * 导航菜单目前暂放application.yml文件
 * Created by Administrator on 2018/3/21.
 */
@Configuration
@ConfigurationProperties(prefix = "menus")
public class MenuConfig {
    private String menuid;
    private String menuname;
    private List<Map<String, String>> lists;

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public List<Map<String, String>> getLists() {
        return lists;
    }

    public void setLists(List<Map<String, String>> lists) {
        this.lists = lists;
    }
}
