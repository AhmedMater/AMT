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
import {UserListRS} from "../util/vto/resultSet/UserListRS";
import {UserListFilter} from "../util/dto/filters/UserListFilter";
import {UserListLookup} from "../util/vto/lookup/UserListLookup";
import DateTimeFormat = Intl.DateTimeFormat;

@Injectable()
export class UserService{
    private USER_BASE_URL: string = ConfigParam.BASE_URL + "/user";

    private LOGIN_PATH: string = this.USER_BASE_URL + "/login";
    private REGISTER_PATH: string = this.USER_BASE_URL + "/register";
    private USER_PROFILE_PATH: string = this.USER_BASE_URL + "/profile/";
    private CHANGE_ROLE_PATH: string = this.USER_BASE_URL + "/profile/changeRole/";

    private USER_LIST_URL: string = this.USER_BASE_URL + "/list";
    private USER_LIST_LOOKUPS_URL: string = this.USER_LIST_URL + "/lookups";

    // observer:Observable<Response>;

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

    getUserList(filters: UserListFilter){
        return this.http.post<UserListRS>(this.USER_LIST_URL, filters);//.map(this.extractData);
    }

    getUserListLookup(){
        return this.http.get<UserListLookup>(this.USER_LIST_LOOKUPS_URL);
    }

    private extractData(res){
        console.log(res);
        var data = res.data || [];
        data.forEach((d) => {
            d.creationDate = new Date(d.creationDate);
        });
        res.data = data;
        return res;
    }
}
