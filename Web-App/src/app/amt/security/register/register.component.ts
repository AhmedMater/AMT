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

@Component({
    selector: 'register',
  templateUrl: 'register.component.html',
    providers: [RESTClient, UserService, FormBuilder]
})
export class RegisterComponent implements OnInit{
    registerForm: FormGroup;
    userRegisterData: UserRegisterData;

    registerError: AMError;
    formInvalid: boolean;
    // formValidationErrors: FormValidation;

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
        // this.formValidationErrors = new FormValidation();
    }

    register(){
        this.userRegisterData = new UserRegisterData();
        this.userRegisterData.firstName = this.registerForm.value.firstName;
        this.userRegisterData.lastName = this.registerForm.value.lastName;
        this.userRegisterData.username = this.registerForm.value.username;
        this.userRegisterData.password = this.registerForm.value.passwordData.password;
        this.userRegisterData.email = this.registerForm.value.email;

        this.formInvalid = false;
        // this.formValidationErrors = new FormValidation();

        this.userService.register(this.userRegisterData).subscribe(
            res => {
                this.toastr.success("User Registration is successfully done.", this.TOASTR_TITLE);
                this.router.navigate([this.LOGIN_URL]);
            },
            err => {
                this.registerError = err.error;

                if(this.registerError.validation != null)
                    this.formInvalid = true;
                else
                    this.toastr.error(this.registerError.message, this.TOASTR_TITLE);
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
