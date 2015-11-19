import com.testfairy.uploader.*;

public class Main {

    public static final String API_KEY = "7ac85171ea92ad548396846dc4310e1e625fe65a";

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
            public void onUploadComplete(Build build) {
                System.out.println(build.toString());
            }

            @Override
            public void onUploadFailed(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onProgress(float p) {

            }
        };


        IosUploader ios = new IosUploader.Builder(API_KEY)
            .setOptions(options)
            .setIpaPath("/Users/vsharma/Desktop/Blah/Blah-original.ipa")
            .build();

        ios.upload(listener);

        AndroidUploader android = new AndroidUploader.Builder(API_KEY)
            .setOptions(options)
            .setApkPath("/Users/vsharma/testfairy/demo/app/build/outputs/apk/app-debug-unaligned.apk")
            .build();

        android.upload(listener);
    }
}
