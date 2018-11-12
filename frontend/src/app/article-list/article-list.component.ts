import { Component, ViewChild, OnInit, ChangeDetectorRef } from '@angular/core';
import { MatTableDataSource, MatSort } from '@angular/material';
import { Article } from '../models/article.model';
import { ArticleService } from '../services/article.service';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent implements OnInit{
  
  displayedColumns: string[] = ['url', 'price', 'city', 'land', 'home', 'sold'];
  dataSource: MatTableDataSource<Article>;

  @ViewChild(MatSort) sort: MatSort;

  public constructor(private articleService: ArticleService, private changeDetectorRefs: ChangeDetectorRef) {
  }

  ngOnInit(): void {
    this.articleService.getArticleByLandSurfaceGreaterThan(7).subscribe(results => {
      this.dataSource = new MatTableDataSource<Article>(results);
      this.dataSource.sort = this.sort;
    });
  }

  setAsSold(article: Article) {
    this.articleService.setArticleAsSold(article.provider, article.id).subscribe(result => {
      const idx = this.dataSource.data.indexOf(article);

      this.dataSource.data.splice(idx, 1);

      this.dataSource = new MatTableDataSource<Article>(this.dataSource.data);
      this.dataSource.sort = this.sort;
    });
  }
}
