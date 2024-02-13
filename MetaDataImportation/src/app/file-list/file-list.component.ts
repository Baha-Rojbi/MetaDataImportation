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
      this.files = data;
      console.log(this.files); // Pour déboguer et voir les données récupérées
    }, error => {
      console.error("Erreur lors de la récupération des fichiers", error);
    });
  }
}

