/**
 * Created by ahmed.motair on 12/26/2017.
 */

import {SortingInfo} from "../common/SortingInfo";
export class CourseListFilter{
    courseName: string;
    courseLevel:string;
    courseType:string;
    creationDateFrom: Date;
    creationDateTo: Date;
    startDateFrom: Date;
    startDateTo: Date;

    pageNum:number;
    sorting: SortingInfo;
}