package com.example.exportationmetadata.Repositories;

import com.example.exportationmetadata.Entities.ColumnInfo;
import com.example.exportationmetadata.Entities.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnInfoRepository extends JpaRepository<ColumnInfo, Long> {
    void deleteByFileInfo(FileInfo fileInfo);
}
