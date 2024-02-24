import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-table-detail',
  templateUrl: './table-detail.component.html',
  styleUrl: './table-detail.component.css'
})
export class TableDetailComponent implements OnInit {
  @Input() table: any;
  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    const tableId = this.route.snapshot.paramMap.get('id');
    // Fetch the table details using the tableId
  }
}
