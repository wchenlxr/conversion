package com.adtech.cn.config;

import com.adtech.cn.domain.RangeDetail;
import com.adtech.cn.mapper.RangeDetailMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化加载方法
 *
 * @author PYH
 */
@Component
public class InitServlet {
    @Autowired
    private RangeDetailMapper detailMapper;

//    @Autowired
//    private IndexConfig indexConfig;

    @Autowired
    private IndexWriter indexWriter;

    /**
     * 创建索引
     *
     * @param
     */
    @Scheduled(fixedDelay = 60 * 1000)
    public void createIndex() {
        Map<String, Object> map = new HashMap<>();
        map.put("indexStatus", 0);
        // 查询所有的明细数据
        List<RangeDetail> rdList = detailMapper.findAllDetail(map);
//        Directory directory = null;
//        IndexWriter indexWriter = null;
        try {
//            directory = FSDirectory.open(new File(indexConfig.getLocation()));
//            Analyzer analyzer = new CJKAnalyzer(Version.LATEST);
            // 索引配置
//            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
//            // 设置打开索引模式为创建或追加
//            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
//            indexWriter = new IndexWriter(directory, indexWriterConfig);
            for (RangeDetail rd : rdList) {
                if (StringUtils.isEmpty(rd.getDetailName()) || StringUtils.isEmpty(rd.getPlatformCode()) || StringUtils.isEmpty(rd.getDetailCode()))
                    continue;
                // 创建文档对象
                Document document = new Document();
                StringField idField = new StringField("id", rd.getId().toString(), Store.YES);
                StringField platformCodeField = new StringField("platformCode",rd.getPlatformCode(), Store.YES);
                StringField detailCodeField = new StringField("detailCode", rd.getDetailCode(), Store.YES);
                TextField detailNameField = new TextField("detailName", rd.getDetailName(), Store.YES);
                document.add(idField);
                document.add(platformCodeField);
                document.add(detailCodeField);
                document.add(detailNameField);
                indexWriter.addDocument(document);
                RangeDetail nRd = new RangeDetail();
                // 修改索引状态：1已索引
                nRd.setId(rd.getId());
                nRd.setIndexStatus(1);
                detailMapper.updateByPrimaryKeySelective(nRd);
            }
            indexWriter.commit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
/*            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/
/*            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/

        }

    }

}
