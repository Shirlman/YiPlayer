package com.shirlman.yiplayer.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
	private static final String TAG = StringUtils.class.getSimpleName();

	public static boolean notNullNorEmpty(String str) {
		return str != null && !str.isEmpty();
	}

	public static boolean nullOrEmpty(String str) {
		return str == null || str.isEmpty();
	}

	public static String join(List<String> list, String separator) {
		if(list == null || list.size() == 0) {
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < list.size(); i++) {
			stringBuilder.append(list.get(i));

			if (i < list.size() - 1) {
				stringBuilder.append(separator);
			}
		}

		return stringBuilder.toString();
	}

	public static String join(final ArrayList<String> array, final String separator) {
		StringBuffer result = new StringBuffer();

		if (array != null && array.size() > 0) {
			for (String str : array) {
				result.append(str);
				result.append(separator);
			}

			result.delete(result.length() - 1, result.length());
		}

		return result.toString();
	}

	public static String join(final String[] array, final String separator) {
		if(array == null || array.length == 0) {
			return "";
		}

		StringBuilder stringBuilder = new StringBuilder();

		for (int i = 0; i < array.length; i++) {
			stringBuilder.append(array[i]);

			if (i < array.length - 1) {
				stringBuilder.append(separator);
			}
		}

		return stringBuilder.toString();
	}

	public static String getTimeDisplayString(long milliSeconds) {
		String timeFormat = milliSeconds > 60 * 60 * 1000 ? "HH:mm:ss" : "mm:ss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.ENGLISH);
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		return simpleDateFormat.format(milliSeconds);
	}

	public static boolean isChineseChar(String str) {
		boolean temp = false;
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);

		if (m.find()) {
			temp = true;
		}

		return temp;
	}
}
