package com.example.exportationmetadata.Repositories;

import com.example.exportationmetadata.Entities.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
    Optional<FileInfo> findByFileName(String fileName);
}
