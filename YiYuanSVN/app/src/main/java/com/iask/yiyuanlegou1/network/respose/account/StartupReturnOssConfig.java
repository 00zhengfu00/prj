package com.iask.yiyuanlegou1.network.respose.account;

public class StartupReturnOssConfig {
	
	private String bucketName;
	private String cdnName;
	private String hostId;
	private String imgPath;
	private String videoPath;

	public StartupReturnOssConfig() {
		// TODO Auto-generated constructor stub
	}
	
	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getCdnName() {
		return cdnName;
	}
	public void setCdnName(String cdnName) {
		this.cdnName = cdnName;
	}
	
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
	public String getVideoPath() {
		return videoPath;
	}
	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}
	
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
}
