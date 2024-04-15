import { Component } from '@angular/core';
import { UploadService } from '../services/upload.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrl: './file-upload.component.css'
})
export class FileUploadComponent {
  selectedFile: File | null = null; // Make selectedFile nullable
  description: string = '';

  constructor(private uploadService: UploadService,private router: Router) {}

  onFileSelected(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      this.selectedFile = fileList[0];
    } else {
      this.selectedFile = null;
    }
  }

  onUpload() {
    if (!this.description.trim()) { // Check if description is empty or only whitespace
      alert('Please enter a description for the file.');
      return;
    }
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile, this.selectedFile.name);
      formData.append('description', this.description);
      this.uploadService.uploadFile(formData).subscribe({
        next: (response) => {
          console.log("Upload successful", response);
          this.router.navigate(['/tables']); // Make sure this line executes
        },
        error: (error) => {
          console.error('Upload failed', error);
        }
      });
    } else {
      alert('No file selected');
    }
  }
}
