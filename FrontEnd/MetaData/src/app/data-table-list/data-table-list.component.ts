import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { UploadService } from '../services/upload.service';
import { DataTable } from '../models/data-table';
import { EditDataTableDialogComponent } from '../edit-data-table-dialog/edit-data-table-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';


@Component({
  selector: 'app-data-table-list',
  templateUrl: './data-table-list.component.html',
  styleUrl: './data-table-list.component.css'
})
export class DataTableListComponent implements OnInit {
  dataTables: DataTable[] = [];
 


  constructor(private dataService: UploadService, public dialog: MatDialog, private router: Router) {}

  ngOnInit(): void {
    this.fetchDataTables();
  }

  fetchDataTables(): void {
    this.dataService.getDataTables().subscribe(data => {
      this.dataTables = data;
    });
  }

  openEditForm(dataTable: DataTable): void {
    const dialogRef = this.dialog.open(EditDataTableDialogComponent, {
      width: '250px',
      data: dataTable
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateDataTable(result);
      }
    });
  }

  updateDataTable(updatedDataTable: DataTable): void {
    this.dataService.updateDataTable(updatedDataTable).subscribe({
      next: (response) => {
        console.log("Update successful", response);
        this.fetchDataTables(); // Refresh the list to show the updated data
      },
      error: (error) => {
        console.error("Update failed", error);
      }
    });
  }
  openSchemas(dataTable: DataTable): void {
    // Example: Open a new route with the ID of the DataTable
    // Make sure to configure routing in your Angular app to handle this route
    this.router.navigate(['/schemas', dataTable.idTable]);
  }
 
  
}