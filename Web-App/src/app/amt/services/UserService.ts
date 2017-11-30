/**
 * Created by ahmed.motair on 10/25/2017.
 */
import {Injectable} from "@angular/core";
import {RESTClient} from "./RESTClient";
import {LoginData} from "../util/dto/user/LoginData";
import {UserRegisterData} from "../util/dto/user/UserRegisterData";
import {HttpClient} from "@angular/common/http";
import {Response} from "@angular/http";
import {Observable} from "rxjs";
import {ConfigParam} from "../util/constants/ConfigParam";
import {UserProfileData} from "../util/vto/user/UserProfileData";

@Injectable()
export class UserService{
    private USER_BASE_URL: string = ConfigParam.BASE_URL + "/user";

    private LOGIN_PATH: string = this.USER_BASE_URL + "/login";
    private REGISTER_PATH: string = this.USER_BASE_URL + "/register";
    private USER_PROFILE_PATH: string = this.USER_BASE_URL + "/profile/";
    private CHANGE_ROLE_PATH: string = this.USER_BASE_URL + "/profile/changeRole/";


    observer:Observable<Response>;

    constructor(private http: HttpClient){}

    login(loginData:LoginData){
        return this.http.post<LoginData>(this.LOGIN_PATH, loginData);
    }

    register(userData: UserRegisterData){
        return this.http.post<UserRegisterData>(this.REGISTER_PATH, userData);
    }

    getUserProfileData(userID: number){
        return this.http.get<UserProfileData>(this.USER_PROFILE_PATH + userID);
    }

    changeUserRole(userID: number, newRole: string){
        return this.http.post<Response>(this.CHANGE_ROLE_PATH + userID, newRole);
    }
}
