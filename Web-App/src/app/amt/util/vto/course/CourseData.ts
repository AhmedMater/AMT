/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {CoursePRData} from "./CoursePRData";
import {CourseRefData} from "./CourseRefData";

export class CourseData{
    courseName: string;
    courseLevel: string;
    courseType: string;
    description: string;

    estimatedDuration: number;
    actualDuration: number;

    createdOn: Date;
    createdBy: string;
    isCompleted:boolean;

    preRequisites: CoursePRData[];
    references: CourseRefData[];
}