import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { DataService } from '../data.service';

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrl: './table-list.component.css'
})
export class TableListComponent implements OnInit{
  tables: any[] = [];
  @Output() selectTable = new EventEmitter<any>();

  constructor(private dataService: DataService) { }

  ngOnInit(): void {
    this.dataService.getTables().subscribe(data => {
      this.tables = data;
    });
  }

  onSelectTable(table: any): void {
    this.selectTable.emit(table); // Make sure this line is executed when a table is clicked
  }

}
