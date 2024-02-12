package com.example.exportationmetadata.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@ToString
public class FileInfoDto {
    private String fileName;
    private LocalDateTime creationDate;
    private List<ColumnInfoDto> columnInfos;

    // Constructeurs, Getters et Setters
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class ColumnInfoDto {
        private String columnName;
        private String columnType;

        // Constructeurs, Getters et Setters
    }
}
