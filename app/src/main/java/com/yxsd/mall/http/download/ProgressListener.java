package com.yxsd.mall.http.download;

public interface ProgressListener {

    void onProgress(long currentBytes, long contentLength, boolean done);
}
