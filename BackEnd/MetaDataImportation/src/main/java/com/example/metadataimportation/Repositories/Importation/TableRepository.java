package com.example.metadataimportation.Repositories.Importation;

import com.example.metadataimportation.Entities.Importation.DataTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TableRepository extends JpaRepository<DataTable,Long> {
    Optional<DataTable> findByName(String name);

}
