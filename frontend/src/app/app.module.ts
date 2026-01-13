import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { routes } from './app.routes';

/**
 * Módulo raíz de la aplicación Angular.
 */
@NgModule({
  imports: [BrowserModule, BrowserAnimationsModule, HttpClientModule, RouterModule.forRoot(routes), AppComponent],
  bootstrap: [AppComponent]
})
export class AppModule {}
