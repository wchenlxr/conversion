package com.adtech.cn.dto;

import java.util.List;

import com.adtech.cn.domain.RangeContrast;

public class UpdateRangeContrastDTO {
	private List<RangeContrast> inserted;
    private List<RangeContrast> updated;
    private List<RangeContrast> deleted;

    public List<RangeContrast> getInserted() {
        return inserted;
    }

    public void setInserted(List<RangeContrast> inserted) {
        this.inserted = inserted;
    }

    public List<RangeContrast> getUpdated() {
        return updated;
    }

    public void setUpdated(List<RangeContrast> updated) {
        this.updated = updated;
    }

    public List<RangeContrast> getDeleted() {
        return deleted;
    }

    public void setDeleted(List<RangeContrast> deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "UpdateRangeContrastDTO{" +
                "inserted=" + inserted +
                ", updated=" + updated +
                ", deleted=" + deleted +
                '}';
    }
}
