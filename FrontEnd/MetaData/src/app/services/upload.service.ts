import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { EditDataTableDialogComponent } from '../edit-data-table-dialog/edit-data-table-dialog.component';
import { DataTable, Schema } from '../models/data-table';

@Injectable({
  providedIn: 'root'
})
export class UploadService {

  private baseUrl = 'http://localhost:8075/api';
  private baseUrl2= 'http://localhost:8075/api/tables';

  constructor(private http: HttpClient,public dialog: MatDialog) {}

  uploadFile(formData: FormData) {
    return this.http.post(`${this.baseUrl}/upload`, formData);
  }
  getDataTables(): Observable<DataTable[]> {
    return this.http.get<DataTable[]>(`${this.baseUrl2}`);
  }
  updateDataTable(dataTable: DataTable): Observable<any> {
    return this.http.put(`${this.baseUrl2}/${dataTable.idTable}`, dataTable);
  }
  
  
  getSchemasForTable(tableId: number): Observable<Schema[]> {
    return this.http.get<Schema[]>(`${this.baseUrl2}/${tableId}/schemas`);
  }
  updateSchema(schema: Schema): Observable<any> {
    return this.http.put(`${this.baseUrl}/schemas/${schema.idSchema}`, schema);
  }
  updateTags(idSchema: number, tags: string[]): Observable<any> {
    return this.http.put(`${this.baseUrl}/schemas/${idSchema}/tags`, tags);
  }
  
}
