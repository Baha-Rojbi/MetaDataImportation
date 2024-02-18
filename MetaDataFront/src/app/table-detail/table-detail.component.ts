import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-table-detail',
  templateUrl: './table-detail.component.html',
  styleUrl: './table-detail.component.css'
})
export class TableDetailComponent {
  @Input() table: any;
}
