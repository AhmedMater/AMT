/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Injectable} from "@angular/core";
import {CourseData} from "../util/vto/course/CourseData";
import {HttpClient} from "@angular/common/http";
import {Response} from "@angular/http";
import {NewCourseLookup} from "../util/vto/course/NewCourseLookup";
import {ConfigParam} from "../util/constants/ConfigParam";

@Injectable()
export class CourseService {
    private COURSE_BASE_URL: string = ConfigParam.BASE_URL + "/course";

    private NEW_COURSE_PATH: string = this.COURSE_BASE_URL + "/new";
    private COURSE_DETAILS_PATH: string = this.COURSE_BASE_URL + "/";
    private NEW_COURSE_LOOKUPS_URL: string = this.NEW_COURSE_PATH + "/lookups";

    constructor(private http: HttpClient) {
    }

    addNewCourse(courseData: CourseData){
        return this.http.post<CourseData>(this.NEW_COURSE_PATH, courseData);
    }

    getCourseByID(courseID: string){
        return this.http.get<CourseData>(this.COURSE_DETAILS_PATH + courseID);
    }

    getNewCourseLookups(){
        return this.http.get<NewCourseLookup>(this.NEW_COURSE_LOOKUPS_URL);
    }
}