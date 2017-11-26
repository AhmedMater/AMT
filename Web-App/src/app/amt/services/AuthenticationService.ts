/**
 * Created by ahmed.motair on 11/13/2017.
 */
import {Injectable} from "@angular/core";
import {ConfigParam} from "../util/constants/ConfigParam";
import {AppHeader} from "../../components/app-header/app-header.component";

@Injectable()
export class AuthenticationService{

    public isUserLoginIn(){
        return (localStorage.getItem(ConfigParam.AUTH_DATA) != null);
    }

    public getUserFullName(){
        return JSON.parse(localStorage.getItem(ConfigParam.AUTH_DATA)).FullName;
    }
    public setAuthenticationData(authUser){
        localStorage.setItem(ConfigParam.AUTH_DATA, JSON.stringify({
            "UserToken": authUser.token,
            "Username": authUser.username,
            "UserID": authUser.userID,
            "FullName": authUser.fullName,
            "Role": authUser.role,
            "RefreshToken": true
        }));
    }

    public logOutUser(){
        localStorage.removeItem(ConfigParam.AUTH_DATA);
    }
}