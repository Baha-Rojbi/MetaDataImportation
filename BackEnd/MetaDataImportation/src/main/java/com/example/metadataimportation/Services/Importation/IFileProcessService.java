package com.example.metadataimportation.Services.Importation;

import org.springframework.web.multipart.MultipartFile;

public interface IFileProcessService {
    public String processFile(MultipartFile file, String description) throws Exception;
}
