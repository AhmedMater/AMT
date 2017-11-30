/**
 * Created by ahmed.motair on 11/27/2017.
 */

import {FormBuilder, FormGroup} from "@angular/forms";
import {Component, OnInit, ViewContainerRef} from "@angular/core";
import {UserService} from "../../services/UserService";
import {RESTClient} from "../../services/RESTClient";
import {UserRegisterData} from "../../util/dto/user/UserRegisterData";
import {FormValidation} from "../../util/vto/error/FormValidation";
import {FullRoutes} from "../../util/constants/FullRoutes";
import {ToastsManager} from "ng2-toastr";
import {Router, ActivatedRoute} from "@angular/router";
import {ChangeRoleData} from "../../util/dto/user/ChangeRoleData";
import {LoginData} from "../../util/dto/user/LoginData";
import {AuthenticationService} from "../../services/AuthenticationService";
import {UserProfileData} from "../../util/vto/user/UserProfileData";
import {Role} from "../../util/dto/lookup/Role";

@Component({
    selector: 'profile',
    templateUrl: 'profile.component.html',
    providers: [RESTClient, UserService, FormBuilder, AuthenticationService]
})
export class ProfileComponent implements OnInit{
    userID: number;
    userProfileData: UserProfileData = new UserProfileData();

    editRole: boolean = false;
    changeRoleForm: FormGroup;

    // HOME_URL: string = FullRoutes.HOME_URL;
    // LOGIN_URL: string = FullRoutes.LOGIN_URL;
    TOASTR_TITLE :string = "User Profile";

    constructor(
        private userService: UserService, private formBuilder: FormBuilder,
        public toastr: ToastsManager, private router: Router,
        private vcr: ViewContainerRef, private route: ActivatedRoute,
        private authService: AuthenticationService
    ) {
        this.toastr.setRootViewContainerRef(vcr);
        console.log(this.userProfileData);
    }

    ngOnInit(): void {
        this.route.params.subscribe(params => {
            this.userID = +params['userID'];
            this.userService.getUserProfileData(this.userID).subscribe(
                res => {this.userProfileData = res; console.log(res)},
                err => {console.log(err);}
            );
        });


        this.changeRoleForm = this.formBuilder.group({newRole: ''});
    }

    changeRole(){
        this.editRole = !this.editRole;
    }

    editUserData(){

    }
    submitNewRole(){
        this.userService.changeUserRole(this.userID, this.changeRoleForm.value.newRole).subscribe(
            res => {
                this.toastr.success("Role is changed successfully", this.TOASTR_TITLE);
                for(let item of this.userProfileData.roleList)
                    if(item.role == this.changeRoleForm.value.newRole)
                        this.userProfileData.role = item.description;
            },
            err => {console.log(err);}
        );
    }
}