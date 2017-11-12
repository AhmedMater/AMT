/**
 * Created by ahmed.motair on 11/4/2017.
 */
import { NgModule } from '@angular/core';
import {ReactiveFormsModule, FormBuilder} from "@angular/forms";
import {HttpModule, Http} from "@angular/http";
import {BsDropdownModule, ModalModule} from "ngx-bootstrap";
import {ChartsModule} from "ng2-charts";
import {ToastModule} from "ng2-toastr";

import {RESTClient} from "../services/RESTClient";
import {CourseRoutingModule} from "./course-routing.module";
import {NewCourseComponent} from "./new/new-course.component";
import {CourseListComponent} from "./list/course-list.component";
import {CourseDetailComponent} from "./details/course-details.component";
import {CommonModule} from "@angular/common";

@NgModule({
    imports: [CourseRoutingModule, CommonModule,
        ToastModule.forRoot(), ReactiveFormsModule, HttpModule
    ],
    declarations: [NewCourseComponent, CourseListComponent, CourseDetailComponent]
})
export class CourseModule { }