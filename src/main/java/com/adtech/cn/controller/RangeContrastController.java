package com.adtech.cn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adtech.cn.dto.UpdateContrastDTO;
import com.adtech.cn.dto.UpdateRangeContrastDTO;
import com.adtech.cn.service.impl.RangeContrastServiceImpl;

/**
 * 业务厂商对照 Created by Administrator on 2018/3/23.
 */
@RestController
@RequestMapping("contrast")
public class RangeContrastController {
    @Autowired
    private RangeContrastServiceImpl rangeContrastService;

    /**
     * 转码匹配分页数据
     *
     * @param companyCode
     * @param platformRangeCode
     * @return
     */
    @RequestMapping("/getContrast")
    public String getContrast(@RequestParam String companyCode, @RequestParam String platformRangeCode,
                              @RequestParam String companyRangeCode, @RequestParam(value = "page", required = false) Integer page,
                              @RequestParam(value = "rows", required = false) Integer rows) {
        return rangeContrastService.getContrast(page, rows, companyCode, platformRangeCode, companyRangeCode);
    }

    /**
     * 搜索分类分页数据
     *
     * @param companyCode
     * @param platformRangeCode
     * @param page
     * @param rows
     * @param type
     * @param value
     * @param ppFlag
     * @return
     */
    @RequestMapping("/searchRangeContrast")
    public String searchRangeContrast(
            @RequestParam(value = "companyCode", required = false, defaultValue = "") String companyCode,
            @RequestParam(value = "platformRangeCode", required = false, defaultValue = "") String platformRangeCode,
            @RequestParam Integer page, @RequestParam Integer rows,
            @RequestParam(value = "type", required = false) Integer type,
            @RequestParam(value = "value", required = false) String value,
            @RequestParam String ppFlag) {
        return rangeContrastService.searchRangeContrast(page, rows, companyCode, platformRangeCode, type, value, ppFlag);
    }

    /**
     * 厂商值域对照操作
     *
     * @param updateRangeContrastDTO
     * @return
     */
    @RequestMapping(value = "/updateRangeContrast")
    public String updateRangeContrast(@RequestBody UpdateRangeContrastDTO updateRangeContrastDTO) {
        return rangeContrastService.updateRangeContrast(updateRangeContrastDTO);
    }

    /**
     * 转码匹配操作
     *
     * @param updateContrastDTO
     * @return
     */
    @RequestMapping(value = "/updateContrast")
    public String updateContrast(@RequestBody UpdateContrastDTO updateContrastDTO) {
        return rangeContrastService.updateContrast(updateContrastDTO);
    }

    /**
     * 转码
     *
     * @param companyCode
     * @param platformRangeCode
     * @return
     */
    @RequestMapping(value = "/transcode")
    public String transcode(@RequestParam String companyCode, @RequestParam String platformRangeCode) {
        return rangeContrastService.transcode(companyCode, platformRangeCode);
    }

    /**
     * 清空选中对照
     *
     * @param id
     * @return
     */

    @RequestMapping(value = "/clear/{id}")
    public String clearOne(@PathVariable Long id) {
        return rangeContrastService.clearOne(id);
    }
}
