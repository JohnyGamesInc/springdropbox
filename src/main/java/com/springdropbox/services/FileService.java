package com.springdropbox.services;

import com.springdropbox.entities.File;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FileService {

    List<String> findAllPathsByUserId(Long userId);

    List<File> findAllFilesByUserId(Long userId);

    File findFileByPath(String path);

    Path upLoad(String filename);

    Resource loadAsResource(String filename);

    void deleteFilesByPath(String path);

    void store(MultipartFile file);

}
