import {Component, OnInit} from '@angular/core';
import {ConfigParam} from "../../amt/util/constants/ConfigParam";
import {AuthenticationService} from "../../amt/services/AuthenticationService";
import {FullRoutes} from "../../amt/util/constants/FullRoutes";
import {Lookups} from "../../amt/util/constants/Lookups";

@Component({
  selector: 'app-header',
  templateUrl: './app-header.component.html',
    providers:[AuthenticationService]
})
export class AppHeader implements OnInit{

    fullName:string;
    isLoggedIn:boolean;
    userID: number;

    //Roles
    isAdmin: boolean;
    isTutor: boolean;

    HOME_URL: string = FullRoutes.HOME_URL;
    LOGIN_URL: string = FullRoutes.LOGIN_URL;
    REGISTER_URL: string = FullRoutes.REGISTER_URL;
    USER_PROFILE_URL: string = FullRoutes.USER_PROFILE_URL;

    NEW_COURSE_URL: string = FullRoutes.NEW_COURSE_URL;
    COURSE_LIST_URL: string = FullRoutes.COURSE_LIST_URL;

  // public disabled = false;
  // public status: {isopen: boolean} = {isopen: false};
  //
  // public toggled(open: boolean): void {
  //   console.log('Dropdown is now: ', open);
  // }
  //
  // public toggleDropdown($event: MouseEvent): void {
  //   $event.preventDefault();
  //   $event.stopPropagation();
  //   this.status.isopen = !this.status.isopen;
  // }

    constructor(
        private authService: AuthenticationService){
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

  logout(){
      this.authService.logOutUser();
      this.isLoggedIn = false;
      this.fullName = null;
      this.userID = null;
  }
}
