package com.adtech.cn.service.support;

import com.adtech.cn.service.IBaseService;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 删除索引任务
 * Created by Administrator on 2018/5/2.
 */
public class DeleteIndexTask extends TimerTask implements IBaseService{
    private IndexWriter indexWriter;

    private String[] ids;

    public DeleteIndexTask(IndexWriter iw, String... id) {
        this.indexWriter = iw;
        this.ids = id;
        Timer timer = new Timer();
        timer.schedule(this, 1000);
    }

    @Override
    public void run() {
        try {
            log.info("提交前索引数目,{}",indexWriter.numDocs());
            for (String id : ids) {
                log.info("删除索引,id={}",id);
                indexWriter.deleteDocuments(new Term("id", id));
                indexWriter.commit();
            }
            log.info("提交后索引数目,{}",indexWriter.numDocs());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
