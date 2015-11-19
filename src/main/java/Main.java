import com.testfairy.uploader.*;

public class Main {

	public static final String API_KEY = "3c0d457a5c1f93315d8e510ab694b82030eebed7";

	public static void main(String[] args) {
		Metrics metrics = new Metrics.Builder()
			.addBattery()
			.addCpu()
			.addNetwork()
			.build();

		Options options = new Options.Builder()
			.notifyTesters(true)
			.setAnonymous(true)
			.setComment("Newest Upload")
			.setIconWatermark(true)
			.setMetrics(metrics)
			.build();

		Listener listener = new Listener() {
			@Override
			public void onUploadStarted() {

			}

			@Override
			public void onUploadComplete() {

			}

			@Override
			public void onUploadFailed(Throwable throwable) {

			}

			@Override
			public void onProgress(float p) {

			}
		};


		IosUploader ios = new IosUploader.Builder(API_KEY)
			.setOptions(options)
			.build();

		ios.upload("/Users/vsharma/Desktop/Blah/Blah-original.ipa", listener);

		AndroidUploader android = new AndroidUploader.Builder(API_KEY)
			.setOptions(options)
			.build();
		android.upload("/Users/vsharma/testfairy/demo/app/build/outputs/apk/app-debug-unaligned.apk", listener);
	}
}
