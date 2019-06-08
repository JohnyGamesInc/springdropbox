package com.springdropbox.services;

import com.springdropbox.entities.User;
import com.springdropbox.exceptions.StorageException;
import com.springdropbox.exceptions.StorageFileNotFoundException;
import com.springdropbox.repositories.FileRepository;
import com.springdropbox.repositories.UserRepository;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
@Data
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;
    @Autowired
    UserRepository userRepository;
    public static Path rootLocation;
    public static HttpSession session;
    public static User currentUser;

    @Override
    public List<String> findAllPathsByUserId(Long userId) {
        return fileRepository.findAllByUserId(userId);
    }

    @Override
    public List<com.springdropbox.entities.File> findAllFilesByUserId(Long userId) {
        return fileRepository.findAllFilesByUser_Id(userId);
    }

    @Override
    public com.springdropbox.entities.File findFileByPath(String path) {
        return fileRepository.findFileByPath(path);
    }

    @Override
    public Path upLoad(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = upLoad(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    @Transactional
    public void deleteFilesByPath(String fileName) {
        File filePath = new File(rootLocation.toString() + "/" + fileName);
        boolean isExists = fileRepository.existsByPath(filePath.toString());
        FileSystemUtils.deleteRecursively(filePath);
        fileRepository.deleteFilesByPath(filePath.toString());
    }

    @Override
    @Transactional
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        File filePath = new File(rootLocation + "/" + filename);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            if (fileRepository.existsByPath(filePath.toString())) {
                throw new StorageException("File with this title already exists " + filename);
            } else {
                try (InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, this.rootLocation.resolve(filename),
                               StandardCopyOption.REPLACE_EXISTING
                    );

                    com.springdropbox.entities.File fileORMObject = new com.springdropbox.entities.File(
                            currentUser,
                            filename,
                            filePath.toString(),
                            file.getSize()
                    );
                    fileRepository.save(fileORMObject);
                }
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

}
