/**
 * Created by ahmed.motair on 10/25/2017.
 */

import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {ConfigParam} from "../util/constants/ConfigParam";
import {Observable} from "rxjs";
import {
    HttpHeaders, HttpParams, HttpInterceptor, HttpRequest, HttpHandler, HttpEvent,
    HttpClient
} from "@angular/common/http";

@Injectable()
export class RESTClient{
    BASE_URL : string = ConfigParam.BASE_URL;

    observer:Observable<Response>;

    constructor(private http: HttpClient ){}

    get(path:string, params, authenticated:boolean): Observable<Response>{
        const url = this.BASE_URL + path;

        if(authenticated) {
            if(params != null)
                this.observer = this.http.get<Response>(url, { params: params });
            else
                this.observer = this.http.get<Response>(url);
        }else{
            if(params != null)
                this.observer = this.http.get<Response>(url, { params: params });
            else
                this.observer = this.http.get<Response>(url);
        }
        return this.observer;
    }

    post(path:string, object:any, authenticated:boolean): Observable<Response>{
        const url = this.BASE_URL + path;

        if(authenticated)
            return this.http.post<Response>(url, object).map(res => res.json());
        else
            return this.http.post<Response>(url, object).map(res => res.json());
    }
}
