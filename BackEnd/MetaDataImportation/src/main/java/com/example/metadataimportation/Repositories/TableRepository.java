package com.example.metadataimportation.Repositories;

import com.example.metadataimportation.Entities.DataTable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.xml.crypto.Data;
import java.util.Optional;

public interface TableRepository extends JpaRepository<DataTable,Long> {
    Optional<DataTable> findByName(String name);

}
