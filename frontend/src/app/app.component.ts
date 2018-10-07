import { Article } from './models/article.model';
import { ArticleService } from './services/article.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  displayedColumns: string[] = ['url', 'price', 'city', 'land', 'home'];
  dataSource: MatTableDataSource;

  @ViewChild(MatSort) sort: MatSort;

  public constructor(private articleService: ArticleService) {
    articleService.getArticleByLandSurfaceGreaterThan(7).subscribe(results => {
      this.dataSource = new MatTableDataSource(results);
      this.dataSource.sort = this.sort;
    });
  }
}
