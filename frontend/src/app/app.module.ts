import { ArticleDetailsService } from './services/article-details.service';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { NguiInviewModule } from '@ngui/common';

import { AppComponent } from './app.component';
import { ArticleComponent } from './article/article.component';

@NgModule({
  declarations: [
    AppComponent,
    ArticleComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NguiInviewModule,
  ],
  providers: [
    ArticleDetailsService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
