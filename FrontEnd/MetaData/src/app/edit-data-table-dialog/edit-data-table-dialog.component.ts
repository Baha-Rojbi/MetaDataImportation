import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DataTable } from '../models/data-table';
@Component({
  selector: 'app-edit-data-table-dialog',
  templateUrl: './edit-data-table-dialog.component.html',
  styleUrl: './edit-data-table-dialog.component.css'
})
export class EditDataTableDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<EditDataTableDialogComponent>, // Inject MatDialogRef
    @Inject(MAT_DIALOG_DATA) public data: DataTable
  ) {}

  // Method to close the dialog
  onCancelClick(): void {
    this.dialogRef.close();
  }
  // Add a save method that closes the dialog and returns the updated data
save(): void {
  this.dialogRef.close(this.data);
}


}
