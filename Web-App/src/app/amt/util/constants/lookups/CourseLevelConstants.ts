import {CourseLevel} from "../../dto/lookup/CourseLevel";
/**
 * Created by ahmed.motair on 12/26/2017.
 */

export class CourseLevelConstants{
    static BEGINNER: CourseLevel = new CourseLevel('Be', 'Beginner');
    static INTERMEDIATE: CourseLevel = new CourseLevel('In', 'Intermediate');
    static ADVANCED: CourseLevel = new CourseLevel('Ad', 'Advanced');
}