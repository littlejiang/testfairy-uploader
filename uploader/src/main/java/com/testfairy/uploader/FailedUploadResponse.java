package com.testfairy.uploader;

class FailedUploadResponse {
    public final String status;
    public final String message;

    private FailedUploadResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
