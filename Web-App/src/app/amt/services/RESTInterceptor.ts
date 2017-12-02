/**
 * Created by ahmed.motair on 11/13/2017.
 */

import {Injectable} from "@angular/core";
import {HttpInterceptor, HttpEvent, HttpHandler, HttpRequest, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {ConfigParam} from "../util/constants/ConfigParam";

@Injectable()
export class RESTInterceptor implements HttpInterceptor{

    intercept(req: HttpRequest<any>,
              next: HttpHandler): Observable<HttpEvent<any>> {
        let token: string = null;

        if(localStorage.getItem(ConfigParam.AUTH_DATA) != null)
            token = JSON.parse(localStorage.getItem(ConfigParam.AUTH_DATA)).UserToken;

        if (token != null)
            req = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + token) });

        if (!req.headers.has('Content-Type'))
            req = req.clone({ headers: req.headers.set('Content-Type', 'application/json;charset=utf-8') });

        console.log(req);

        return next.handle(req);
    }
}