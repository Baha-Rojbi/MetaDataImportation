import { Component } from '@angular/core';
import { UploadService } from '../services/upload.service';
import { Router } from '@angular/router'; // Import Router
import { DataTableListComponent } from '../data-table/data-table-list/data-table-list.component';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css'] // Corrected from 'styleUrl' to 'styleUrls'
})
export class FileUploadComponent {
  selectedFile: File | null = null;
  description: string = '';

  constructor(private uploadService: UploadService, private router: Router) {} // Inject Router

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
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile, this.selectedFile.name);
      formData.append('description', this.description);
      this.uploadService.uploadFile(formData).subscribe({
        next: (response) => {
          console.log(response);
          alert('File uploaded successfully!'); // Success message
          this.router.navigate([DataTableListComponent]); // Redirect to DataTableList
        },
        error: (error) => {
          console.error('Upload failed', error);
          alert('Upload failed'); // Display error message
        }
      });
    } else {
      console.error('No file selected');
    }
  }
}
