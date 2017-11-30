import {Component, ViewContainerRef, OnInit} from '@angular/core';
import {UserService} from "../../services/UserService";
import {RESTClient} from "../../services/RESTClient";
import {LoginData} from "../../util/dto/user/LoginData";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {FullRoutes} from "../../util/constants/FullRoutes";
import {Router} from "@angular/router";
import {ConfigParam} from "../../util/constants/ConfigParam";
import {AuthenticationService} from "../../services/AuthenticationService";
import {FormValidation} from "../../util/vto/error/FormValidation";
import {AMError} from "../../util/vto/error/AMError";

@Component({
    selector: 'app-login',
  templateUrl: 'login.component.html',
    providers: [RESTClient, UserService, FormBuilder, AuthenticationService],
})
export class LoginComponent implements OnInit{

    loginForm:FormGroup;
    loginData:LoginData;

    loginError: AMError;
    formInvalid: boolean;
    // formValidationErrors: FormValidation;

    HOME_URL: string = FullRoutes.HOME_URL;
    REGISTER_URL: string = FullRoutes.REGISTER_URL;
    TOASTR_TITLE :string = "Login";

    constructor(
        private userService: UserService,
        private formBuilder: FormBuilder,
        public toastr: ToastsManager,
        private vcr: ViewContainerRef,
        private router: Router,
        private authService: AuthenticationService
    ) {
        this.toastr.setRootViewContainerRef(vcr);
    }

    ngOnInit(): void {
        this.loginForm = this.formBuilder.group({
            username: '',
            password: ''
        });
    }

    login(){
        this.loginData = new LoginData();
        this.loginData.username = this.loginForm.value.username;
        this.loginData.password = this.loginForm.value.password;

        this.userService.login(this.loginData).subscribe(
            res => {
                // console.log(res);
                this.authService.setAuthenticationData(res);
                // console.log(ConfigParam.LOGGED_IN_USER);
                // console.log(ConfigParam.IS_LOGIN);
                this.toastr.success("User Login successfully", this.TOASTR_TITLE);
                this.router.navigate([this.HOME_URL]);
            },
            err => {

                this.loginError = err.error;

                if(this.loginError.validation != null)
                    this.formInvalid = true;
                else
                    this.toastr.error(this.loginError.message, this.TOASTR_TITLE);

                // this.formValidationErrors = err.error;
                //
                // if(this.formValidationErrors.mainError != null)
                //     this.formInvalid = true;
                // else
                //     this.toastr.error(this.formValidationErrors.message, this.TOASTR_TITLE);
            }
        );
    }
}
