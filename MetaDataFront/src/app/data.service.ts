import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataService {

  private apiUrl = 'http://localhost:8075/api'; // Replace with your actual API URL

  constructor(private http: HttpClient) { }

  getTables(): Observable<any> {
    return this.http.get(`${this.apiUrl}/tables`);
  }

  getTableDetails(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/tables/${id}`);
  }
}
