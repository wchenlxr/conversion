package com.adtech.cn.service.impl;

import com.adtech.cn.domain.Company;
import com.adtech.cn.dto.UpdateCompanyDTO;
import com.adtech.cn.exception.ApplicationException;
import com.adtech.cn.exception.SystemError;
import com.adtech.cn.mapper.CompanyMapper;
import com.adtech.cn.service.IBaseService;
import com.adtech.cn.utils.IdWorker;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wchen on 2018/3/16.
 */
@Service
@Transactional
public class CompanyServiceImpl implements IBaseService {
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询业务厂商
     *
     * @param page
     * @param rows
     * @return
     */
    public String findCompany(int page, int rows) {
        Map<String, Integer> map = new HashMap<>();
        Map<String, Object> re = new HashMap<>();
        map.put("page", page);
        map.put("rows", rows);
        int total = companyMapper.countNum();
        List<Company> companyList = companyMapper.findCompany(map);
        re.put("total", total);
        re.put("rows", companyList);
        return gson.toJson(re);
    }

    /**
     * 更新业务厂商
     *
     * @param updateCompanyDTO
     * @return
     */
    public String updateCompany(UpdateCompanyDTO updateCompanyDTO) {
        Map<String, List<Company>> map = new HashMap<>();
        List<Company> insertCompanys = updateCompanyDTO.getInserted();
        List<Company> deleteCompanys = updateCompanyDTO.getDeleted();
        List<Company> updateCompanys = updateCompanyDTO.getUpdated();
        if (CollectionUtils.isNotEmpty(insertCompanys)) {
            for (Company company : insertCompanys) {
                List<Company> companyList = companyMapper.selectByName(company.getCompanyName());
                if (CollectionUtils.isNotEmpty(companyList)) {
                    SystemError error = SystemError.DB_CANNOT_REPEAT;
                    throw new ApplicationException(error);
                }
                company.setCompanyCode(idWorker.getId());
                company.setCreateTime(new Date());
                map = gson.fromJson(gson.toJson(company), new TypeToken<Map<String, Object>>() {
                }.getType());
                companyMapper.insertOne(map);
            }
        }
        if (CollectionUtils.isNotEmpty(deleteCompanys)) {
            for (Company company : deleteCompanys) {
                companyMapper.deleteOne(company.getCompanyCode());
            }
        }
        if (CollectionUtils.isNotEmpty(updateCompanys)) {
            for (Company company : updateCompanys) {
                List<Company> companyList = companyMapper.selectByName(company.getCompanyName());
                for (Company company1 : companyList) {
                    if (company1.getCompanyCode().longValue() != company.getCompanyCode().longValue() && StringUtils.equals(company1.getCompanyName(), company.getCompanyName())) {
                        SystemError error = SystemError.DB_CANNOT_REPEAT;
                        throw new ApplicationException(error);
                    }
                }
                company.setUpdateTime(new Date());
                map = gson.fromJson(gson.toJson(company), new TypeToken<Map<String, Object>>() {
                }.getType());
                companyMapper.updateOne(map);
            }
        }
        return "ok";
    }


}
