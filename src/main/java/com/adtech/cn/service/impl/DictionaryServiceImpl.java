package com.adtech.cn.service.impl;

import com.adtech.cn.config.IndexConfig;
import com.adtech.cn.domain.RangeClass;
import com.adtech.cn.domain.RangeDetail;
import com.adtech.cn.dto.UpdateRangeClassDTO;
import com.adtech.cn.dto.UpdateRangeDetailDTO;
import com.adtech.cn.mapper.RangeClassMapper;
import com.adtech.cn.mapper.RangeDetailMapper;
import com.adtech.cn.service.IBaseService;
import com.adtech.cn.utils.IdWorker;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 平台字典
 *
 * @author PYH
 */
@Service
@Transactional
public class DictionaryServiceImpl implements IBaseService {
    @Autowired
    private RangeClassMapper classMapper;
    @Autowired
    private RangeDetailMapper detailMapper;
    @Autowired
    private IdWorker idWorker;
    //	private PropertiesUtil propertiesUtil = new PropertiesUtil();
    @Autowired
    private IndexConfig indexConfig;

    @Autowired
    private IndexWriter indexWriter;

    /**
     * 平台值域分类搜索
     *
     * @param page
     * @param rows
     * @return
     */
    public String searchRangeClass(int page, int rows, Integer type, String value, Integer status) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> re = new HashMap<>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("type", type);
        map.put("value", value);
        map.put("status", status);
        int total = classMapper.searchCountNum(map);
        List<RangeClass> companyList = classMapper.searchClass(map);
        re.put("total", total);
        re.put("rows", companyList);
        return gson.toJson(re);
    }

    /**
     * 平台值域分类明细查询
     *
     * @param page
     * @param rows
     * @return
     */
    public String findRangeDetail(int page, int rows, String platformCode) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> re = new HashMap<>();
        map.put("page", page);
        map.put("rows", rows);
        map.put("platformCode", platformCode);
        int total = detailMapper.countNum(platformCode);
        List<RangeDetail> companyList = detailMapper.findDetail(map);
        re.put("total", total);
        re.put("rows", companyList);
        return gson.toJson(re);
    }

    /**
     * 平台值域分类操作
     *
     * @param updateRangeClassDTO
     * @return
     */
    public String updateRangeClass(UpdateRangeClassDTO updateRangeClassDTO) {
        String result = "ok";
        List<RangeClass> insertRangeClass = updateRangeClassDTO.getInserted();
        List<RangeClass> deleteRangeClass = updateRangeClassDTO.getDeleted();
        List<RangeClass> updateRangeClass = updateRangeClassDTO.getUpdated();
        if (CollectionUtils.isNotEmpty(insertRangeClass)) {
            for (RangeClass rangeClass : insertRangeClass) {
                // 验证分类编码的唯一性
                RangeClass rc = classMapper.selectByCode(rangeClass.getPlatformCode());
                if (rc == null) {
                    rangeClass.setId(idWorker.getId());
                    rangeClass.setCreateTime(new Date());
                    classMapper.insertSelective(rangeClass);
                } else {
                    result = "分类编码重复，请重新输入！";
                }
            }
        }
        if (CollectionUtils.isNotEmpty(deleteRangeClass)) {
            for (RangeClass rangeClass : deleteRangeClass) {
                Map<String, Object> map = new HashMap<>();
                map.put("platformCode", rangeClass.getPlatformCode());
                // 验证该分类下是否有关联数据
                List<RangeDetail> rdList = detailMapper.findAllDetail(map);
                if (rdList.size() > 0) {
                    result = "该分类下存在子类，不允许删除！";
                } else {
                    classMapper.deleteByPrimaryKey(rangeClass.getId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(updateRangeClass)) {
            for (RangeClass rangeClass : updateRangeClass) {
                classMapper.updateByPrimaryKeySelective(rangeClass);
            }
        }
        return result;
    }

    /**
     * 平台值域分类明细操作
     *
     * @param updateRangeDetailDTO
     * @return
     */
    public String updateRangeDetail(UpdateRangeDetailDTO updateRangeDetailDTO) {
        String result = "ok";
/*        Directory directory = null;
        IndexWriter indexWriter = null;*/
        List<RangeDetail> insertRangeDetail = updateRangeDetailDTO.getInserted();
        List<RangeDetail> deleteRangeDetail = updateRangeDetailDTO.getDeleted();
        List<RangeDetail> updateRangeDetail = updateRangeDetailDTO.getUpdated();
        try {
            if (CollectionUtils.isNotEmpty(insertRangeDetail)) {
                for (RangeDetail rangeDetail : insertRangeDetail) {
                    Map<String, String> map = new HashMap<>();
                    map.put("platformCode", rangeDetail.getPlatformCode());
                    map.put("detailCode", rangeDetail.getDetailCode());
                    // 验证分类值域编码的唯一性
                    RangeDetail rc = detailMapper.selectByCode(map);
                    if (rc == null) {
                        rangeDetail.setId(idWorker.getId());
                        detailMapper.insertSelective(rangeDetail);
/*                        directory = FSDirectory.open(new File(indexConfig.getLocation()));
                        Analyzer analyzer = new CJKAnalyzer(Version.LATEST);
                        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
                        indexWriter = new IndexWriter(directory, indexWriterConfig);*/
                        Document newDoc = new Document();
                        newDoc.add(new StringField("platformCode", rangeDetail.getPlatformCode(), Store.YES));
                        newDoc.add(new StringField("id", rangeDetail.getId().toString(), Store.YES));
                        newDoc.add(new StringField("detailCode", rangeDetail.getDetailCode(), Store.YES));
                        newDoc.add(new TextField("detailName", rangeDetail.getDetailName(), Store.YES));
                        indexWriter.addDocument(newDoc);
                        // 修改索引状态：1已索引
                        RangeDetail nRd = new RangeDetail();
                        nRd.setId(rangeDetail.getId());
                        nRd.setIndexStatus(1);
                        detailMapper.updateByPrimaryKeySelective(nRd);
                        indexWriter.commit();
                    } else {
                        result = "分类值域编码重复，请重新输入！";
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(deleteRangeDetail)) {
                for (RangeDetail rangeDetail : deleteRangeDetail) {
                    detailMapper.deleteByPrimaryKey(rangeDetail.getId());
/*                    directory = FSDirectory.open(new File(indexConfig.getLocation()));
                    Analyzer analyzer = new CJKAnalyzer(Version.LATEST);
                    IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
                    indexWriter = new IndexWriter(directory, indexWriterConfig);*/
                    System.out.println("索引中的文档数量：" + indexWriter.numDocs());
                    indexWriter.deleteDocuments(new Term("id", rangeDetail.getId().toString()));
                    indexWriter.commit();
                    System.out.println("删除文档后的数量" + indexWriter.numDocs());
                }
            }
            if (CollectionUtils.isNotEmpty(updateRangeDetail)) {
                for (RangeDetail rangeDetail : updateRangeDetail) {
                    detailMapper.updateByPrimaryKeySelective(rangeDetail);
                        /*directory = FSDirectory.open(new File(indexConfig.getLocation()));
                        Analyzer analyzer = new CJKAnalyzer(Version.LATEST);
                        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
                        indexWriter = new IndexWriter(directory, indexWriterConfig);
                        Document newDoc = new Document();
                        newDoc.add(new StringField("platformCode", rangeDetail.getPlatformCode(), Store.YES));
                        newDoc.add(new StringField("id", rangeDetail.getId().toString(), Store.YES));
                        newDoc.add(new StringField("detailCode", rangeDetail.getDetailCode(), Store.YES));
                        newDoc.add(new TextField("detailName", rangeDetail.getDetailName(), Store.YES));
                        indexWriter.updateDocument(new Term("id", rangeDetail.getId().toString()), newDoc);*/
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
/*            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*/

        }

        return result;
    }

    /**
     * 平台值域分类下拉列数据查询
     *
     * @return
     */
    public String findClassList() {
        List<Map<String, String>> classList = classMapper.findClassList();
        return gson.toJson(classList);
    }

}
