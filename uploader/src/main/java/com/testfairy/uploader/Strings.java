package com.testfairy.uploader;

import java.util.List;

public class Strings {
	private Strings(){}

	static boolean isEmpty(String input) {
		return input == null || input.trim().isEmpty();
	}

	static String join(List<String> items, String separator) {
		if (items == null)
			return "";

		StringBuilder builder = new StringBuilder();
		for (int index = 0, length = items.size(); index < length; index++) {
			builder.append(items.get(index));
			if (index != (length -1))builder.append(separator);
		}

		return builder.toString();
	}
}
