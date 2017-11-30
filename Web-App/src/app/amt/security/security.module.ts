///<reference path="../services/RESTClient.ts"/>
import { NgModule } from '@angular/core';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

import {ReactiveFormsModule, FormBuilder} from "@angular/forms";
import {HttpModule, Http} from "@angular/http";
import {BsDropdownModule} from "ngx-bootstrap";
import {ChartsModule} from "ng2-charts";
import {DashboardRoutingModule} from "../../views/dashboard/dashboard-routing.module";
import {RESTClient} from "../services/RESTClient";
import {UserService} from "../services/UserService";
import {ToastModule} from "ng2-toastr";
import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
import {BrowserModule} from "@angular/platform-browser";
import {CommonModule} from "@angular/common";
import {ProfileComponent} from "../user/profile/profile.component";
import {Routes, RouterModule} from "@angular/router";

const routes: Routes = [{
    path: '',
    children: [
        {path: 'login', component: LoginComponent},
        {path: 'register', component: RegisterComponent}
    ]
}];

@NgModule({
    imports: [RouterModule.forChild(routes), CommonModule,
        ChartsModule, ToastModule.forRoot(),
        BsDropdownModule,  ReactiveFormsModule, HttpModule
    ],
  declarations: [LoginComponent, RegisterComponent]
})
export class SecurityModule { }
