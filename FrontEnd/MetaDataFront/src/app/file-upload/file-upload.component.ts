import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent {
  selectedFile: File | null = null;

  constructor(private http: HttpClient, private router: Router,private fileService: DataService) {}

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0] || null;
  }

  onUpload() {
    if (this.selectedFile) {
      this.fileService.uploadFile(this.selectedFile).subscribe(
        response => {
          console.log('Upload successful', response);
          // Ajoutez ici toute logique supplémentaire après l'upload réussi
          this.router.navigate(['/tables']);
        },
        error => {
          console.error('Upload error', error);
          // Gérez l'erreur d'upload
        }
      );
    } else {
      console.warn('No file selected');
      // Gérez le cas où aucun fichier n'est sélectionné
    }
  }
}
