package tn.esprit.metadata.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.metadata.Entities.Schema;
import tn.esprit.metadata.Entities.DataTable;
import tn.esprit.metadata.Repositories.SchemaRepository;
import tn.esprit.metadata.Repositories.TableRepository;

import java.util.List;

@Service
public class DataService {
    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private SchemaRepository schemaRepository;
    @Transactional
    public DataTable saveOrUpdateTableWithSchemas(DataTable dataTableEntity) {
        if (dataTableEntity.getIdTable() != null) {
            // Update existing table
            DataTable existingDataTable = tableRepository.findById(dataTableEntity.getIdTable()).orElseThrow(() -> new RuntimeException("Table not found"));
            existingDataTable.setName(dataTableEntity.getName());
            existingDataTable.setDescription(dataTableEntity.getDescription());
            existingDataTable.setCreationDate(dataTableEntity.getCreationDate());
            existingDataTable.setSize(dataTableEntity.getSize());
            existingDataTable.setCreator(dataTableEntity.getCreator());
            existingDataTable.setTags(dataTableEntity.getTags());
            // Assuming schemas are correctly set with references back to the table entity
            existingDataTable.getSchemas().clear();
            existingDataTable.getSchemas().addAll(dataTableEntity.getSchemas());
            dataTableEntity.getSchemas().forEach(schema -> schema.setParentDataTable(existingDataTable));
        }
        return tableRepository.save(dataTableEntity);
    }
    @Transactional
    public Schema addSchemaToTable(Long tableId, Schema schemaEntity) {
        DataTable dataTable = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        schemaEntity.setParentDataTable(dataTable);
        return schemaRepository.save(schemaEntity);
    }
    @Transactional
    public void deleteTableAndSchemas(Long tableId) {
        DataTable dataTable = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        tableRepository.delete(dataTable);
    }
@Transactional
    public List<DataTable>getAllTables(){
        return tableRepository.findAllWithSchemas();
    }
    @Transactional
    public DataTable getTableById(Long tableId) {
        DataTable dataTable =tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        return dataTable;
    }

    @Transactional
    public DataTable updateTableAttribute(Long tableId, String attributeName, Object newValue) {
        DataTable dataTable = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        switch (attributeName.toLowerCase()) {
            case "name":
                dataTable.setName((String) newValue);
                break;
            case "description":
                dataTable.setDescription((String) newValue);
                break;
            case "size":
                dataTable.setSize((Double) newValue);
                break;
            case "creator":
                dataTable.setCreator((String) newValue);
                break;
            case "tags":
                dataTable.setTags((String) newValue);
                break;
            default:
                throw new IllegalArgumentException("Unknown attribute: " + attributeName);
        }

        return tableRepository.save(dataTable);
    }
    @Transactional
    public Schema updateSchemaAttribute(Long schemaId, String attributeName, Object newValue) {
        Schema schema = schemaRepository.findById(schemaId)
                .orElseThrow(() -> new RuntimeException("Schema not found"));

        switch (attributeName.toLowerCase()) {
            case "name":
                schema.setName((String) newValue);
                break;
            case "type":
                schema.setType((String) newValue);
                break;
            case "description":
                schema.setDescription((String) newValue);
                break;
            case "tags":
                schema.setTags((String) newValue);
                break;
            default:
                throw new IllegalArgumentException("Unknown attribute: " + attributeName);
        }

        return schemaRepository.save(schema);
    }


}
