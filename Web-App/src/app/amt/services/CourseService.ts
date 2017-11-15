/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {RESTClient} from "./RESTClient";
import {Injectable} from "@angular/core";
import {CourseData} from "../util/dto/course/CourseData";

@Injectable()
export class CourseService {
    private COURSE_BASE_URL: string = "/course";

    private NEW_COURSE_PATH: string = this.COURSE_BASE_URL + "/new";
    private COURSE_DETAILS_PATH: string = this.COURSE_BASE_URL + "/";
    private NEW_COURSE_LOOKUPS_URL: string = this.NEW_COURSE_PATH + "/lookups";

    constructor(private rest: RESTClient) {
    }

    addNewCourse(courseData: CourseData){
        return this.rest.post(this.NEW_COURSE_PATH, courseData, false);
    }

    getCourseByID(courseID: string){
        return this.rest.get(this.COURSE_DETAILS_PATH + courseID, null, false);
    }

    getNewCourseLookups(){
        return this.rest.get(this.NEW_COURSE_LOOKUPS_URL, null, false);
    }
}