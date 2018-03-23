package com.iask.yiyuanlegou1.network.respose.account;

public class Startup1ReturnInfo {
	
	public StartupReturnOssConfig ossConfig;
	private String knowledgeUrl;

	public String getKnowledgeUrl() {
		return knowledgeUrl;
	}

	public void setKnowledgeUrl(String knowledgeUrl) {
		this.knowledgeUrl = knowledgeUrl;
	}

	public Startup1ReturnInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public StartupReturnOssConfig getOSSConfig(){
		return ossConfig;
	}
	
	public void setOSSConfig(StartupReturnOssConfig ossConfig){
		this.ossConfig = ossConfig;
	}

}
