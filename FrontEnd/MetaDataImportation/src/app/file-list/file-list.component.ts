import { Component, OnInit } from '@angular/core';
import { FileService } from '../services/file.service';
@Component({
  selector: 'app-file-list',
  templateUrl: './file-list.component.html',
  styleUrls: ['./file-list.component.css']
})
// file-list.component.ts
export class FileListComponent implements OnInit {
  files: any[] = [];

  constructor(private fileService: FileService) { }

  ngOnInit() {
    this.loadFiles();
  }

  loadFiles() {
    this.fileService.getFiles().subscribe(data => {
      console.log('Fichiers chargés:', data); // Pour déboguer
      this.files = data;
    }, error => {
      console.error('Erreur lors du chargement des fichiers', error);
    });
  }
  

  deleteFileById(id: number) {
    console.log('Tentative de suppression du fichier avec ID:', id); // Pour déboguer
    if (id !== undefined && confirm('Êtes-vous sûr de vouloir supprimer ce fichier ?')) {
      this.fileService.deleteFileById(id).subscribe(() => {
        alert('Fichier supprimé avec succès');
        this.loadFiles(); // Rechargez la liste des fichiers
      }, error => {
        console.error('Erreur lors de la suppression du fichier', error);
      });
    } else {
      alert('Erreur: ID du fichier est undefined');
    }
  }
  

}

