
import { NgModule } from '@angular/core';
import {ReactiveFormsModule, FormBuilder} from "@angular/forms";
import {HttpModule, Http} from "@angular/http";
import {BsDropdownModule} from "ngx-bootstrap";
import {ChartsModule} from "ng2-charts";
import {ToastModule} from "ng2-toastr";
import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
import {BrowserModule} from "@angular/platform-browser";
import {CommonModule} from "@angular/common";
import {Routes, RouterModule} from "@angular/router";
import {ProfileComponent} from "./profile/profile.component";
import {UserListComponent} from "./list/user-list.component";

const routes: Routes = [
    {
        path: '',
        children: [
            {path: 'profile/:userID', component: ProfileComponent, data:{title:'User Profile'}},
            {path: 'list', component: UserListComponent, data:{title:'Users List'}}
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes), CommonModule,
        ChartsModule, ToastModule.forRoot(),
        BsDropdownModule,  ReactiveFormsModule, HttpModule
    ],
    declarations: [ProfileComponent, UserListComponent]
})
export class UserModule { }
