import { ArticleDetails } from './models/article-details.model';
import { ArticleDetailsService } from './services/article-details.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  details: ArticleDetails[];

  public constructor(private articleDetailsService: ArticleDetailsService) {
    articleDetailsService.getArticleDetailsBySurfaceGreaterThan(10).subscribe(details => this.details = details);
  }
}
