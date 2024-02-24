import { NgModule, CUSTOM_ELEMENTS_SCHEMA  } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import {  HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { DataTableListComponent } from './data-table-list/data-table-list.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { EditDataTableDialogComponent } from './edit-data-table-dialog/edit-data-table-dialog.component';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { SchemasComponent } from './schemas/schemas.component';
import { EditSchemaDialogComponent } from './edit-schema-dialog/edit-schema-dialog.component';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';




@NgModule({
  
  declarations: [
    AppComponent,
    FileUploadComponent,
    DataTableListComponent,
    EditDataTableDialogComponent,
    SchemasComponent,
    EditSchemaDialogComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    MatDialogModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatChipsModule,
    MatIconModule

  ],
  providers: [
    provideClientHydration(),
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
  
})
export class AppModule { }
