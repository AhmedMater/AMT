/**
 * Created by ahmed.motair on 10/25/2017.
 */

import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {ConfigParam} from "../util/constants/ConfigParam";
import {Observable} from "rxjs";
import {HttpHeaders, HttpParams} from "@angular/common/http";

@Injectable()
export class RESTClient{
    BASE_URL : string = ConfigParam.BASE_URL;

    observer:Observable<Response>;

    constructor(private http:Http){}

    get(path:string, params, authenticated:boolean){
        const url = this.BASE_URL + path;

        if(authenticated) {
            if(params != null)
                this.observer = this.http.get(url, { params: params });
            else
                this.observer = this.http.get(url);
        }else{
            if(params != null)
                this.observer = this.http.get(url, { params: params });
            else
                this.observer = this.http.get(url);
        }
    }

    post(path:string, object:any, authenticated:boolean){
        const url = this.BASE_URL + path;

        if(authenticated)
            return this.http.post(url, object);
        else
            return this.http.post(url, object);

        // return this.checkResponse(this.observer);
    }


    // This function is to check if the Response is 404 or 500
    // Check if the Response is 401 not Authorized
    checkResponse(observer): Observable<Response>{
        return this.observer;
    }
}