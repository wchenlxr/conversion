package com.adtech.cn.mapper;

import java.util.List;
import java.util.Map;

import com.adtech.cn.domain.Contrast;
import com.adtech.cn.domain.RangeClass;

/**
 * 值域匹配表DAO
 * @author PYH
 *
 */
public interface ContrastMapper {
	/**
	 * 按业务厂商编码、平台分类编码或厂商值域编码删除匹配临时表数据
	 * @param map
	 * @return
	 */
	int deleteByCode(Map<String, String> map);
	/**
	 * 新增数据
	 * @param record
	 * @return
	 */
    int insertSelective(Contrast record);
    /**
	 * 查询转码匹配总数据
	 * 
	 * @param map
	 * @return
	 */
	int countNum(Map<String, Object> map);
	/**
	 * 查询转码匹配分页数据
	 * 
	 * @param map
	 * @return
	 */
	List<Contrast> findContrast(Map<String, Object> map);
	/**
     * 修改转码匹配选中状态
     * @param record
     * @return
     */
    int updateSelected(Contrast record);

	/**
	 * 所有临时排行数据先未选中
	 * @param map
	 * @return
	 */
	int updateOthers(Map map);
}