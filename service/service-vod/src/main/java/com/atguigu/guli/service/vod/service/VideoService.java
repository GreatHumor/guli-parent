package com.atguigu.guli.service.vod.service;

import com.aliyun.oss.ClientException;

import java.io.InputStream;

public interface VideoService {

    /**
     * 上传文件到阿里云vod服务器
     * @param file
     * @param fileName
     * @return
     */
    String uploadVideo(InputStream file,String fileName);

    void removeVideo(String videoSourceId) throws ClientException;














}
