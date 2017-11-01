/**
 * Created by ahmed.motair on 10/25/2017.
 */
import {Injectable} from "@angular/core";
import {RESTClient} from "./RESTClient";
import {LoginData} from "../util/dto/LoginData";
import {UserRegisterData} from "../util/dto/UserRegisterData";

@Injectable()
export class UserService{

    private LOGIN_PATH: string = "/user/login";
    private REGISTER_PATH: string = "/user/register";

    constructor(private rest:RESTClient){}

    login(loginData:LoginData){
        return this.rest.post(this.LOGIN_PATH, loginData, false);
    }

    register(userData: UserRegisterData){
        return this.rest.post(this.REGISTER_PATH, userData, false);
    }
}
