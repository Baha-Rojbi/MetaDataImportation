package com.example.metadataimportation.Controllers;

import com.example.metadataimportation.Entities.DataTable;
import com.example.metadataimportation.Entities.Schema;
import com.example.metadataimportation.Services.DataService;
import com.example.metadataimportation.Services.FileProcessService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class MetaDataContoller {
    @Autowired
    private FileProcessService fileProcessService;
    @Autowired
    private DataService dataTableService;
    @Autowired
    public MetaDataContoller(DataService dataTableService) {
        this.dataTableService = dataTableService;
    }

    @GetMapping("/tables")
    public List<DataTable> getAllDataTables() {
        return dataTableService.getAllDataTables();
    }

    @GetMapping("/tables/{id}")
    public ResponseEntity<DataTable> getDataTableById(@PathVariable Long id) {
        return dataTableService.getDataTableById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/tables")
    public DataTable createDataTable(@RequestBody DataTable dataTable) {
        return dataTableService.saveOrUpdateDataTable(dataTable);
    }

    @PutMapping("/tables/{id}")
    public ResponseEntity<DataTable> updateDataTable(@PathVariable Long id, @RequestBody DataTable dataTable) {
        dataTable.setIdTable(id); // Make sure the ID is set correctly
        DataTable updatedDataTable = dataTableService.saveOrUpdateDataTable(dataTable);
        return ResponseEntity.ok(updatedDataTable);
    }


    // Add other endpoints as needed
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("description") String description) {
        try {
            String message = fileProcessService.processFile(file, description); // Adjust your service method to accept description
            return ResponseEntity.ok().body(message);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not upload the file: " + e.getMessage());
        }
    }
    @GetMapping("/tables/{id}/schemas")
    public ResponseEntity<List<Schema>> getSchemasForTable(@PathVariable Long id) {
        List<Schema> schemas = dataTableService.getSchemasForTable(id);
        return ResponseEntity.ok(schemas);
    }
    @PutMapping("/schemas/{id}")
    public ResponseEntity<Schema> updateSchema(@PathVariable Long id, @RequestBody Schema schema) {
        Schema updatedSchema = dataTableService.updateSchema(id, schema);
        return new ResponseEntity<>(updatedSchema, HttpStatus.OK);
    }
    @PutMapping("/schemas/{idSchema}/tags")
    public ResponseEntity<?> updateSchemaTags(@PathVariable Long idSchema, @RequestBody Set<String> tags) {
        try {
            dataTableService.updateTags(idSchema, tags);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
