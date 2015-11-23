package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.AndroidUploader;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.File;

public class AndroidOptions {
	private OptionSpec<String> instrumentation;
	private OptionSpec<String> keystorePath;
	private OptionSpec<String> keystoreAlias;
	private OptionSpec<String> keyPassword;
	private OptionSpec<String> storePassword;

	public void configure(OptionParser parser) {
		instrumentation = parser.accepts("instrumentation", "Skip instrumentation of app (Android only), \"on\", or \"off\". Defaults to \"on\"").withRequiredArg();
		keystorePath = parser.accepts("keystore", "Keystore location").withRequiredArg();
		keystoreAlias = parser.accepts("alias", "Keystore alias").withRequiredArg();
		keyPassword = parser.accepts("keypass", "Password for private key (if different)").withRequiredArg();
		storePassword = parser.accepts("storepass", "Password for keystore integrity").withRequiredArg();
	};

	public void apply(OptionSet arguments, AndroidUploader.Builder android) {
		boolean enableInstrumentation = true;
		if (arguments.has(instrumentation)) {
			String value = arguments.valueOf(instrumentation);
			enableInstrumentation = ! "off".equals(value);
		}
		android.enableInstrumentation(enableInstrumentation);

		if (! enableInstrumentation)
			return;

		android.setKeystore(
			value(arguments, keystorePath),
			value(arguments, keystoreAlias),
			value(arguments, storePassword),
			value(arguments, keyPassword)
		);
	}

	private static String value(OptionSet arguments, OptionSpec<String> option) {
		if (! arguments.has(option))
			return null;

		return arguments.valueOf(option);
	}
}
