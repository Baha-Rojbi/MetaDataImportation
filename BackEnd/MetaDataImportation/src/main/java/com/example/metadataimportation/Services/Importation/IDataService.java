package com.example.metadataimportation.Services.Importation;

import com.example.metadataimportation.Entities.Importation.DataTable;
import com.example.metadataimportation.Entities.Importation.Schema;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDataService {
    public List<DataTable> getAllDataTables();
    public DataTable saveOrUpdateDataTable(DataTable dataTable);
    public Optional<DataTable> getDataTableById(Long id);
    public Optional<DataTable> findById(Long id);
    public List<Schema> getSchemasForTable(Long tableId);
    public Schema updateSchema(Long id, Schema schemaDetails);
    public void updateTags(Long idSchema, Set<String> tags);
    public Schema createSchema(Long tableId, Schema schema);
    public void deleteSchema(Long schemaId);
    public DataTable toggleArchiveStatus(Long id);
    public DataTable createDataTable(String name, String description);
}
