package tn.esprit.metadata.Entities;

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
public class Table implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTable;
    private String name;
    private String description;
    private Date creationDate;
    private Double size;
    private String creator;
    private String tags;

    @OneToMany(mappedBy = "tableEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Schema> schemas = new HashSet<>();
}
