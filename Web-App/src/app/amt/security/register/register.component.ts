import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {UserService} from "../../services/UserService";
import {RESTClient} from "../../services/RESTClient";
import {Http} from "@angular/http";
import {FormGroup, FormBuilder} from "@angular/forms";
import {UserRegisterData} from "../../util/dto/user/UserRegisterData";
import {ToastsManager, Toast} from "ng2-toastr";
import {FullRoutes} from "../../util/constants/FullRoutes";
import {FormValidation} from "../../util/vto/error/FormValidation";
import {HttpErrorResponse} from "@angular/common/http";
import {Router} from "@angular/router";
import {AMError} from "../../util/vto/error/AMError";
import {ConfigUtils} from "../../util/generic/ConfigUtils";

@Component({
    selector: 'register',
  templateUrl: 'register.component.html',
    providers: [RESTClient, UserService, FormBuilder]
})
export class RegisterComponent implements OnInit{
    registerForm: FormGroup;
    userRegisterData: UserRegisterData;

    amError: AMError;
    formInvalid: boolean;

    HOME_URL: string = FullRoutes.HOME_URL;
    LOGIN_URL: string = FullRoutes.LOGIN_URL;
    TOASTR_TITLE :string = "Register New User";

    constructor(
        private userService: UserService,
        private formBuilder: FormBuilder,
        public toastr: ToastsManager,
        private router: Router,
        private vcr: ViewContainerRef
    ) {
        this.toastr.setRootViewContainerRef(vcr);
    }

    ngOnInit(): void {
        this.registerForm = this.formBuilder.group({
            firstName: '',
            lastName: '',
            username: '',
            passwordData: this.formBuilder.group({
                password: '',
                confirmPassword: ''
            }),
            email: ''
        });
        this.formInvalid = false;
    }

    register(){
        this.userRegisterData = new UserRegisterData();
        this.userRegisterData.firstName = this.registerForm.value.firstName;
        this.userRegisterData.lastName = this.registerForm.value.lastName;
        this.userRegisterData.username = this.registerForm.value.username;
        this.userRegisterData.password = this.registerForm.value.passwordData.password;
        this.userRegisterData.email = this.registerForm.value.email;

        this.formInvalid = false;

        this.userService.register(this.userRegisterData).subscribe(
            res => {
                this.toastr.success("User Registration is successfully done.", this.TOASTR_TITLE);
                this.router.navigate([this.LOGIN_URL]);
            },
            err => {
                console.log(err);
                this.amError = err.error;
                this.formInvalid = ConfigUtils.handleError(err, this.toastr, this.TOASTR_TITLE);
            }
        );
    }

    clear(){
        this.registerForm = this.formBuilder.group({
            firstName: '',
            lastName: '',
            username: '',
            passwordData: this.formBuilder.group({
                password: '',
                confirmPassword: ''
            }),
            email: ''
        });
    }

}
