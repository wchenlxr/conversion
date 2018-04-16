package com.adtech.cn.mapper;

import com.adtech.cn.domain.Company;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 业务厂商dao
 * Created by wchen on 2018/3/16.
 */
@Repository
public interface CompanyMapper {
    /**
     * 查询所有业务厂商
     * @param map
     * @return
     */
    List<Company> findCompany(Map map);

    /**
     * 查询所有业务厂商数量
     * @return
     */
    int countNum();

    /**
     * 增加业务厂商
     * @param map
     * @return
     */
    int insertOne(Map map);

    /**
     * 批量增加业务厂商
     * @param map
     * @return
     */
    int insertList(Map map);

    /**
     * 更新业务厂商
     * @param map
     * @return
     */
    int updateOne(Map map);

    /**
     * 按业务厂商编码删除
     * @param companyCode
     * @return
     */
    int deleteOne(Long companyCode);

    /**
     * 按业务厂商名称查询
     * @param companyName
     * @return
     */
    List<Company> selectByName(String companyName);
    /**
     * 根据厂商编码查询数据
     * @param companyCode
     * @return
     */
    Company findByCompanyCode(Long companyCode);
}
