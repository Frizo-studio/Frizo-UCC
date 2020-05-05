package com.frizo.ucc.server.utils.files;

import com.frizo.ucc.server.config.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FrizoFileUtils {

    public static String storePhoto(MultipartFile uploadFile, String path) throws IOException {
        if (uploadFile == null){
            return null;
        }
        String oldFileName = uploadFile.getOriginalFilename();
        String randomStr = UUID.randomUUID().toString();
        String newFileName = randomStr + oldFileName.substring(oldFileName.lastIndexOf("."));
        File file = new File(path, newFileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        uploadFile.transferTo(file);
        return newFileName;
    }
}
