package tn.esprit.metadata.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
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
    private String name;
    private String description;
    private Date creationDate;
    private Double size;
    private String creator;
    private String tags;
@JsonIgnore
    @OneToMany(mappedBy = "parentDataTable", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schema> schemas = new HashSet<>();
}
