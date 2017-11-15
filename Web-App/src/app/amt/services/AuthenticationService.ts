/**
 * Created by ahmed.motair on 11/13/2017.
 */
import {Injectable} from "@angular/core";
import {ConfigParam} from "../util/constants/ConfigParam";

@Injectable()
export class AuthenticationService{

    public isUserLoginIn(){
        return (localStorage.getItem(ConfigParam.AUTH_DATA) != null);
    }

    public setAuthenticationData(authUser){
        console.log(authUser);
        localStorage.setItem(ConfigParam.AUTH_DATA, JSON.stringify({
            "UserToken": authUser.token,
            "Username": authUser.username,
            "FullName": authUser.fullName,
            "Role": authUser.role,
            "RefreshToken": true
        }));
    }

    public logOutUser(){
        localStorage.removeItem(ConfigParam.AUTH_DATA);
    }
}