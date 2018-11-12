import { Article } from './../models/article.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';


@Injectable()
export class ArticleService {

    constructor(private httpClient: HttpClient) {
    }

    public getTop20Article(): Observable<Article[]> {
        return this.httpClient.get<Article[]>('/articles/details/top20');
    }

    public getArticleByLandSurfaceGreaterThan(surface: number): Observable<Article[]> {
        return this.httpClient.get<Article[]>('/search/landSurfaceGreaterThan/' + surface);
    }

    public setArticleAsSold(provider: string, articleId: string): Observable<Article> {
        return this.httpClient.get<Article>('/articles/' + provider + '/' + articleId + '/sold');
    }
}
