import {Component, ViewContainerRef} from '@angular/core';
import {UserService} from "../../services/UserService";
import {RESTClient} from "../../services/RESTClient";
import {LoginData} from "../../util/dto/LoginData";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {FullRoutes} from "../../util/constants/FullRoutes";
import {Router} from "@angular/router";
import {ConfigParam} from "../../util/constants/ConfigParam";
import {AuthenticationService} from "../../services/AuthenticationService";

@Component({
    selector: 'app-login',
  templateUrl: 'login.component.html',
    providers: [RESTClient, UserService, FormBuilder, AuthenticationService],
})
export class LoginComponent {

    loginForm:FormGroup;
    loginData:LoginData;

    HOME_URL: string = FullRoutes.HOME_URL;
    REGISTER_URL: string = FullRoutes.REGISTER_URL;

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
                this.toastr.success("User Login successfully");
                this.router.navigate([this.HOME_URL]);
            },
            err => {
                console.log(err);
                this.toastr.error(err._body, "Error");
            },
            () => {console.log("Completed")}
        );
    }
}
