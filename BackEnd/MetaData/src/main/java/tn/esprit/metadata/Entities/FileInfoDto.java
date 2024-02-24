package tn.esprit.metadata.Entities;

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
    private Long idTable;
    private String name;
    private String description;
    private LocalDateTime creationDate;
    private Double size;
    private String creator;
    private String tags;
    private List<ColumnInfoDto> columnInfos;

    // Constructeurs, Getters et Setters
    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class ColumnInfoDto {
        private Long idSchema;
        private String name;
        private String type;
        private String description;
        private String tags;

        // Constructeurs, Getters et Setters
    }
}
