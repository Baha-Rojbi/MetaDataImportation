package tn.esprit.metadata.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.metadata.Entities.Schema;

public interface SchemaRepository extends JpaRepository<Schema, Long> {
}
