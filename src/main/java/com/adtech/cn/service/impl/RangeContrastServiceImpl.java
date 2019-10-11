package com.adtech.cn.service.impl;

import com.adtech.cn.config.IndexConfig;
import com.adtech.cn.domain.Company;
import com.adtech.cn.domain.Contrast;
import com.adtech.cn.domain.RangeContrast;
import com.adtech.cn.dto.UpdateContrastDTO;
import com.adtech.cn.dto.UpdateRangeContrastDTO;
import com.adtech.cn.mapper.CompanyMapper;
import com.adtech.cn.mapper.ContrastMapper;
import com.adtech.cn.mapper.RangeContrastMapper;
import com.adtech.cn.service.IBaseService;
import com.adtech.cn.utils.IdWorker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

/**
 * 业务厂商值域对照 Created by Administrator on 2018/3/23.
 */
@Service
@Transactional
public class RangeContrastServiceImpl implements IBaseService {

    @Autowired
    private RangeContrastMapper rangeContrastMapper;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private ContrastMapper contrastMapper;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private IndexConfig indexConfig;


    /**
     * 搜索分类分页数据
     *
     * @param page
     * @param rows
     * @param companyCode
     * @param platformRangeCode
     * @param type
     * @param value
     * @param ppFlag
     * @return
     */
    public String searchRangeContrast(int page, int rows, String companyCode, String platformRangeCode, Integer type,
                                      String value, String ppFlag) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> re = new HashMap<>();
        map.put("companyCode", companyCode);
        map.put("platformRangeCode", platformRangeCode);
        map.put("ppFlag", ppFlag);
        map.put("page", page);
        map.put("rows", rows);
        map.put("type", type);
        map.put("value", value);
        List<RangeContrast> rangeContrastList = rangeContrastMapper.searchRangeContrast(map);
        int total = rangeContrastMapper.searchCountNum(map);
        re.put("total", total);
        re.put("rows", rangeContrastList);
        return gson.toJson(re);
    }

    /**
     * 厂商值域对照操作
     *
     * @param updateRangeContrastDTO
     * @return
     */
    public String updateRangeContrast(UpdateRangeContrastDTO updateRangeContrastDTO) {
        String result = "ok";
        List<RangeContrast> insertRangeContrast = updateRangeContrastDTO.getInserted();
        List<RangeContrast> deleteRangeContrast = updateRangeContrastDTO.getDeleted();
        List<RangeContrast> updateRangeContrast = updateRangeContrastDTO.getUpdated();
        if (CollectionUtils.isNotEmpty(insertRangeContrast)) {
            List<RangeContrast> rcList = new ArrayList<>();
            for (RangeContrast rangeContrast : insertRangeContrast) {
                Map<String, String> map = new HashMap<>();
                map.put("companyCode", rangeContrast.getCompanyCode());
                map.put("companyRangeCode", rangeContrast.getCompanyRangeCode());
                map.put("platformRangeCode", rangeContrast.getPlatformRangeCode());
                // 验证厂商值域编码的唯一性
                RangeContrast rc = rangeContrastMapper.selectByCode(map);
                if (rc == null) {
                    // 根据厂商编码查询厂商信息
                    Company company = companyMapper.findByCompanyCode(Long.parseLong(rangeContrast.getCompanyCode()));
                    if (company != null) {
                        rangeContrast.setId(idWorker.getId());
                        rangeContrast.setCompanyName(company.getCompanyName());
                        rangeContrastMapper.insertSelective(rangeContrast);
                        rcList.add(rangeContrast);
                    } else {
                        result = "厂商信息不存在，请刷新页面！";
                    }
                } else {
                    result = "厂商值域编码重复，请重新输入！";
                }
            }
            seachIndex(rcList);
        }
        if (CollectionUtils.isNotEmpty(deleteRangeContrast)) {
            for (RangeContrast rangeContrast : deleteRangeContrast) {
                // 删除厂商值域对照数据
                rangeContrastMapper.deleteByPrimaryKey(rangeContrast.getId());
                // 删除转码匹配数据
                Map<String, String> map = new HashMap<>();
                map.put("companyCode", rangeContrast.getCompanyCode());
                map.put("companyRangeCode", rangeContrast.getCompanyRangeCode());
                map.put("platformRangeCode", rangeContrast.getPlatformRangeCode());
                contrastMapper.deleteByCode(map);
            }
        }
        if (CollectionUtils.isNotEmpty(updateRangeContrast)) {
            List<RangeContrast> rcList = new ArrayList<>();
            for (RangeContrast rangeContrast : updateRangeContrast) {
                rangeContrastMapper.updateByPrimaryKeySelective(rangeContrast);
                // 删除转码匹配数据
                Map<String, String> map = new HashMap<>();
                map.put("companyCode", rangeContrast.getCompanyCode());
                map.put("companyRangeCode", rangeContrast.getCompanyRangeCode());
                map.put("platformRangeCode", rangeContrast.getPlatformRangeCode());
                contrastMapper.deleteByCode(map);
                rcList.add(rangeContrast);
            }
            seachIndex(rcList);
        }
        return result;
    }

    /**
     * 查询索引
     *
     * @param rcList
     * @return
     */
    public String seachIndex(List<RangeContrast> rcList) {
        Directory directory = null;
        IndexReader indexReader = null;
        try {
            directory = FSDirectory.open(new File(indexConfig.getLocation()));
            indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new CJKAnalyzer(Version.LATEST);
            // 设置查询域
            BooleanQuery builder;
            QueryParser parser1 = new QueryParser("detailName", analyzer);
            QueryParser parser2 = new QueryParser("platformCode", new KeywordAnalyzer());
            for (RangeContrast rangeContrast : rcList) {
                try {
                    // 查询字符串
                    builder = new BooleanQuery();
                    Query query1 = parser1.parse(escapeQueryChars(rangeContrast.getCompanyRangeName()));
                    builder.add(query1, BooleanClause.Occur.SHOULD);
                    Query query2 = parser2.parse("\"" + rangeContrast.getPlatformRangeCode() + "\"");
                    builder.add(query2, BooleanClause.Occur.MUST);
                    // 获取查询结果
                    TopDocs topDocs = indexSearcher.search(builder, 50);
                    System.out.println("查询的总记录数为： " + topDocs.totalHits);
                    int count = 0;
                    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                        int docID = scoreDoc.doc;
                        // 根据id查询document对象
                        Document document = indexSearcher.doc(docID);
                        // 根据document对象获取相应的信息
                        System.out.println("用户名: " + document.get("detailName"));
                        System.out.println("匹配得分: " + scoreDoc.score);
                        Contrast contrast = new Contrast();
                        contrast.setId(idWorker.getId());
                        contrast.setCompanyCode(rangeContrast.getCompanyCode());
                        contrast.setPlatformRangeCode(rangeContrast.getPlatformRangeCode());
                        contrast.setCompanyRangeCode(rangeContrast.getCompanyRangeCode());
                        contrast.setPlatformDetailCode(document.get("detailCode"));
                        contrast.setPlatformDetailName(document.get("detailName"));
                        contrast.setScore(scoreDoc.score);
                        // 将匹配度最高的数据更新到厂商值域对照表
                        if (count == 0) {
                            contrast.setSelected(1);
                            RangeContrast rc = new RangeContrast();
                            rc.setCompanyCode(contrast.getCompanyCode());
                            rc.setPlatformRangeCode(contrast.getPlatformRangeCode());
                            rc.setCompanyRangeCode(contrast.getCompanyRangeCode());
                            rc.setPlatformDetailCode(contrast.getPlatformDetailCode());
                            rc.setPlatformDetailName(document.get("detailName"));
                            rc.setContrastTime(new Date());
                            rc.setSdStatus("0");
                            rangeContrastMapper.updatePlatformDetail(rc);
                        } else {
                            contrast.setSelected(0);
                        }
                        contrastMapper.insertSelective(contrast);
                        count++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            directory.close();
            indexReader.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "ok";

    }

    /**
     * 修改转码匹配选中状态
     *
     * @param updateContrastDTO
     * @return
     */
    public String updateContrast(UpdateContrastDTO updateContrastDTO) {
        String result = "ok";
        List<Contrast> updateContrast = updateContrastDTO.getUpdated();
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(updateContrast)) {
            for (Contrast contrast : updateContrast) {
                map.put("companyCode", contrast.getCompanyCode());
                map.put("platformRangeCode", contrast.getPlatformRangeCode());
                map.put("companyRangeCode", contrast.getCompanyRangeCode());
                contrastMapper.updateOthers(map);
                contrastMapper.updateSelected(contrast);
                RangeContrast rangeContrast = new RangeContrast();
                rangeContrast.setCompanyCode(contrast.getCompanyCode());
                rangeContrast.setPlatformRangeCode(contrast.getPlatformRangeCode());
                rangeContrast.setCompanyRangeCode(contrast.getCompanyRangeCode());
                // 选中
                if (contrast.getSelected() == 1) {
                    rangeContrast.setPlatformDetailCode(contrast.getPlatformDetailCode());
                    rangeContrast.setPlatformDetailName(contrast.getPlatformDetailName());
                    rangeContrast.setContrastTime(new Date());
                    rangeContrast.setSdStatus("1");
                    rangeContrastMapper.updatePlatformDetail(rangeContrast);
                } else {
                    rangeContrast.setPlatformDetailCode("");
                    rangeContrast.setPlatformDetailName("");
                    rangeContrast.setContrastTime(null);
                    rangeContrast.setSdStatus("0");
                    rangeContrastMapper.updatePlatformDetail(rangeContrast);
                }
            }
        }
        return result;
    }

    /**
     * 根据厂商编码和平台分类编码转码
     *
     * @param companyCode
     * @param platformRangeCode
     * @return
     */
    public String transcode(String companyCode, String platformRangeCode) {
        Map<String, String> map = new HashMap<>();
        map.put("companyCode", companyCode);
        map.put("platformRangeCode", platformRangeCode);
        RangeContrast rc = new RangeContrast();
        rc.setCompanyCode(companyCode);
        rc.setPlatformRangeCode(platformRangeCode);
        rc.setCompanyRangeCode(null);
        rc.setPlatformDetailCode("");
        rc.setPlatformDetailName("");
        rc.setContrastTime(null);
        rc.setSdStatus("0");
        // 按业务厂商编码以及平台分类编码删除匹配临时表数据
        contrastMapper.deleteByCode(map);
        rangeContrastMapper.updatePlatformDetail(rc);
        // 根据厂商编码和平台分类编码查询所有满足条件的厂商值域对照数据
        List<RangeContrast> allRC = rangeContrastMapper.findAllRangeContrast(map);
        seachIndex(allRC);
        return "ok";
    }

    /**
     * 清空选中对照
     *
     * @param id
     * @return
     */
    public String clearOne(Long id) {
        int i = rangeContrastMapper.clearOne(id);
        if (i == 1) {
            return "ok";
        } else {
            return "faile";
        }
    }

    /**
     * 转码匹配分页数据
     *
     * @param page
     * @param rows
     * @param companyCode
     * @param platformRangeCode
     * @param companyRangeCode
     * @return
     */
    public String getContrast(int page, int rows, String companyCode, String platformRangeCode,
                              String companyRangeCode) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> re = new HashMap<>();
        map.put("companyCode", companyCode);
        map.put("platformRangeCode", platformRangeCode);
        map.put("companyRangeCode", companyRangeCode);
        map.put("page", page);
        map.put("rows", rows);
        List<Contrast> contrastList = contrastMapper.findContrast(map);
        int total = contrastMapper.countNum(map);
        re.put("total", total);
        re.put("rows", contrastList);
        return gson.toJson(re);
    }

    public static String escapeQueryChars(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            // These characters are part of the query syntax and must be escaped
            if (c == '\\' || c == '+' || c == '-' || c == '!' || c == '(' || c == ')' || c == ':'
                    || c == '^' || c == '[' || c == ']' || c == '\"' || c == '{' || c == '}' || c == '~'
                    || c == '*' || c == '?' || c == '|' || c == '&' || c == ';' || c == '/'
                    || Character.isWhitespace(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
