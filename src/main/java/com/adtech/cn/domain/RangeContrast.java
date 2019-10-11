package com.adtech.cn.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class RangeContrast {
    private Long id;

    private String companyCode;

    private String companyName;

    private String platformRangeCode;

    private String platformRangeName;

    private String companyRangeCode;

    private String companyRangeName;

    private String platformDetailCode;

    private String platformDetailName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date contrastTime;

    private String sdStatus;

    public String getSdStatus() {
        return sdStatus;
    }

    public void setSdStatus(String sdStatus) {
        this.sdStatus = sdStatus;
    }

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getPlatformRangeCode() {
        return platformRangeCode;
    }

    public void setPlatformRangeCode(String platformRangeCode) {
        this.platformRangeCode = platformRangeCode == null ? null : platformRangeCode.trim();
    }

    public String getPlatformRangeName() {
        return platformRangeName;
    }

    public void setPlatformRangeName(String platformRangeName) {
        this.platformRangeName = platformRangeName == null ? null : platformRangeName.trim();
    }

    public String getCompanyRangeCode() {
        return companyRangeCode;
    }

    public void setCompanyRangeCode(String companyRangeCode) {
        this.companyRangeCode = companyRangeCode == null ? null : companyRangeCode.trim();
    }

    public String getCompanyRangeName() {
        return companyRangeName;
    }

    public void setCompanyRangeName(String companyRangeName) {
        this.companyRangeName = companyRangeName == null ? null : companyRangeName.trim();
    }

    public String getPlatformDetailCode() {
        return platformDetailCode;
    }

    public void setPlatformDetailCode(String platformDetailCode) {
        this.platformDetailCode = platformDetailCode == null ? null : platformDetailCode.trim();
    }

    public String getPlatformDetailName() {
        return platformDetailName;
    }

    public void setPlatformDetailName(String platformDetailName) {
        this.platformDetailName = platformDetailName == null ? null : platformDetailName.trim();
    }

    public Date getContrastTime() {
        return contrastTime;
    }

    public void setContrastTime(Date contrastTime) {
        this.contrastTime = contrastTime;
    }
}