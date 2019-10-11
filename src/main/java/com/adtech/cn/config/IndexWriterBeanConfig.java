package com.adtech.cn.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2018/5/2.
 */
@Configuration
public class IndexWriterBeanConfig {

    @Bean(name = "indexWriter")
    @ConditionalOnMissingBean(name="indexWriter")
    public IndexWriter createIndexWriter(@Qualifier("indexConfig") IndexConfig indexConfig) throws IOException {
        Directory directory = FSDirectory.open(new File(indexConfig.getLocation()));
        Analyzer analyzer = new CJKAnalyzer(Version.LATEST);
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        return indexWriter;
    }
}
