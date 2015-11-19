package com.testfairy.uploader;

public interface Listener {
    void onUploadStarted();
    void onUploadComplete(Build build);
    void onUploadFailed(Throwable throwable);
    void onProgress(float p);
}
