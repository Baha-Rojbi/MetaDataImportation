package com.example.metadataimportation.Services;

import com.example.metadataimportation.Entities.DataTable;
import com.example.metadataimportation.Entities.Schema;
import com.example.metadataimportation.Repositories.SchemaRepository;
import com.example.metadataimportation.Repositories.TableRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DataService {
    @Autowired
    private final TableRepository tableRepository;
    @Autowired
    private SchemaRepository schemaRepository;
    @Autowired
    public DataService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<DataTable> getAllDataTables() {
        return tableRepository.findAll();
    }

    public Optional<DataTable> getDataTableById(Long id) {
        return tableRepository.findById(id);
    }
@Transactional
    public DataTable saveOrUpdateDataTable(DataTable dataTable) {
        if (dataTable.getIdTable() != null && tableRepository.existsById(dataTable.getIdTable())) {
            return tableRepository.save(dataTable); // Update existing
        } else {
            return tableRepository.save(dataTable); // Create new
        }
    }


    public Optional<DataTable> findById(Long id) {
        return tableRepository.findById(id);
    }
    // Add other service methods as needed
    public List<Schema> getSchemasForTable(Long tableId) {
        DataTable dataTable = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("DataTable not found with id " + tableId));
        return new ArrayList<>(dataTable.getSchemas());
    }
    public Schema updateSchema(Long id, Schema schemaDetails) {
        Schema schema = schemaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schema not found for id: " + id));

        schema.setName(schemaDetails.getName());
        schema.setType(schemaDetails.getType());
        schema.setDescription(schemaDetails.getDescription());
        schema.setTags(schemaDetails.getTags());

        return schemaRepository.save(schema);
    }
    public void updateTags(Long idSchema, Set<String> tags) {
        Schema schema = schemaRepository.findById(idSchema)
                .orElseThrow(() -> new EntityNotFoundException("Schema not found"));
        schema.setTags(tags);
        schemaRepository.save(schema);
    }

}
