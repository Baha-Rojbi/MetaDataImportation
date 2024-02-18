package tn.esprit.metadata.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.metadata.Entities.Schema;
import tn.esprit.metadata.Entities.Table;
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
    public Table saveOrUpdateTableWithSchemas(Table tableEntity) {
        if (tableEntity.getIdTable() != null) {
            // Update existing table
            Table existingTable = tableRepository.findById(tableEntity.getIdTable()).orElseThrow(() -> new RuntimeException("Table not found"));
            existingTable.setName(tableEntity.getName());
            existingTable.setDescription(tableEntity.getDescription());
            existingTable.setCreationDate(tableEntity.getCreationDate());
            existingTable.setSize(tableEntity.getSize());
            existingTable.setCreator(tableEntity.getCreator());
            existingTable.setTags(tableEntity.getTags());
            // Assuming schemas are correctly set with references back to the table entity
            existingTable.getSchemas().clear();
            existingTable.getSchemas().addAll(tableEntity.getSchemas());
            tableEntity.getSchemas().forEach(schema -> schema.setParentTable(existingTable));
        }
        return tableRepository.save(tableEntity);
    }
    @Transactional
    public Schema addSchemaToTable(Long tableId, Schema schemaEntity) {
        Table table = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        schemaEntity.setParentTable(table);
        return schemaRepository.save(schemaEntity);
    }
    @Transactional
    public void deleteTableAndSchemas(Long tableId) {
        Table table = tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        tableRepository.delete(table);
    }
    @Transactional
    public List<Table>getAllTables(){
        return tableRepository.findAll();
    }
    @Transactional
    public Table getTableById(Long tableId) {
        Table table=tableRepository.findById(tableId).orElseThrow(() -> new RuntimeException("Table not found"));
        return table;
    }

    @Transactional
    public Table updateTableAttribute(Long tableId, String attributeName, Object newValue) {
        Table table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        switch (attributeName.toLowerCase()) {
            case "name":
                table.setName((String) newValue);
                break;
            case "description":
                table.setDescription((String) newValue);
                break;
            case "size":
                table.setSize((Double) newValue);
                break;
            case "creator":
                table.setCreator((String) newValue);
                break;
            case "tags":
                table.setTags((String) newValue);
                break;
            default:
                throw new IllegalArgumentException("Unknown attribute: " + attributeName);
        }

        return tableRepository.save(table);
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
