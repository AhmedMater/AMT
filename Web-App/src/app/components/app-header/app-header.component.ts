import {Component, OnInit} from '@angular/core';
import {ConfigParam} from "../../amt/util/constants/ConfigParam";
import {AuthenticationService} from "../../amt/services/AuthenticationService";
import {FullRoutes} from "../../amt/util/constants/FullRoutes";
import {Roles} from "../../amt/util/constants/lookups/Roles";

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
    isOwner: boolean;
    isTutor: boolean;

    FULL_ROUTES = FullRoutes;

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
          this.isAdmin = role == Roles.ADMIN.role;
          this.isOwner = role == Roles.OWNER.role;
          this.isTutor = role == Roles.TUTOR.role;
      }
  }

  logout(){
      this.authService.logOutUser();
      this.isLoggedIn = false;
      this.fullName = null;
      this.userID = null;
  }
}
