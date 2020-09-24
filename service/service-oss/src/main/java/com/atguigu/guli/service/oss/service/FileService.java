package com.atguigu.guli.service.oss.service;

import java.io.InputStream;

public interface FileService {

    String upload(InputStream inputStream,String module,String originalFilename);

    void removeFile(String url);
}
