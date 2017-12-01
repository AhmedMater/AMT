import {Component, OnInit} from '@angular/core';
import {FullRoutes} from "../../amt/util/constants/FullRoutes";
import {AuthenticationService} from "../../amt/services/AuthenticationService";
import {Lookups} from "../../amt/util/constants/Lookups";

@Component({
  selector: 'app-sidebar',
  templateUrl: './app-sidebar.component.html',
    providers:[AuthenticationService]
})
export class AppSidebar implements OnInit{
    fullName:string;
    isLoggedIn:boolean;
    userID: number;

    //Roles
    isAdmin: boolean;
    isTutor: boolean;

    NEW_COURSE_URL: string = FullRoutes.NEW_COURSE_URL;
    COURSE_LIST_URL: string = FullRoutes.COURSE_LIST_URL;
    HOME_URL: string = FullRoutes.HOME_URL;
    USER_LIST_URL: string = FullRoutes.USER_LIST_URL;

    constructor(private authService: AuthenticationService){
    }

    ngOnInit(): void {
        this.isLoggedIn = this.authService.isUserLoginIn();
        if(this.isLoggedIn) {
            this.fullName = this.authService.getUserFullName();
            this.userID = this.authService.getUserID();

            let role: string = this.authService.getUserRole();
            this.isAdmin = role == Lookups.ADMIN_ROLE;
            this.isTutor = role == Lookups.TUTOR_ROLE;
        }
    }
}
