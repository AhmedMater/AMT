/**
 * Created by ahmed.motair on 11/4/2017.
 */
import {CoursePRData} from "./CoursePRData";
import {CourseRefData} from "./CourseRefData";

export class CourseData{
    courseName: string;
    courseLevel: string;
    courseType: string;
    estimatedDuration: number;
    description: string;

    preRequisites: CoursePRData[];
    references: CourseRefData[];
}