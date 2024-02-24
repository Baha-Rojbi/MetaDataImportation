package tn.esprit.metadata.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.metadata.Entities.DataTable;

import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<DataTable, Long> {
    Optional<DataTable> findByName(String name);
    @Query("SELECT dt FROM DataTable dt JOIN FETCH dt.schemas")
    List<DataTable> findAllWithSchemas();

}
