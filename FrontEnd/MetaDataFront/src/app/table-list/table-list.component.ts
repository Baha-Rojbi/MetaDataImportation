import { Component, EventEmitter, OnInit, Output } from '@angular/core';

import { DataService } from '../data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-table-list',
  templateUrl: './table-list.component.html',
  styleUrl: './table-list.component.css'
})
export class TableListComponent implements OnInit{
  tables: any[] = [];
  @Output() selectTable = new EventEmitter<any>();
  selectedTable: any;

  constructor(private dataService: DataService,private router: Router) { }

  ngOnInit(): void {
    this.dataService.getTables().subscribe(data => {
      this.tables = data;
    });
  }

  onSelectTable(table: any): void {
    this.selectTable.emit(table); // Make sure this line is executed when a table is clicked
  }

}
