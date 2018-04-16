package com.adtech.cn.service;

import com.adtech.cn.service.support.NullStringToEmptyAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wchen on 2018/3/16.
 */
public interface IBaseService {
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory()).create();
    Logger log = LoggerFactory.getLogger(IBaseService.class);
}
