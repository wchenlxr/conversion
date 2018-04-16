package com.adtech.cn.dto;

import java.util.List;

import com.adtech.cn.domain.RangeClass;

public class UpdateRangeClassDTO {
	private List<RangeClass> inserted;
    private List<RangeClass> updated;
    private List<RangeClass> deleted;

    public List<RangeClass> getInserted() {
        return inserted;
    }

    public void setInserted(List<RangeClass> inserted) {
        this.inserted = inserted;
    }

    public List<RangeClass> getUpdated() {
        return updated;
    }

    public void setUpdated(List<RangeClass> updated) {
        this.updated = updated;
    }

    public List<RangeClass> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<RangeClass> deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "UpdateRangeClassDTO{" +
                "inserted=" + inserted +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
