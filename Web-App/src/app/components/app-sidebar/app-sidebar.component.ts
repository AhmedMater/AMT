import {Component, OnInit} from '@angular/core';
import {FullRoutes} from "../../amt/util/constants/FullRoutes";

@Component({
  selector: 'app-sidebar',
  templateUrl: './app-sidebar.component.html'
})
export class AppSidebar implements OnInit{

    NEW_COURSE_URL: string = FullRoutes.NEW_COURSE_URL;
    COURSE_LIST_URL: string = FullRoutes.COURSE_LIST_URL;
    HOME_URL: string = FullRoutes.HOME_URL;

    constructor(){
    }

    ngOnInit(): void {}
}
