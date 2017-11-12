/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {RESTClient} from "./RESTClient";
import {Injectable} from "@angular/core";

@Injectable()
export class ChapterService {

    // private LOGIN_PATH: string = "/user/login";
    // private REGISTER_PATH: string = "/user/register";

    constructor(private rest: RESTClient) {
    }
}