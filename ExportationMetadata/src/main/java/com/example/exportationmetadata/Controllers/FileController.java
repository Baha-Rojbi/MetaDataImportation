package com.example.exportationmetadata.Controllers;

import com.example.exportationmetadata.Services.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileProcessingService fileProcessingService;

    // Suppression par ID
    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<?> deleteFileById(@PathVariable Long id) {
        try {
            fileProcessingService.deleteFileById(id);
            return ResponseEntity.ok("Fichier et métadonnées supprimés avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fichier non trouvé");
        }
    }

    // Suppression par nom
    @DeleteMapping("/deleteByName/{name}")
    public ResponseEntity<?> deleteFileByName(@PathVariable String name) {
        try {
            fileProcessingService.deleteFileByName(name);
            return ResponseEntity.ok("Fichier et métadonnées supprimés avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fichier non trouvé");
        }
    }
}
