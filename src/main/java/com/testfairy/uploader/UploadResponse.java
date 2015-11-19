package com.testfairy.uploader;

public class UploadResponse {
	private final String status;
	private final String app_name;
	private final String app_version;
	private final String file_size;
	private final String build_url;
	private final String instrumented_url;
	private final String invite_testers_url;
	private final String icon_url;

	UploadResponse(
		String status,
		String app_name,
		String app_version,
		String file_size,
		String build_url,
		String instrumented_url,
		String invite_testers_url,
		String icon_url
	) {
		this.status = status;
		this.app_name = app_name;
		this.app_version = app_version;
		this.file_size = file_size;
		this.build_url = build_url;
		this.instrumented_url = instrumented_url;
		this.invite_testers_url = invite_testers_url;
		this.icon_url = icon_url;
	}

	public String status() {
		return this.status;
	}

	public String appName() {
		return app_name;
	}

	public String appVersion() {
		return app_version;
	}

	public String fileSize() {
		return file_size;
	}

	public String buildUrl() {
		return build_url;
	}

	public String instrumentedUrl() {
		return instrumented_url;
	}

	public String inviteTestersUrl() {
		return invite_testers_url;
	}

	public String iconUrl() {
		return icon_url;
	}

	@Override
	public String toString() {
		return "UploadResponse{" +
				"status='" + status + '\'' +
				", app_name='" + app_name + '\'' +
				", app_version='" + app_version + '\'' +
				", file_size='" + file_size + '\'' +
				", build_url='" + build_url + '\'' +
				", instrumented_url='" + instrumented_url + '\'' +
				", invite_testers_url='" + invite_testers_url + '\'' +
				", icon_url='" + icon_url + '\'' +
				'}';
	}
}
