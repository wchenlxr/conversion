package com.adtech.cn.domain;

public class RangeDetail {
    private Long id;

    private String platformCode;
    
	private String platformName;

	private String detailCode;

    private String detailName;
    
    private Integer indexStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getDetailCode() {
        return detailCode;
    }

    public void setDetailCode(String detailCode) {
        this.detailCode = detailCode == null ? null : detailCode.trim();
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName == null ? null : detailName.trim();
    }

	public Integer getIndexStatus() {
		return indexStatus;
	}

	public void setIndexStatus(Integer indexStatus) {
		this.indexStatus = indexStatus;
	}
    
}