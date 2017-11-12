///<reference path="../services/RESTClient.ts"/>
import { NgModule } from '@angular/core';

import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

import { SecurityRoutingModule } from './security-routing.module';
import {ReactiveFormsModule, FormBuilder} from "@angular/forms";
import {HttpModule, Http} from "@angular/http";
import {BsDropdownModule} from "ngx-bootstrap";
import {ChartsModule} from "ng2-charts";
import {DashboardRoutingModule} from "../../views/dashboard/dashboard-routing.module";
import {RESTClient} from "../services/RESTClient";
import {UserService} from "../services/UserService";
import {ToastModule} from "ng2-toastr";

@NgModule({
    imports: [SecurityRoutingModule,
        ChartsModule, ToastModule.forRoot(),
        BsDropdownModule,  ReactiveFormsModule, HttpModule
    ],
  declarations: [LoginComponent, RegisterComponent]
})
export class SecurityModule { }
