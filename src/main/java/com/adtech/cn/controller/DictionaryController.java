package com.adtech.cn.controller;

import com.adtech.cn.dto.UpdateRangeClassDTO;
import com.adtech.cn.dto.UpdateRangeDetailDTO;
import com.adtech.cn.service.impl.DictionaryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台字典的Controller
 *
 * @author PYH
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryServiceImpl dictionaryService;

    /**
     * 平台值域分类查询
     *
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping(value = "/queryRangeClass")
    public String queryRangeClass(@RequestParam int page, @RequestParam int rows, @RequestParam(value = "type", required = false) Integer type,
                                  @RequestParam(value = "value", required = false) String value, @RequestParam(value = "status", required = false) Integer status) {
        return dictionaryService.searchRangeClass(page, rows, type, value, status);
    }


    /**
     * 平台值域分类明细查询
     *
     * @param page
     * @param rows
     * @param
     * @return
     */
    @RequestMapping(value = "/queryRangeDetail")
    public String queryRangeDetail(@RequestParam int page, @RequestParam int rows,
                                   @RequestParam(value = "platformCode", required = false) String platformCode) {
        return dictionaryService.findRangeDetail(page, rows, platformCode);
    }

    /**
     * 平台值域分类操作
     *
     * @param updateRangeClassDTO
     * @return
     */
    @RequestMapping(value = "/updateRangeClass")
    public String updateRangeClass(@RequestBody UpdateRangeClassDTO updateRangeClassDTO) {
        return dictionaryService.updateRangeClass(updateRangeClassDTO);
    }

    /**
     * 平台值域分类明细操作
     *
     * @param updateRangeDetailDTO
     * @return
     */
    @RequestMapping(value = "/updateRangeDetail")
    public String updateRangeDetail(@RequestBody UpdateRangeDetailDTO updateRangeDetailDTO) {
        return dictionaryService.updateRangeDetail(updateRangeDetailDTO);
    }

    /**
     * 平台值域分类下拉列数据查询
     *
     * @return
     */
    @RequestMapping(value = "/queryClassList")
    public String queryClassList() {
        return dictionaryService.findClassList();
    }

}
