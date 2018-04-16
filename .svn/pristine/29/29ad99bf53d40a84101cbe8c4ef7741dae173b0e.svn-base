package com.adtech.cn.mapper;

import java.util.List;
import java.util.Map;

import com.adtech.cn.domain.RangeContrast;

public interface RangeContrastMapper {
	/**
	 * 根据主键ID删除数据
	 * 
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(Long id);
	/**
	 * 按业务厂商编码与平台类别编码增加一条未对照数据
	 * 
	 * @param record
	 * @return
	 */
	int insertSelective(RangeContrast record);
	/**
	 * 根据厂商编码、平台分类编码和厂商值域编码查询数据
	 * 
	 * @param map
	 * @return
	 */
	RangeContrast selectByCode(Map<String, String> map);
	/**
	 * 更新
	 * 
	 * @param record
	 * @return
	 */
	int updateByPrimaryKeySelective(RangeContrast record);
	/**
	 * 根据厂商编码、平台分类编码和厂商值域编码修改数据
	 * 
	 * @param record
	 * @return
	 */
	int updatePlatformDetail(RangeContrast record);
	/**
	 * 按业务厂商编码与平台类别编码删除
	 * 
	 * @param map
	 * @return
	 */
	int deleteByCompanyCodeAndRangeCode(Map<String, String> map);
	/**
	 * 按业务厂商编码与平台值域类别查询厂商值域对照分页数据
	 * 
	 * @param map
	 * @return
	 */
	List<RangeContrast> selectByCompanyCodeAndCompanyRangeCode(Map<String, Object> map);
	/**
	 * 搜索分类分页数据
	 * 
	 * @param map
	 * @return
	 */
	List<RangeContrast> searchRangeContrast(Map<String, Object> map);
	/**
	 * 查询所有厂商值域对照数据
	 * 
	 * @param map
	 * @return
	 */
	List<RangeContrast> findAllRangeContrast(Map<String, String> map);
	/**
	 * 按业务厂商编码与平台值域类别查询总量
	 * 
	 * @param map
	 * @return
	 */
	int countNum(Map<String, Object> map);
	/**
	 * 搜索分类总数据
	 * 
	 * @param map
	 * @return
	 */
	int searchCountNum(Map<String, Object> map);
}