import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserListComponent } from './components/user-list/user-list.component';
import { JwtInterceptor } from './interceptors/jwt.interceptor';

// Ng-Aquila Modules
import { NxTableModule } from '@aposin/ng-aquila/table';
import { NxIconModule } from '@aposin/ng-aquila/icon';
import { NxSpinnerModule } from '@aposin/ng-aquila/spinner';
import { NxMessageModule } from '@aposin/ng-aquila/message';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    UserListComponent,
    NxTableModule,
    NxIconModule,
    NxSpinnerModule,
    NxMessageModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }