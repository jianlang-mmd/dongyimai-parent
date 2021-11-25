package com.lang.search.controller;

import com.lang.entity.Result;
import com.lang.entity.StatusCode;
import com.lang.util.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;//文件服务器地址

    @PostMapping("/upload")
    public Result upload(@RequestParam(name = "file") MultipartFile file) {

        //1.取文件扩展名
        String originalFilename = file.getOriginalFilename();
        String exName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

        try {
            //2.创建一个fastdfs客户端
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:fdfs_client.conf");
            //3.执行上传处理
            String path = fastDFSClient.uploadFile(file.getBytes(), exName);
            //4.拼接返回url和ip地址，拼装成完整的url
            String url = FILE_SERVER_URL + path;
            return new Result(true, StatusCode.OK, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, StatusCode.ERROR, "上传失败！");
        }
    }
}
