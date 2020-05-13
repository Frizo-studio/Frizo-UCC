package com.frizo.ucc.server.utils.files;

import com.frizo.ucc.server.config.AppProperties;
import com.frizo.ucc.server.exception.UnsupportedMediaTypeException;
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
        String fileExtensionName = oldFileName.substring(oldFileName.lastIndexOf("."));
        checkFileExtensionName(fileExtensionName);
        String newFileName = randomStr + fileExtensionName;
        File file = new File(path, newFileName);
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        uploadFile.transferTo(file);
        return newFileName;
    }

    private static void checkFileExtensionName(String fileExtensionName) {
        fileExtensionName = fileExtensionName.toLowerCase();
        if (
                fileExtensionName.equals("jpg") ||
                fileExtensionName.equals("jepg") ||
                fileExtensionName.equals("png") ||
                fileExtensionName.equals("svg") ||
                fileExtensionName.equals("heif") ||
                fileExtensionName.equals("heic")
        ){
            return;
        }else{
            throw new UnsupportedMediaTypeException("我們不支援您上傳檔案的格式");
        }
    }
}
