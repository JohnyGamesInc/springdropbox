package com.springdropbox.repositories;

import com.springdropbox.entities.File;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    List<String> findAllByUserId(Long id);

    List<File> findAllFilesByUser_Id(Long userId);

    File findFileByPath(String path);

    Boolean existsByPath(String path);

    void deleteByPath(String path);
}
