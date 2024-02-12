package com.example.exportationmetadata.Controllers;

import com.example.exportationmetadata.Entities.FileInfoDto;
import com.example.exportationmetadata.Services.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    // affichage
    @GetMapping("/all")
    public List<FileInfoDto> getAllFilesWithMetadata() {
        return fileProcessingService.getAllFilesWithMetadata();
    }
}
