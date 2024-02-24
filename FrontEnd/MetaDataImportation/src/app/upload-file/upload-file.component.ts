import { Component } from '@angular/core';
import { FileService } from '../services/file.service';
@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.css']
})
// upload-file.component.ts
export class UploadFileComponent {
  selectedFile: File | null = null;

  constructor(private fileService: FileService) { }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0] || null;
  }

  upload() {
    if (this.selectedFile) {
      this.fileService.uploadFile(this.selectedFile).subscribe(
        response => {
          console.log('Upload successful', response);
          // Ajoutez ici toute logique supplémentaire après l'upload réussi
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


