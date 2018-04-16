package com.adtech.cn.dto;

import com.adtech.cn.domain.Company;

import java.util.List;

/**
 * 对厂商进行增,删,改dto
 * Created by Administrator on 2018/3/21.
 */
public class UpdateCompanyDTO {
    private List<Company> inserted;
    private List<Company> updated;
    private List<Company> deleted;

    public List<Company> getInserted() {
        return inserted;
    }

    public void setInserted(List<Company> inserted) {
        this.inserted = inserted;
    }

    public List<Company> getUpdated() {
        return updated;
    }

    public void setUpdated(List<Company> updated) {
        this.updated = updated;
    }

    public List<Company> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<Company> deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "UpdateCompanyDTO{" +
                "inserted=" + inserted +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
