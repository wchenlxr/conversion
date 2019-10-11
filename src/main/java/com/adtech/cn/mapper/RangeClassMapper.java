package com.adtech.cn.mapper;

import java.util.List;
import java.util.Map;

import com.adtech.cn.domain.RangeClass;
/**
 * 平台值域分类DAO
 * @author PYH
 *
 */
public interface RangeClassMapper {
	/**
	 * 根据ID删除平台值域分类
	 * @param id
	 * @return
	 */
    int deleteByPrimaryKey(Long id);
    /**
     * 新增平台值域分类
     * @param record
     * @return
     */
    int insertSelective(RangeClass record);
    /**
     * 根据分类Code查询数据
     * @param platformCode
     * @return
     */
    RangeClass selectByCode(String platformCode);
    /**
     * 修改平台值域分类
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(RangeClass record);
    /**
     * 查询分类下拉列数据
     * @return
     */
    List<Map<String, String>> findClassList();
    /**
     * 搜索分类数据
     * @param map
     * @return
     */
    List<RangeClass> searchClass(Map<String,Object> map);
    /**
     * 查询分类总数据
     * @param map
     * @return
     */
    int searchCountNum(Map<String,Object> map);
}