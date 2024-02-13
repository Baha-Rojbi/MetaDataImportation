import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FileService {
  private baseUrl = 'http://localhost:8075/api/upload/'; // Ajustez selon votre configuration

  constructor(private http: HttpClient) { }

  uploadFile(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(this.baseUrl, formData);
  }
  getFiles(): Observable<any> {
    return this.http.get(`http://localhost:8075/files/all`);
}
}

