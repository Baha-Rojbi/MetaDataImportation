package com.example.metadataimportation.Repositories.Importation;

import com.example.metadataimportation.Entities.Importation.DataTable;
import com.example.metadataimportation.Entities.Importation.Schema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchemaRepository extends JpaRepository<Schema,Long> {
    void deleteByParentDataTable(DataTable fileInfo);
}
