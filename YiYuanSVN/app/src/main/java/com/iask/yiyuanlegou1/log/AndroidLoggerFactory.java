
package com.iask.yiyuanlegou1.log;

public class AndroidLoggerFactory extends LoggerFactory {

	@Override
	public Logger getLogger() {
		return new AndroidLogger();
	}

	@Override
	public Logger getLogger(String tag) {
		return new AndroidLogger(tag);
	}

	@Override
	public Logger getLogger(Class<?> clz) {
		return new AndroidLogger(clz.getSimpleName());
	}
	
}
