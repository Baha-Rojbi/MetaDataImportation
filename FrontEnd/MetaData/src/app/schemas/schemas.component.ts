import { Component, OnInit } from '@angular/core';
import { UploadService } from '../services/upload.service';
import { ActivatedRoute } from '@angular/router';
import { Schema } from '../models/data-table';
import { MatIconRegistry } from '@angular/material/icon';
import { DomSanitizer } from '@angular/platform-browser';
import { MatDialog } from '@angular/material/dialog';
import { EditSchemaDialogComponent } from '../edit-schema-dialog/edit-schema-dialog.component';

@Component({
  selector: 'app-schemas',
  templateUrl: './schemas.component.html',
  styleUrl: './schemas.component.css'
})
export class SchemasComponent implements OnInit {
  schemas: Schema[] = [];
  newTag: string = '';
  showEditIcon: boolean[] = [];
  editMode: boolean[] = [];
 
  constructor(
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private dataTableService: UploadService,
    private iconRegistry: MatIconRegistry, private sanitizer: DomSanitizer
  ) { this.iconRegistry.addSvgIconSet(
    'material-symbols',
    this.sanitizer.bypassSecurityTrustResourceUrl('https://fonts.googleapis.com/icons?family=Material+Symbols')
  );}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const tableId = +params['id']; // '+' converts the string 'id' to a number
      this.dataTableService.getSchemasForTable(tableId).subscribe(
        data => this.schemas = data,
        error => console.error('There was an error retrieving the schemas', error)
      );
    });
  }
  openEditDialog(schema: Schema): void {
    const dialogRef = this.dialog.open(EditSchemaDialogComponent, {
      width: '250px',
      data: {description: schema.description}
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        schema.description = result;
        this.dataTableService.updateSchema(schema).subscribe(
          success => console.log('Update successful'),
          error => console.error('Error updating schema', error)
        );
      }
    });
  }
  addTag(schema: Schema, idx: number): void {
    if (!schema.tags) {
      schema.tags = []; // Initialize tags array if it's undefined
      this.editMode[idx] = false;
    }

    if (this.newTag) {
      schema.tags.push(this.newTag);
      this.updateTags(schema);
      this.newTag = ''; // Clear the input after adding
    }
  }

  removeTag(schema: Schema, tagIndex: number): void {
    if (schema.tags) {
      schema.tags.splice(tagIndex, 1);
      this.updateTags(schema);
    }
  }

  updateTags(schema: Schema): void {
    // Use 'schema.tags || []' to provide an empty array if tags are undefined
    this.dataTableService.updateTags(schema.idSchema, schema.tags || []).subscribe(
      success => console.log('Tags updated successfully'),
      error => console.error('Error updating tags', error)
    );
  }

  cancelEdit(): void {
    // Handle cancelation logic here, if necessary
  }
  

}
