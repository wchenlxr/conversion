package com.adtech.cn.domain;

public class Contrast {
    private Long id;

    private String companyCode;

    private String platformRangeCode;

    private String companyRangeCode;

    private String platformDetailCode;

    private Float score;

    private Integer selected;
    
    private String platformDetailName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getPlatformRangeCode() {
        return platformRangeCode;
    }

    public void setPlatformRangeCode(String platformRangeCode) {
        this.platformRangeCode = platformRangeCode == null ? null : platformRangeCode.trim();
    }

    public String getCompanyRangeCode() {
        return companyRangeCode;
    }

    public void setCompanyRangeCode(String companyRangeCode) {
        this.companyRangeCode = companyRangeCode == null ? null : companyRangeCode.trim();
    }

    public String getPlatformDetailName() {
        return platformDetailName;
    }

    public void setPlatformDetailName(String platformDetailName) {
        this.platformDetailName = platformDetailName == null ? null : platformDetailName.trim();
    }

    public String getPlatformDetailCode() {
        return platformDetailCode;
    }

    public void setPlatformDetailCode(String platformDetailCode) {
        this.platformDetailCode = platformDetailCode == null ? null : platformDetailCode.trim();
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }
}