
package com.iask.yiyuanlegou1.log;

public abstract class LoggerFactory {

	/**
	 * 获取Logger
	 * 
	 * @return
	 * @return Logger
	 */
	public abstract Logger getLogger();
	
	public abstract Logger getLogger(String tag);

	public abstract Logger getLogger(final Class<?> clz);
}
