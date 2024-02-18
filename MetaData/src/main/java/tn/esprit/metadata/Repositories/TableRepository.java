package tn.esprit.metadata.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.metadata.Entities.Table;

public interface TableRepository extends JpaRepository<Table, Long> {
}
