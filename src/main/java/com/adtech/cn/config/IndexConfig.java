package com.adtech.cn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2018/4/2.
 */
@Component
@ConfigurationProperties(prefix = "lucene_index")
public class IndexConfig {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    private String location;


    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }
}
