package com.adtech.cn.service.impl;

import com.adtech.cn.config.MenuConfig;
import com.adtech.cn.domain.Company;
import com.adtech.cn.mapper.CompanyMapper;
import com.adtech.cn.service.IBaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/21.
 */
@Service
@Transactional
public class MenuServiceImpl implements IBaseService {
    @Autowired
    private CompanyMapper companyMapper;
    @Value("${convertUrl}")
    private String convertUrl;
    @Autowired
    private MenuConfig menuConfig;

    /**
     * 组装菜单导航
     * @return
     */
    public String getMenu() {
        Map<String, Integer> map = new HashMap<>();
        map.put("page", 1);
        map.put("rows", 5000);
        List<Company> companies = companyMapper.findCompany(map);
        List<Map<String, String>> menuList = new ArrayList<>();
        Map<String, String> menu = new HashMap<>();
        MenuConfig menuConfig1 = new MenuConfig();
        MenuConfig menuConfig2 = new MenuConfig();
        for (Company company : companies) {
            if (org.apache.commons.lang3.StringUtils.equals(company.getStatus(), "0")) continue;
            menu = new HashMap<>();
            menu.put("menu.url", convertUrl);
            menu.put("menu.menuid", company.getCompanyCode() + "");
            menu.put("menu.menuname", company.getCompanyName());
            menuList.add(menu);
        }
        menuConfig2.setMenuid("other_menu");
        menuConfig2.setMenuname("厂商编码对照");
        menuConfig2.setLists(menuList);
        BeanUtils.copyProperties(menuConfig, menuConfig1);
        Map<String, List<MenuConfig>> re = new HashMap<>();
        List<MenuConfig> menuConfigs = new ArrayList<>();
        menuConfigs.add(menuConfig1);
        menuConfigs.add(menuConfig2);
        re.put("menus", menuConfigs);
        return StringUtils.replace(gson.toJson(re), "menu.", "").toString();
    }
}
