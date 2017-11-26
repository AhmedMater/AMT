/**
 * Created by ahmed.motair on 11/4/2017.
 */
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {NewCourseComponent} from "./new/new-course.component";
import {CourseDetailComponent} from "./details/course-details.component";
import {CourseListComponent} from "./list/course-list.component";


const routes: Routes = [
    {path: '',
        children: [
            {path: 'new', component: NewCourseComponent, data: {title: 'New Course'}},
            {path: 'list', component: CourseListComponent, data: {title: 'Course List'}},
            {path: ':courseID', component: CourseDetailComponent, data: {title: 'Course Details'}}
        ]
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class CourseRoutingModule {}
