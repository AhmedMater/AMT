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
        let token: string = JSON.parse(localStorage.getItem(ConfigParam.AUTH_DATA)).UserToken;

        if (token)
            req = req.clone({ headers: req.headers.set('Authorization', 'Bearer ' + token) });

        if (!req.headers.has('Content-Type'))
            req = req.clone({ headers: req.headers.set('Content-Type', 'application/json') });

        return next.handle(req);
    }
}