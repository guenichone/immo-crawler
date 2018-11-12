import { AuthGuard } from './guards/auth.guard';
import { ArticleService } from './services/article.service';
import { AuthService } from './services/auth.service';

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { NguiInviewModule } from '@ngui/common';
import { MatTableModule } from '@angular/material/table';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSortModule } from '@angular/material/sort';
import { ArticleComponent } from './article/article.component';
import { LoginComponent } from './login/login.component';
import { RouterModule, Routes } from '@angular/router';
import { AuthInterceptor } from './interceptors/auth.interceptor';
import { SpinnerInterceptor } from './interceptors/spinner.interceptor';
import { ArticleListComponent } from './article-list/article-list.component';
import { NgxSpinnerModule } from 'ngx-spinner';

const appRoutes: Routes = [
  { path: '', redirectTo: 'article', pathMatch: 'full'},
  { path: 'login', component: LoginComponent },
  // { path: 'register', component: RegisterComponent },
  { path: 'article', component: ArticleListComponent, canActivate: [AuthGuard] },
  { path: '**', redirectTo: 'article' },
];


@NgModule({
  declarations: [
    AppComponent,
    ArticleComponent,
    LoginComponent,
    ArticleListComponent,
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    ),
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    NguiInviewModule,
    MatTableModule,
    MatSortModule,
    MatButtonModule,
    ReactiveFormsModule,
    RouterModule,
    NgxSpinnerModule,
  ],
  providers: [
    AuthService,
    ArticleService,
    AuthGuard,
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: SpinnerInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
