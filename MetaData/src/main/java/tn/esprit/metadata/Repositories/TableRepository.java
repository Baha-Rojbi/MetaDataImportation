package tn.esprit.metadata.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.metadata.Entities.DataTable;

public interface TableRepository extends JpaRepository<DataTable, Long> {
}
