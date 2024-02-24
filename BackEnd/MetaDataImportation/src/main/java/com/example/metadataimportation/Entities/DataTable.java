package com.example.metadataimportation.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class DataTable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTable;
    private String name; // This will now hold the file name without the extension
    private String source; // Added attribute to store the file name with extension
    private String description;
    private LocalDateTime creationDate;
    private Double size;
    private String creator;

    @JsonIgnore
    @OneToMany(mappedBy = "parentDataTable", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Schema> schemas;
}
