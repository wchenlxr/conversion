package com.adtech.cn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转,以后可以不用,静态文件移至nodejs或者nginx
 * Created by Administrator on 2018/3/20.
 */
@Controller
public class PageController {
    /**
     * 门户
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/index", "/"})
    public String indexPage(Model model) {
        model.addAttribute("name", "Dear");
        return "index";
    }

    /**
     * 业务厂商列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/companyList")
    public String companyListPage(Model model) {
        model.addAttribute("companys", "s");
        return "company";
    }

    /**
     * 平台字典列表
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/dictionaryList")
    public String dictionaryListPage(Model model) {
        model.addAttribute("dictionary", "s");
        return "dictionary";
    }

    /**
     * 值域导入
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/dataimport")
    public String dataimportPage(Model model) {
        model.addAttribute("dictionary", "s");
        return "dataimport";
    }

    /**
     * 业务厂商对照
     *
     * @param model
     * @return
     */
    @RequestMapping(value = "/convert")
    public String convertPage(Model model) {
        return "convert";
    }
}
