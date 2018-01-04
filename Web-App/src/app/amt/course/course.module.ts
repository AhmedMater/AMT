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
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {NgxPaginationModule} from "ngx-pagination";
import {SortableTableDirective} from "../util/components.sorting/sortable-table";
import {SortableColumnComponent} from "../util/components.sorting/sortable-column.component";
import {StringToDate} from "../util/pipe/StringToDate";
import {MyDatePickerModule} from "mydatepicker";
import {AMTPipes} from "../util/pipe/AMTPipes";

@NgModule({
    imports: [CourseRoutingModule, CommonModule, NgxPaginationModule, AMTPipes,
        ToastModule.forRoot(), ReactiveFormsModule, HttpModule, MyDatePickerModule
    ],
    declarations: [NewCourseComponent, CourseListComponent, CourseDetailComponent,
        SortableColumnComponent, SortableTableDirective]
    // providers: [{provide: HTTP_INTERCEPTORS, useClass: RESTInterceptor, multi: true}]
})
export class CourseModule { }