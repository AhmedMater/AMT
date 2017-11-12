/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {RESTClient} from "./RESTClient";
import {Injectable} from "@angular/core";
import {CourseType} from "../util/dto/lookup/CourseType";

@Injectable()
export class LookupService {
    private LOOKUP_BASE_URL: string = "/lookup";

    private COURSE_TYPES_URL: string = this.LOOKUP_BASE_URL + "/courseTypes";
    private COURSE_LEVELS_URL: string = this.LOOKUP_BASE_URL + "/courseLevels";

    constructor(private rest: RESTClient) {
    }


    // getAllCourseTypes(){
    //     return this.rest.get(this.COURSE_TYPES_URL, null, false);
    // }
    //
    // getAllCourseLevels(){
    //     return this.rest.get(this.COURSE_LEVELS_URL, null, false);
    // }
}