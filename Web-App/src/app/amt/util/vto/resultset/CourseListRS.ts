/**
 * Created by ahmed.motair on 12/2/2017.
 */

import {CourseListUI} from "../ui/CourseListUI";
import {PaginationInfo} from "../PaginationInfo";

export class CourseListRS{
    data: CourseListUI[];
    pagination: PaginationInfo;

    constructor(){
        this.pagination = new PaginationInfo();
    }
}