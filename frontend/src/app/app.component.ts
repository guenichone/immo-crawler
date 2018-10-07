import { Article } from './models/article.model';
import { ArticleService } from './services/article.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  results: Article[];

  public constructor(private articleService: ArticleService) {
    articleService.getArticleByLandSurfaceGreaterThan(7).subscribe(results => this.results = results);
  }
}
