package com.adtech.cn.controller;

import com.adtech.cn.service.impl.FileServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2018/3/19.
 */
@Controller
@RequestMapping("/dataimport")
public class FileController {

    @Value("${excel.template}")
    private String templateUrl;
    @Autowired
    private FileServiceImpl fileServiceImpl;

    /**
     * 导入值域
     * @param file
     * @param dz
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/excelUpload")
    public String excelUpload(MultipartFile file, @RequestParam String dz) {
        return fileServiceImpl.excelUpload(file, dz);
    }

    /**
     * 下载值域模版
     * @param response
     */
    @RequestMapping(value = "/download")
    public void download(HttpServletResponse response) {
        try {
            InputStream inputStream = null;
            Resource resource = new PathMatchingResourcePatternResolver().getResource(templateUrl);
            inputStream = resource.getInputStream();
            System.out.println(resource.getFilename());
            response.setHeader("Content-Disposition", "attachment;fileName=" + resource.getFilename());
            byte[] buffer = new byte[1024];
            BufferedInputStream bis = null;
            OutputStream os = null; //输出流
            try {
                os = response.getOutputStream();
                bis = new BufferedInputStream(inputStream);
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bis.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
