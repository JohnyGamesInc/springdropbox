package com.springdropbox.repositories;

import com.springdropbox.entities.File;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {

    List<String> findAllByUserId(Long id);

    List<File> findAllFilesByUser_Id(Long userId);

    File findFileByPath(String path);

    Boolean existsByPath(String path);

    @Query("delete from File where path = :path")
    @Modifying
    void deleteFilesByPath(@Param("path") String path);
}
