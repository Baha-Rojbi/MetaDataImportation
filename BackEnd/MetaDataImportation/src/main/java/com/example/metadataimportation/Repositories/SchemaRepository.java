package com.example.metadataimportation.Repositories;

import com.example.metadataimportation.Entities.DataTable;
import com.example.metadataimportation.Entities.Schema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SchemaRepository extends JpaRepository<Schema,Long> {
    void deleteByParentDataTable(DataTable fileInfo);
}
