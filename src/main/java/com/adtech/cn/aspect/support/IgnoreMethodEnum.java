package com.adtech.cn.aspect.support;

/**
 * 忽略掉的异常
 * Created by Administrator on 2018/3/21.
 */

public enum  IgnoreMethodEnum {

    DOWNLOADAREAFILE("downloadAreaFile"),
    DOWNLOADFILE("downloadFile"),
    ;

    private String name;

    IgnoreMethodEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static boolean exist(String name){
        boolean flag = false;
        for (IgnoreMethodEnum item: IgnoreMethodEnum.values()) {
            if (item.getName().equals(name)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
