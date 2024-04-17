package com.example.metadataimportation.Services;

import com.example.metadataimportation.Entities.DataTable;
import com.example.metadataimportation.Entities.Schema;
import com.example.metadataimportation.Repositories.SchemaRepository;
import com.example.metadataimportation.Repositories.TableRepository;


import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
            dataTable.setModificationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            return tableRepository.save(dataTable); // Update existing
        } else {
            dataTable.setModificationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
            return tableRepository.save(dataTable); // Create new
        }
    }


    public Optional<DataTable> findById(Long id) {
        return tableRepository.findById(id);
    }
    // Add other service methods as needed
    public List<Schema> getSchemasForTable(Long tableId) {
        if (tableId == 0) {
            // Return all schemas if tableId is 0
            return new ArrayList<>(schemaRepository.findAll());
        } else {
            DataTable dataTable = tableRepository.findById(tableId)
                    .orElseThrow(() -> new RuntimeException("DataTable not found with id " + tableId));
            return new ArrayList<>(dataTable.getSchemas());
        }
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
    @Transactional
    public Schema createSchema(Long tableId, Schema schema) {
        DataTable dataTable = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("DataTable not found with id: " + tableId));
        schema.setParentDataTable(dataTable);
        return schemaRepository.save(schema);
    }

    @Transactional
    public void deleteSchema(Long schemaId) {
        if (!schemaRepository.existsById(schemaId)) {
            throw new EntityNotFoundException("Schema not found with id: " + schemaId);
        }
        schemaRepository.deleteById(schemaId);
    }
    @Transactional
    public DataTable toggleArchiveStatus(Long id) {
        Optional<DataTable> optionalDataTable = tableRepository.findById(id);
        if (optionalDataTable.isPresent()) {
            DataTable dataTable = optionalDataTable.get();
            dataTable.setArchived(!dataTable.isArchived());
            return tableRepository.save(dataTable);
        } else {
            throw new EntityNotFoundException("DataTable not found with id: " + id);
        }
    }
    @Transactional
    public DataTable createDataTable(String name, String description) {
        DataTable dataTable = new DataTable();
        dataTable.setName(name);
        dataTable.setDescription(description);
        dataTable.setSource("table"); // As specified
        dataTable.setFileType("none"); // As specified
        dataTable.setCreationDate(LocalDateTime.now());
        dataTable.setModificationDate(LocalDateTime.now());
        dataTable.setSize(0.0);
        dataTable.setCreator("System");
        return tableRepository.save(dataTable);
    }






}
