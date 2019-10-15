package com.adtech.cn.service;

import org.apache.commons.lang3.StringUtils;

public class test {

    public static void main(String[] args) {
        String[] names = new String[2];
        String sheetName = "平台分类编码-GB-01 CXBLXZ-1982?-";
        sheetName = "平台分类编码-GB-01 CXBLXZ-1982?-";
        names = StringUtils.split(sheetName, "-", 2);
        System.out.println(names[0]);
        System.out.println(names[1]);
        System.out.println(sheetName.indexOf("-"));
        names[0] = StringUtils.substring(sheetName, 0, sheetName.indexOf("-"));
        names[1] = StringUtils.substring(sheetName, names[0].length() + 1);
        System.out.println(names[0]);
        System.out.println(names[1]);
    }
}
