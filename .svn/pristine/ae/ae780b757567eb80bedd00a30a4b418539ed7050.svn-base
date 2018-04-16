package com.adtech.cn.controller;

import com.adtech.cn.dto.UpdateCompanyDTO;
import com.adtech.cn.service.impl.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2018/3/21.
 */
@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyServiceImpl companyService;

    /**
     * 业务厂商列表查询
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/queryCompanys")
    public String queryCompanys(@RequestParam int page, @RequestParam int rows) {
        String re = companyService.findCompany(page, rows);
        return re;
    }

    /**
     * 增改业务厂商
     *
     * @param updateCompanyDTO
     * @return
     */
    @RequestMapping(value = "/updateCompany")
    public String updateCompany(@RequestBody UpdateCompanyDTO updateCompanyDTO) {
        return companyService.updateCompany(updateCompanyDTO);
    }
}
