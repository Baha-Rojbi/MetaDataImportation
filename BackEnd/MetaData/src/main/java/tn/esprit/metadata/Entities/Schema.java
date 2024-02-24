package tn.esprit.metadata.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Schema implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSchema;
    private String name;
    private String type;
    private String description;
    private String tags;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_table")
    private DataTable parentDataTable;
}
