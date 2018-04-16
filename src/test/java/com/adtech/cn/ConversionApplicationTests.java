package com.adtech.cn;

import com.adtech.cn.domain.Company;
import com.adtech.cn.mapper.CompanyMapper;
import com.adtech.cn.service.impl.CompanyServiceImpl;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConversionApplicationTests {

    @Autowired
    private CompanyServiceImpl companyService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testCompany() {
        String[] name = {"中迪", "云信", "万达", "东软", "亚德", "曼荼罗", "银海", "中联"};
        List<Company> companies = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Company company = new Company();
            company.setCompanyCode(123l);
            company.setCompanyName(name[new Random().nextInt(7)]);
            company.setCreateTime(new Date());
            companies.add(company);
        }
        CompanyMapper mockCompanyMapper = EasyMock.createMock(CompanyMapper.class);
        EasyMock.expect(mockCompanyMapper.findCompany(null)).andReturn(companies);
        EasyMock.replay(mockCompanyMapper);
        ReflectionTestUtils.setField(companyService, "companyMapper", mockCompanyMapper, CompanyMapper.class);
        String re = companyService.findCompany(1,20);
        Assert.assertNotNull(re);
        EasyMock.verify(mockCompanyMapper);
    }

}
