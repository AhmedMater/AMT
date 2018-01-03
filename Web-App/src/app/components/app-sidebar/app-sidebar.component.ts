import {Component, OnInit} from '@angular/core';
import {FullRoutes} from "../../amt/util/constants/FullRoutes";
import {AuthenticationService} from "../../amt/services/AuthenticationService";
import {Roles} from "../../amt/util/constants/lookups/Roles";

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
    isOwner: boolean;
    isTutor: boolean;

    FULL_ROUTES = FullRoutes;

    constructor(private authService: AuthenticationService){
    }

    ngOnInit(): void {
        this.isLoggedIn = this.authService.isUserLoginIn();
        if(this.isLoggedIn) {
            this.fullName = this.authService.getUserFullName();
            this.userID = this.authService.getUserID();

            let role: string = this.authService.getUserRole();
            this.isAdmin = role == Roles.ADMIN.role;
            this.isOwner = role == Roles.OWNER.role;
            this.isTutor = role == Roles.TUTOR.role;
        }
    }
}
