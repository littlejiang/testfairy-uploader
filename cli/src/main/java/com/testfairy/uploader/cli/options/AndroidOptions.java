package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.AndroidUploader;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class AndroidOptions {
	private OptionSpec<String> instrumentation;
	private OptionSpec<String> keystorePath;
	private OptionSpec<String> keystoreAlias;
	private OptionSpec<String> keyPassword;
	private OptionSpec<String> storePassword;
	private OptionSpec<String> digestAlgorithm;
	private OptionSpec<String> signatureAlgorithm;

	public void configure(OptionParser parser) {
		instrumentation = parser.accepts("instrumentation", "Skip instrumentation of app (Android only), \"on\", or \"off\". Defaults to \"on\"").withRequiredArg();
		keystorePath = parser.accepts("keystore", "Keystore location").withRequiredArg();
		keystoreAlias = parser.accepts("alias", "Keystore alias").withRequiredArg();
		keyPassword = parser.accepts("keypass", "Password for private key (if different)").withRequiredArg();
		storePassword = parser.accepts("storepass", "Password for keystore integrity").withRequiredArg();
		digestAlgorithm = parser.accepts("digestalg", "Name of digest algorithm").withRequiredArg();
		signatureAlgorithm = parser.accepts("sigalg", "Name of signature algorithm").withRequiredArg();
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

		if (arguments.has(digestAlgorithm))
			android.setDigestAlgorithm(value(arguments, digestAlgorithm));

		if (arguments.has(signatureAlgorithm))
			android.setSignatureAlgorithm(value(arguments, signatureAlgorithm));
	}

	private static String value(OptionSet arguments, OptionSpec<String> option) {
		if (! arguments.has(option))
			return null;

		return arguments.valueOf(option);
	}
}
