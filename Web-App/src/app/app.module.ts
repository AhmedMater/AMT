import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {LocationStrategy, HashLocationStrategy, CommonModule} from '@angular/common';
import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {TabsModule} from 'ngx-bootstrap/tabs';
import {ChartsModule} from 'ng2-charts/ng2-charts';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

import {AppComponent} from './app.component';
import {AppRoutingModule} from './app.routing';

import {FullLayout, SimpleLayout} from './containers';
import {AsideToggleDirective, NAV_DROPDOWN_DIRECTIVES, SIDEBAR_TOGGLE_DIRECTIVES} from './directives';
import {AppAside, AppBreadcrumbs, AppFooter, AppHeader, AppSidebar} from './components';
import {NewCourseComponent} from "./amt/course/new/new-course.component";
import {CourseDetailComponent} from "./amt/course/details/course-details.component";
import {CourseListComponent} from "./amt/course/list/course-list.component";
import {HttpModule} from "@angular/http";
import {ReactiveFormsModule} from "@angular/forms";
import {ToastModule} from "ng2-toastr";

const APP_COMPONENTS = [AppAside, AppBreadcrumbs, AppFooter, AppHeader, AppSidebar];
const APP_CONTAINERS = [FullLayout, SimpleLayout];//, NewCourseComponent, CourseDetailComponent, CourseListComponent];
const APP_DIRECTIVES = [AsideToggleDirective, NAV_DROPDOWN_DIRECTIVES, SIDEBAR_TOGGLE_DIRECTIVES];


@NgModule({
    imports: [BrowserModule, BrowserAnimationsModule, AppRoutingModule, BsDropdownModule.forRoot(),
        TabsModule.forRoot(), ChartsModule, ToastModule.forRoot(), ReactiveFormsModule, HttpModule],
    declarations: [AppComponent, APP_CONTAINERS, APP_COMPONENTS, APP_DIRECTIVES],
    providers: [{provide: LocationStrategy, useClass: HashLocationStrategy}],
    bootstrap: [AppComponent]
})
export class AppModule {
}
