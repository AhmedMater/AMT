import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {UserService} from "../../services/UserService";
import {RESTClient} from "../../services/RESTClient";
import {Http} from "@angular/http";
import {FormGroup, FormBuilder} from "@angular/forms";
import {UserRegisterData} from "../../util/dto/UserRegisterData";
import {ToastsManager, Toast} from "ng2-toastr";
import {FullRoutes} from "../../util/constants/FullRoutes";

@Component({
    selector: 'register',
  templateUrl: 'register.component.html',
    providers: [RESTClient, UserService, FormBuilder]
})
export class RegisterComponent implements OnInit{
    registerForm: FormGroup;
    userRegisterData: UserRegisterData;

    HOME_URL: string = FullRoutes.HOME_URL;
    LOGIN_URL: string = FullRoutes.LOGIN_URL;

    constructor(
        private userService: UserService,
        private formBuilder: FormBuilder,
        public toastr: ToastsManager,
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
    }

    register(){
        this.userRegisterData = new UserRegisterData();
        this.userRegisterData.firstName = this.registerForm.value.firstName;
        this.userRegisterData.lastName = this.registerForm.value.lastName;
        this.userRegisterData.username = this.registerForm.value.username;
        this.userRegisterData.password = this.registerForm.value.passwordData.password;
        this.userRegisterData.email = this.registerForm.value.email;

        this.userService.register(this.userRegisterData).subscribe(
            res => {
                this.toastr.success("User Registration is successfully done.")
            },
            err => {
                console.log(err);
                this.toastr.error(err._body, "Error");
            }
        );
    }

}
