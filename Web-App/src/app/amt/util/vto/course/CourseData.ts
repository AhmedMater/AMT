/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {CoursePRData} from "./CoursePRData";
import {CourseRefData} from "./CourseRefData";

export class CourseData{
    courseID: string;
    courseName: string;
    courseLevel: string;
    courseType: string;
    description: string;

    estimatedDuration: number;
    actualDuration: number;

    estimatedMinPerDay: number;
    startDate: Date;
    dueDate: Date;

    createdOn: Date;
    createdBy: string;
    courseStatus:string;

    preRequisites: CoursePRData[] = [];
    references: CourseRefData[] = [];
}