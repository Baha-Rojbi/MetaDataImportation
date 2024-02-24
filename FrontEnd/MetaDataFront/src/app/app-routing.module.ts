import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { TableListComponent } from './table-list/table-list.component';
import { FileUploadComponent } from './file-upload/file-upload.component';
import { TableDetailComponent } from './table-detail/table-detail.component';

const routes: Routes = [
  { path: 'upload', component: FileUploadComponent },
  { path: '', redirectTo: '/tables', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
