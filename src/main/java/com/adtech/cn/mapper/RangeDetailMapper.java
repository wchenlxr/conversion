package com.adtech.cn.mapper;

import java.util.List;
import java.util.Map;

import com.adtech.cn.domain.RangeDetail;
/**
 * 平台值域分类明细DAO
 * @author PYH
 *
 */
public interface RangeDetailMapper {
	/**
	 * 根据主键ID删除数据
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Long id);
    /**
     * 新增数据
     * @param record
     * @return
     */
    int insertSelective(RangeDetail record);
    /**
     * 修改数据
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(RangeDetail record);
    /**
     * 查询分类明细总数据
     * @param platformCode
     * @return
     */
    int countNum(String platformCode);
    /**
     * 查询分类明细分页数据
     * @param map
     * @return
     */
    List<RangeDetail> findDetail(Map<String,Object> map);
    /**
     * 查询所有未索引的明细数据
     * @return
     */
    List<RangeDetail> findAllDetail(Map<String,Object> map);
    /**
     * 按平台分类编码删除值域表数据
     * @param platformCode
     * @return
     */
    int deleteByCode(String platformCode);
    /**
     * 根据分类编码和分类值域编码查询数据
     * @param map
     * @return
     */
    RangeDetail selectByCode(Map<String,String> map);
}