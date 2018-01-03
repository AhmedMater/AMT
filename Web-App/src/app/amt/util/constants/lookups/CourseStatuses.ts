/**
 * Created by ahmed.motair on 12/26/2017.
 */
import {CourseStatus} from "../../dto/lookup/CourseStatus";

export class CourseStatuses{
    static DONE: CourseStatus = new CourseStatus('Do', 'Done');
    static FUTURE: CourseStatus = new CourseStatus('Fu', 'Future');
    static IN_PROGRESS: CourseStatus = new CourseStatus('Pr', 'In Progress');
}