package tn.esprit.metadata.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.metadata.Entities.FileInfoDto;
import tn.esprit.metadata.Entities.Schema;
import tn.esprit.metadata.Entities.DataTable;
import tn.esprit.metadata.Services.DataService;
import tn.esprit.metadata.Services.FileProcessingService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DataService dataService;
    @Autowired
    private FileProcessingService fileProcessingService;

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
    @GetMapping("")
    public ResponseEntity<?> getAllTables() {
        List<FileInfoDto> dataTables = fileProcessingService.getAllFilesWithMetadata();
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

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String response = fileProcessingService.processFile(file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du traitement du fichier: " + e.getMessage());
        }
    }
    @GetMapping("/tables")
    public ResponseEntity<?> getAllDataTables(){
        List<DataTable> dataTable = dataService.getAllTables();
        return ResponseEntity.ok(dataTable);
    }
}
