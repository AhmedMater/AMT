/**
 * Created by ahmed.motair on 11/7/2017.
 */

import {CourseType} from "../../dto/lookup/CourseType";
import {CourseLevel} from "../../dto/lookup/CourseLevel";
import {MaterialType} from "../../dto/lookup/MaterialType";

export class NewCourseLookup{
    courseTypeList: CourseType[];
    courseLevelList: CourseLevel[];
    materialTypeList: MaterialType[];
}