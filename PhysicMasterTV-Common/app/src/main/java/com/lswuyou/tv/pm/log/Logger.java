
package com.lswuyou.tv.pm.log;


public abstract class Logger {

	/**
	 * 打印DEBUG级别日志
	 * 
	 * @param message
	 * @return void
	 */
	public abstract void debug(String message);

	/**
	 * 打印DEBUG级别日志
	 * 
	 * @param message
	 * @param tr
	 * @return void
	 */
	public abstract void debug(String message, Throwable tr);

	/**
	 * 打印INFO级别日志
	 * 
	 * @param message
	 * @return void
	 */
	public abstract void info(String message);

	/**
	 * 打印INFO级别日志
	 * 
	 * @param message
	 * @param tr
	 * @return void
	 */
	public abstract void info(String message, Throwable tr);

	/**
	 * 打印ERROR级别日志
	 * 
	 * @param message
	 * @return void
	 */
	public abstract void error(String message);

	/**
	 * 打印ERROR级别日志
	 * 
	 * @param message
	 * @param tr
	 * @return void
	 */
	public abstract void error(String message, Throwable tr);

	/**
	 * 打印WARN级别日志
	 * 
	 * @param message
	 * @return void
	 */
	public abstract void warn(String message);

	/**
	 * 打印WARN级别日志
	 * 
	 * @param message
	 * @param tr
	 * @return void
	 */
	public abstract void warn(String message, Throwable tr);

	/**
	 * DEBUG级别是否有效
	 * 
	 * @return
	 * @return boolean
	 */
	public abstract boolean isDebugEnabled();

	/**
	 * ERROR级别是否有效
	 * 
	 * @return
	 * @return boolean
	 */
	public abstract boolean isErrorEnabled();

	/**
	 * INFO级别是否有效
	 * 
	 * @return
	 * @return boolean
	 */
	public abstract boolean isInfoEnabled();

	/**
	 * WARN级别是否有效
	 * 
	 * @return
	 * @return boolean
	 */
	public abstract boolean isWarnEnabled();
	
	private static final LoggerFactory LOGGER_FACTORY = new AndroidLoggerFactory();
	
	public static Logger getLogger(String tag) {
		return LOGGER_FACTORY.getLogger(tag);
	}
	
	public static Logger getLogger(Class<?> clz) {
		return LOGGER_FACTORY.getLogger(clz);
	}
	
	public static Logger getLogger() {
		return LOGGER_FACTORY.getLogger();
	}
}
