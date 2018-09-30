import { ArticleDetails } from './../models/article-details.model';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Injectable } from '@angular/core';


@Injectable()
export class ArticleDetailsService {

    constructor(private httpClient: HttpClient) {
    }

    public getTop20ArticleDetails(): Observable<ArticleDetails[]> {
        return this.httpClient.get<ArticleDetails[]>('/details/top20');
    }

    public getArticleDetailsBySurfaceGreaterThan(surface: number): Observable<ArticleDetails[]> {
        return this.httpClient.get<ArticleDetails[]>('/details/surfaceGreaterThan/' + surface);
    }
}
