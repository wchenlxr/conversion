package com.adtech.cn.service.impl;

import com.adtech.cn.domain.Company;
import com.adtech.cn.domain.RangeClass;
import com.adtech.cn.domain.RangeContrast;
import com.adtech.cn.domain.RangeDetail;
import com.adtech.cn.exception.ApplicationException;
import com.adtech.cn.exception.SystemError;
import com.adtech.cn.mapper.CompanyMapper;
import com.adtech.cn.mapper.RangeClassMapper;
import com.adtech.cn.mapper.RangeContrastMapper;
import com.adtech.cn.mapper.RangeDetailMapper;
import com.adtech.cn.service.IBaseService;
import com.adtech.cn.service.support.DeleteIndexTask;
import com.adtech.cn.utils.IdWorker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.IndexWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2018/3/19.
 */
@Service
@Transactional
public class FileServiceImpl implements IBaseService {

    @Autowired
    private RangeClassMapper rangeClassMapper;
    @Autowired
    private RangeDetailMapper rangeDetailMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private RangeContrastMapper rangeContrastMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private DictionaryServiceImpl dictionaryService;
    @Autowired
    private IndexWriter indexWriter;

    /**
     * 值域导入并入库(包括平台值域分类，平台分类值域，接入业务厂商值域)
     *
     * @param file
     * @param code
     * @return
     */
    public String templateUpload(MultipartFile file, String code) {
        //平台分类代码
        Map<String, String> ptfldm = new HashMap<>();
        //平台分类值域
        Map<String, Map<String, String>> ptflzy = new HashMap<>();
        //业务值域
        Map<String, Map<String, String>> ywzy = new HashMap<>();
        Map<String, String> map = null;
//        if (code.contentEquals("2800")) return "";
//        System.out.println(file.getOriginalFilename() + " code=" + code);
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = null;
            try {
                //2003
                workbook = new HSSFWorkbook(inputStream);
            } catch (Exception e) {
                try {
                    //2007
                    workbook = new XSSFWorkbook(file.getInputStream());
                } catch (Exception e1) {
                    SystemError error = SystemError.SYSTEM_ERROR;
                    error.setText(e1.getMessage());
                    throw new ApplicationException(error);
                }
            }

            Iterator workBookIterator = workbook.sheetIterator();
            while (workBookIterator.hasNext()) {
                Sheet sheet = (Sheet) workBookIterator.next();
                String sheetName = sheet.getSheetName().trim();
                //模版使用说明　跳过
                if (StringUtils.equals(sheetName, "模版使用说明")) continue;
                Iterator rowIterator = sheet.rowIterator();
                Boolean firstCellIsCode = false;
                map = new HashMap<>();
                int i = 1;
                while (rowIterator.hasNext()) {
                    Row row = (Row) rowIterator.next();
                    Iterator cellIterator = row.cellIterator();
                    String valueCell2 = null;
                    int j = 1;//第一列
                    while (cellIterator.hasNext()) {
                        Cell titleCell = (Cell) cellIterator.next();
                        titleCell.setCellType(CellType.STRING);
                        String cellName = titleCell.getStringCellValue().trim();
                        String value = null;
                        titleCell.setCellType(CellType.STRING);
                        value = titleCell.getStringCellValue();
                        if (j == 1 && i == 1) {
                            if (StringUtils.equals(cellName, "分类代码") || StringUtils.equals(cellName, "值代码")) {
                                //第一列为编码
                                firstCellIsCode = true;
                            }
                        }
                        if (j == 1) {
                            if (i > 1) {
                                valueCell2 = value;
                                if (firstCellIsCode) {
                                    //第一列为编码
                                    map.put(value, null);
                                }
                            }
                        } else if (j == 2) {
                            if (i > 1)
                                if (firstCellIsCode) {
                                    //第一列为编码
                                    map.put(valueCell2, value);
                                } else {
                                    //第二列为名称
                                    map.put(value, valueCell2);
                                }
                        }
                        j++;
                        if (j >= 3) break;
                    }
                    i++;
                }
                if (StringUtils.equals(sheetName, "平台分类代码")) {
                    ptfldm.putAll(map);
                }
                String[] sheetNames = StringUtils.split(sheetName, "-", 2);
                if (sheetNames.length == 2) {
                    if (StringUtils.equals(sheetNames[0], "平台分类值域")) {
                        ptflzy.put(sheetNames[1], map);
                    } else {
                        //其它业务系统值域
                        ywzy.put(sheetName, map);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            SystemError error = SystemError.EXCEL_ERROR;
            throw new ApplicationException(error);
        }
        //平台分类编码
        if (MapUtils.isNotEmpty(ptfldm)) {
            Set<String> ptflbmSet = ptfldm.keySet();
            for (String ptflbm : ptflbmSet) {
                if (StringUtils.isEmpty(ptflbm.trim())) continue;
                String ptflname = ptfldm.get(ptflbm);
                RangeClass rangeClass = new RangeClass();
                rangeClass.setCreateTime(new Date());
                rangeClass.setId(idWorker.getId());
                rangeClass.setStatus(new BigDecimal(1));
                rangeClass.setPlatformCode(ptflbm);
                rangeClass.setPlatformName(ptflname);
                //平台分类保存
                RangeClass findBean = rangeClassMapper.selectByCode(ptflbm);
                if (findBean != null) {
                    rangeClass.setCreateTime(null);
                    rangeClass.setUpdateTime(new Date());
                    rangeClass.setId(findBean.getId());
                    rangeClassMapper.updateByPrimaryKeySelective(rangeClass);
                } else {
                    rangeClassMapper.insertSelective(rangeClass);
                }
            }
        }
        //平台分类值域 平台分类编码不存在时不导入明细 先删除平台原有值域明细
        if (MapUtils.isNotEmpty(ptflzy)) {
            Set<String> ptflbmSet = ptflzy.keySet();
            Map<String, Object> map1 = new HashMap<>();
            for (String ptflbm : ptflbmSet) {
                Map<String, String> detailMap = ptflzy.get(ptflbm);
                Set<String> detailKey = detailMap.keySet();
                RangeClass rangeClass = rangeClassMapper.selectByCode(ptflbm);
                if (rangeClass == null) continue;
                rangeClass.setUpdateTime(new Date());
                rangeClass.setId(rangeClass.getId());
                rangeClassMapper.updateByPrimaryKeySelective(rangeClass);
                //删除原索引
                map1.put("platformCode", ptflbm);
                List<RangeDetail> rangeDetails = rangeDetailMapper.findAllDetail(map1);
                List<String> ids = new ArrayList<>();
                for (RangeDetail detail : rangeDetails) {
                    ids.add(detail.getId() + "");
                }
                if (CollectionUtils.isNotEmpty(ids))
                    new DeleteIndexTask(indexWriter, ids.toArray(new String[]{}));
                //删除原有值域
                rangeDetailMapper.deleteByCode(ptflbm);
                for (String detailCode : detailKey) {
                    if (StringUtils.isEmpty(detailCode.trim())) continue;
/*                 RangeDetail rangeDetailExisted = rangeDetailMapper.selectByCode(map1);
                   if (rangeDetailExisted != null && StringUtils.equals(rangeDetailExisted.getDetailName().trim(), detailMap.get(detailCode).trim()))
                        continue;*/
                    RangeDetail rangeDetail = new RangeDetail();
                    rangeDetail.setPlatformCode(ptflbm);
                    rangeDetail.setDetailCode(detailCode);
                    rangeDetail.setDetailName(detailMap.get(detailCode));
                    rangeDetail.setId(idWorker.getId());
                    rangeDetail.setIndexStatus(0);
                    //保存值域
                    rangeDetailMapper.insertSelective(rangeDetail);
                }
            }
        }
        //业务值域
        if (MapUtils.isNotEmpty(ywzy)) {
            Set<String> sheetNameSet = ywzy.keySet();
            for (String sheetName : sheetNameSet) {
                String[] names = StringUtils.split(sheetName, "-", 2);
                List<Company> companyList = companyMapper.selectByName(names[0]);
                if (CollectionUtils.isEmpty(companyList)) {
                    log.error("{}不存在,请检查系统中业务厂商名与导入文件是否匹配", names[0]);
                    SystemError error = SystemError.EXCEL_ERROR;
                    error.setText(names[0] + "不存在,请检查系统中业务厂商名与导入文件是否匹配");
                    throw new ApplicationException(error);
                }
                if (companyList.size() > 1) {
                    log.error("{}业务厂商名不能重复,无法确定导入唯一性,请在系统中进行区分", names[0]);
                    SystemError error = SystemError.EXCEL_ERROR;
                    error.setText(names[0] + "业务厂商名不能重复,无法确定导入唯一性,请在系统中进行区分");
                    throw new ApplicationException(error);
                }
                Company company = companyList.get(0);
                Map<String, String> map1 = new HashMap<>();
                map1.put("companyCode", company.getCompanyCode() + "");
                map1.put("platformRangeCode", names[1]);
                ///先删除对照表
                rangeContrastMapper.deleteByCompanyCodeAndRangeCode(map1);
                Map<String, String> detailMap = ywzy.get(sheetName);
                Set<String> detailKey = detailMap.keySet();
                for (String detailCode : detailKey) {
                    if (StringUtils.isEmpty(detailCode.trim())) continue;
                    RangeContrast rangeContrast = new RangeContrast();
                    rangeContrast.setId(idWorker.getId());
                    rangeContrast.setCompanyCode(company.getCompanyCode() + "");
                    rangeContrast.setCompanyName(company.getCompanyName());
                    rangeContrast.setPlatformRangeCode(names[1]);
                    RangeClass rangeClass = rangeClassMapper.selectByCode(names[1]);
                    rangeContrast.setPlatformRangeName(rangeClass.getPlatformName());
                    rangeContrast.setCompanyRangeCode(detailCode);
                    rangeContrast.setCompanyRangeName(detailMap.get(detailCode));
                    rangeContrastMapper.insertSelective(rangeContrast);
                }
            }
        }
        return "{\"success\" :\"1\"}";
    }


    public ByteArrayOutputStream contrastDownload(String platformRangeCode, String companyCode) {
        Map<String, String> map = new HashMap<>();
        map.put("companyCode", companyCode);
        map.put("platformRangeCode", platformRangeCode);
        List<RangeContrast> allRC = rangeContrastMapper.findAllRangeContrast(map);
        try {
            Workbook workbook = new HSSFWorkbook();
            Sheet sheet = workbook.createSheet(platformRangeCode);
            //创建行
            Row row = sheet.createRow(0);
            //创建单元格
            row.createCell(0).setCellValue("厂商值编码");
            row.createCell(1).setCellValue("厂商值名称");
            row.createCell(2).setCellValue("平台值编码");
            row.createCell(3).setCellValue("平台值名称");
            row.createCell(4).setCellValue("相等");
            row.createCell(5).setCellValue("手工对照标志,1手工");
            for (int i = 0; i < allRC.size(); i++) {
                row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(allRC.get(i).getCompanyRangeCode());
                row.createCell(1).setCellValue(allRC.get(i).getCompanyRangeName());
                row.createCell(2).setCellValue(allRC.get(i).getPlatformDetailCode());
                row.createCell(3).setCellValue(allRC.get(i).getPlatformDetailName());
                row.createCell(4).setCellValue(StringUtils.equals(allRC.get(i).getPlatformDetailName(), allRC.get(i).getCompanyRangeName()) ? "1" : "0");
                row.createCell(5).setCellValue(allRC.get(i).getSdStatus());
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return outputStream;
        } catch (Exception e) {
            e.printStackTrace();
            SystemError error = SystemError.SYSTEM_ERROR;
            error.setText("excel创建模块出错");
            throw new ApplicationException(error);
        }
    }
}
