package tn.esprit.metadata.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.metadata.Entities.Schema;
import tn.esprit.metadata.Entities.DataTable;
import tn.esprit.metadata.Services.DataService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;

    // Create or update a table with schemas
    @PostMapping("/tables")
    public ResponseEntity<?> saveOrUpdateTable(@RequestBody DataTable dataTable) {
        DataTable savedDataTable = dataService.saveOrUpdateTableWithSchemas(dataTable);
        return ResponseEntity.ok(savedDataTable);
    }

    // Add a schema to an existing table
    @PostMapping("/tables/{tableId}/schemas")
    public ResponseEntity<?> addSchemaToTable(@PathVariable Long tableId, @RequestBody Schema schema) {
        Schema savedSchema = dataService.addSchemaToTable(tableId, schema);
        return ResponseEntity.ok(savedSchema);
    }

    // Delete a table and its schemas
    @DeleteMapping("/tables/{tableId}")
    public ResponseEntity<?> deleteTable(@PathVariable Long tableId) {
        dataService.deleteTableAndSchemas(tableId);
        return ResponseEntity.ok().build();
    }

    // Additional endpoint examples:
    // Get all tables
    @GetMapping("/tables")
    public ResponseEntity<?> getAllTables() {
        List<DataTable> dataTables = dataService.getAllTables();
        return ResponseEntity.ok(dataTables);
    }

    // Get a specific table by ID
    @GetMapping("/tables/{tableId}")
    public ResponseEntity<?> getTableById(@PathVariable Long tableId) {
        DataTable dataTable = dataService.getTableById(tableId);
        return ResponseEntity.ok(dataTable);
    }
    @PatchMapping("/tables/{tableId}")
    public ResponseEntity<?> updateTableAttribute(@PathVariable Long tableId,
                                                  @RequestParam String attributeName,
                                                  @RequestBody Object newValue) throws RuntimeException, IllegalArgumentException {
        DataTable updatedDataTable = dataService.updateTableAttribute(tableId, attributeName, newValue);
        return ResponseEntity.ok(updatedDataTable);
    }

    // Endpoint to update an attribute of a schema
    @PatchMapping("/schemas/{schemaId}")
    public ResponseEntity<?> updateSchemaAttribute(@PathVariable Long schemaId,
                                                   @RequestParam String attributeName,
                                                   @RequestBody Object newValue) throws RuntimeException, IllegalArgumentException {
        Schema updatedSchema = dataService.updateSchemaAttribute(schemaId, attributeName, newValue);
        return ResponseEntity.ok(updatedSchema);
    }
}
