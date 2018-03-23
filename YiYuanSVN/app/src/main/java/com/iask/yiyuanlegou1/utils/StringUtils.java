/*
 * 系统名称：lswuyou
 * 类  名  称：StringUtil.java
 * 软件版权：无届网络科技有限公司
 * 系统版本：1.0
 * 开发人员： huashigen
 * 开发时间： 2015-8-7 下午4:55:15
 * 功能说明： 字符串常用处理类
 * 审核人员：
 * 相关文档
 * 修改记录： 需求编号	  修改日期	          修改人员          修改说明
 */
package com.iask.yiyuanlegou1.utils;

import com.iask.yiyuanlegou1.log.Logger;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
	private static Logger logger = Logger.getLogger("StringUtils");

	// private static final String PNG = ".png";
	// private static final String JPG = ".jpg";
	// private static final String DATE_FORMAT_1 = "yyyy-MM-dd HH:mm:ss";

	public static boolean isNotEmpty(String str) {
		return str != null && str.length() > 0;
	}

	/**
	 * 前后空格
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 限制最小长度
	 * 
	 * @param str
	 * @param limitNum
	 * @return
	 */
	public static boolean isLimit(String str, int limitNum) {
		return str != null && str.length() >= limitNum;
	}

	/**
	 * 限制最大长度
	 * 
	 * @param str
	 * @param limitNum
	 * @return
	 */
	public static boolean isMax(String str, int limitNum) {
		return str != null && str.length() <= limitNum;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().length() == 0;
	}

	/**
	 * 判断str 是否为空 是：返回 空字符串 否：返回 原数据
	 * 
	 * @param str
	 * @return
	 */
	public static String handString(String str) {
		return isEmpty(str) ? "" : str;
	}

	public static boolean isNotEmpty(Collection c) {
		return c != null && c.size() > 0;
	}

	public static boolean isEmpty(Collection c) {
		return c == null || c.size() == 0;
	}

	/**
	 * 1转换01
	 * 
	 * @param single
	 * @return
	 */
	public static String singleChange(int single) {
		if (single < 10) {
			return "0" + single;
		}

		return single + "";
	}

	// 取汉字首字母
	public static String getAlpha(String str) {
		if (str == null) {
			return "#";
		}

		if (str.trim().length() == 0) {
			return "#";
		}

		char c = str.trim().substring(0, 1).charAt(0);

		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(c + "").matches()) {
			return (c + "").toUpperCase();
		} else {
			return "#";
		}
	}

	/**
	 * 元转换万
	 * 
	 * @param firstamt
	 * @return
	 */
	public static int getFirstamt(String firstamt) {
		if (isEmpty(firstamt))
			return 0;

		int firstamtInt = Integer.parseInt(firstamt);

		return firstamtInt / 10000;
	}

	/**
	 * 在flowMenu里onclick 的url带参数的特殊处理
	 * 
	 * @param path
	 * @param id
	 * @param type
	 * @return
	 */
	public static String modifyGotoPath(String path, String id, String type) {
		String newPath;
		int index = path.indexOf("%@");
		int endIndex = path.lastIndexOf("%@");
		newPath = path.substring(0, index) + id + path.substring(index + 2, endIndex) + type + path.substring(endIndex + 2);
		logger.error("newpath:" + newPath);
		return newPath;
	}

	public static String transform(String content) {
		if (null == content || "".equals((content))) {
			return content;
		}
		content = content.replaceAll("&", "&amp;");
		content = content.replaceAll("<", "&lt;");
		content = content.replaceAll("\"", "&quot;");
		// content = content.replaceAll(" ", "&nbsp;");
		content = content.replaceAll(">", "&gt;");
		// content = content.replaceAll( "“", "&ldquo;");
		// content = content.replaceAll( "”", "&rdquo;");
		// content = content.replaceAll( "—", "&mdash;");
		// content = content.replaceAll("\n", "<br>");
		return content;
	}

	public static String isearlierred(String isearlierred) {
		if (!StringUtils.isEmpty(isearlierred)) {
			int flag = Integer.parseInt(isearlierred);
			if (flag == 1) {
				return "(可赎回)";
			}
		}

		return "(不可赎回)";
	}

	/**
	 * 小数点带一位
	 * 
	 * @param pParam
	 * @return
	 */
	public static String decimalFormat(double pParam) {
		DecimalFormat df = new DecimalFormat("0.0");
		return df.format(pParam);
	}

	/**
	 * 四舍五入
	 * 
	 * @param pDouble
	 * @param scale
	 *            小数点位数
	 * @return
	 */
	public static String decimalFormatScale(double pDouble, int scale) {
		BigDecimal bigDecimal = new BigDecimal(pDouble);
		return bigDecimal.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 判断是否是手机号码
	 * 
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if (isEmpty(mobiles)) {
			return false;
		}
		Pattern p = Pattern.compile("^1\\d{10}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/**
	 * 截取手机号前3位和后4位(其余显示*号)
	 * 
	 * @param mobile
	 * @return
	 */
	public static String showMobileCiphertext(String mobile) {
		String newNumber = "****";
		if (!isEmpty(mobile)) {
			return mobile.substring(0, 3) + newNumber + mobile.substring(mobile.length() - 4);
		}

		return newNumber;
	}

	/**
	 * 
	 * @param idCard
	 * @return
	 */
	public static String showIdCardCiphertext(String idCard) {
		String newNumber = "********";
		if (!isEmpty(idCard)) {
			if (idCard.length() == 18)
				return idCard.substring(0, 6) + newNumber + idCard.substring(idCard.length() - 4);
		}

		return idCard;
	}

	/**
	 * 判断密码是否合法
	 */
	public static boolean isPasswdLegal(String text) {
		Pattern p = Pattern.compile("[a-zA-Z0-9]+");
		Matcher m = p.matcher(text);
		if (m.matches()) {
			return true;
		}
		return false;
	}
}