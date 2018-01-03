/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Injectable} from "@angular/core";
import {CourseData} from "../util/vto/course/CourseData";
import {HttpClient} from "@angular/common/http";
import {Response} from "@angular/http";
import {NewCourseLookup} from "../util/vto/lookup/NewCourseLookup";
import {ConfigParam} from "../util/constants/ConfigParam";
import {CourseListFilter} from "../util/dto/filters/CourseListFilter";
import {CourseListLookup} from "../util/vto/lookup/CourseListLookup";
import {ListResultSet} from "../util/vto/ListResultSet";
import {CourseListUI} from "../util/vto/ui/CourseListUI";
import {CourseListRS} from "../util/vto/resultSet/CourseListRS";

@Injectable()
export class CourseService {
    private COURSE_BASE_URL: string = ConfigParam.BASE_URL + "/course";

    private NEW_COURSE_PATH: string = this.COURSE_BASE_URL + "/new";
    private COURSE_DETAILS_PATH: string = this.COURSE_BASE_URL + "/";
    private NEW_COURSE_LOOKUPS_URL: string = this.NEW_COURSE_PATH + "/lookups";

    private COURSE_LIST_URL: string = this.COURSE_BASE_URL + "/list";
    private COURSE_LIST_LOOKUPS_URL: string = this.COURSE_LIST_URL + "/lookups";

    constructor(private http: HttpClient) {
    }

    addNewCourse(courseData: CourseData){
        return this.http.post<CourseData>(this.NEW_COURSE_PATH, courseData);
    }

    getCourseByID(courseID: string){
        return this.http.get<CourseData>(this.COURSE_DETAILS_PATH + courseID);
    }

    getCourseList(filters: CourseListFilter){
        return this.http.post<CourseListRS>(this.COURSE_LIST_URL, filters);
    }

    getCourseListLookup(){
        return this.http.get<CourseListLookup>(this.COURSE_LIST_LOOKUPS_URL);
    }

    getNewCourseLookups(){
        return this.http.get<NewCourseLookup>(this.NEW_COURSE_LOOKUPS_URL);
    }
}