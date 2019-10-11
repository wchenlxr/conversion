//package com.adtech.cn.config;
//
//import org.springframework.boot.web.servlet.MultipartConfigFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.MultipartConfigElement;
//
//@Configuration
//public class MultipartConfig {
//    /**
//     * 文件上传临时路径
//     */
//    @Bean
//    MultipartConfigElement multipartConfigElement() {
//        MultipartConfigFactory factory = new MultipartConfigFactory();
//        //这里始终会在linux /tmp/work/tomcat 后面拼接 ./tmp 因为linux 的临时文件夹超过10天未使用就会清理掉，所以不使用这咱方式，改为server.tomcat.basedir=./tmp
//        //会直接在当前目录下建tmp文件夹来保存临时文件
//        factory.setLocation("./tmp");
//        return factory.createMultipartConfig();
//    }
//
//}