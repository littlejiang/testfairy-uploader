TestFairy Uploader
==================
[![Build Status](https://travis-ci.org/testfairy/testfairy-uploader.png)](https://travis-ci.org/testfairy/testfairy-uploader)

A java library to simplify uploading Android and iOS build artifacts to [TestFairy](https://www.testfairy.com). The library allows you to define the metrics and options for any given session of your artifact.

Download
--------

Download [the latest JAR][1] or grab via Maven:
```xml
<dependency>
  <groupId>com.testfairy</groupId>
  <artifactId>testfairy-uploader</artifactId>
  <version>1.0.0</version>
</dependency>
```
or Gradle:
```groovy
compile 'com.testfairy:testfairy-uploader:1.0.0'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].

Quick Start
-----------
Be sure to first get an API KEY from your [account][2] on TestFairy.

For Android Uploads
```java
Options options = new Options.Builder()
    .notifyTesters(true)
    .setComment("Uploading New Build")
    .build();

AndroidUploader uploader = new AndroidUploader.Builder(API_KEY)
    .setOptions(options)
    .setApkPath(APK_PATH)
    .setKeystore(KEYSTORE_PATH, KEYSTORE_ALIAS, STORE_PASSWORD, KEY_PASSWORD)
    .build();

uploader.upload(new Listener() {
    @Override public void onUploadStarted() {...}
    @Override public void onUploadComplete(Build build) {...}
    @Override public void onUploadFailed(Throwable throwable) {...}
    @Override public void onProgress(float p) {...}
});
```

For iOS Uploads
```java
Options options = new Options.Builder()
    .notifyTesters(true)
    .setComment("Uploading New Build")
    .build();

IOSUploader uploader = new IOSUploader.Builder(API_KEY)
    .setOptions(options)
    .setIpaPath(IPA_PATH)
    .build();

uploader.upload(new Listener() {
    @Override public void onUploadStarted() {...}
    @Override public void onUploadComplete(Build build) {...}
    @Override public void onUploadFailed(Throwable throwable) {...}
    @Override public void onProgress(float p) {...}
});
```

License
=======

    Copyright 2015 TestFairy.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.



 [1]: https://search.maven.org/remote_content?g=com.testfairy.testfairy%2Duploader&a=testfairy%2Duploader&v=LATEST
 [2]: https://app.testfairy.com/settings/
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/