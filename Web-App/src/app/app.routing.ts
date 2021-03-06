import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';

// Import Containers
import {FullLayout, SimpleLayout} from './containers';
import {NewCourseComponent} from "./amt/course/new/new-course.component";
import {CourseListComponent} from "./amt/course/list/course-list.component";
import {CourseDetailComponent} from "./amt/course/details/course-details.component";
import {P404Component} from "./views/pages/404.component";

export const routes: Routes = [
    {path: '', redirectTo: 'login', pathMatch: 'full'},
    {path: '', component: SimpleLayout,
        children: [
            {path: '', loadChildren: './amt/security/security.module#SecurityModule'}
        ]
    },
    {path: '', component: FullLayout, data: {title: 'Home'},
        children: [
            {path: 'course', loadChildren: './amt/course/course.module#CourseModule'},
            {path: 'user', loadChildren: './amt/user/user.module#UserModule'},

            {path: 'home', loadChildren: './views/dashboard/dashboard.module#DashboardModule'},
            {path: 'components', loadChildren: './views/components/components.module#ComponentsModule'},
            {path: 'icons', loadChildren: './views/icons/icons.module#IconsModule'},
            {path: 'widgets', loadChildren: './views/widgets/widgets.module#WidgetsModule'},
            {path: 'charts', loadChildren: './views/chartjs/chartjs.module#ChartJSModule'}
        ]
    },
    { path: '**', component: P404Component }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
