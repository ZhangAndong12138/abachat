package org.makabaka.abaaba.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class FileController {
    @PostMapping
    public String upload(MultipartFile file) {
        String filePath = "D:\\makabaka\\upload";
        File localFile = new File(filePath + File.separator + file.getOriginalFilename());
        try {
            file.transferTo(localFile);
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
        return "上传成功";
    }
}
