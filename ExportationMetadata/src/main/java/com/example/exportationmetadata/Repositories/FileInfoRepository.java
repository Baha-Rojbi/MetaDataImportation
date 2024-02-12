package com.example.exportationmetadata.Repositories;

import com.example.exportationmetadata.Entities.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {
}
