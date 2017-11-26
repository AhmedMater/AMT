/**
 * Created by ahmed.motair on 10/25/2017.
 */
import {Injectable} from "@angular/core";
import {RESTClient} from "./RESTClient";
import {LoginData} from "../util/dto/user/LoginData";
import {UserRegisterData} from "../util/dto/user/UserRegisterData";

@Injectable()
export class UserService{
    private USER_BASE_URL: string = "/user";

    private LOGIN_PATH: string = this.USER_BASE_URL + "/login";
    private REGISTER_PATH: string = this.USER_BASE_URL + "/register";

    constructor(private rest:RESTClient){}

    login(loginData:LoginData){
        return this.rest.post(this.LOGIN_PATH, loginData, false);
    }

    register(userData: UserRegisterData){
        return this.rest.post(this.REGISTER_PATH, userData, false);
    }
}
