/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Component, ViewContainerRef, OnInit} from "@angular/core";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {Router} from "@angular/router";

import {RESTClient} from "../../services/RESTClient";
import {CourseService} from "../../services/CourseService";
import {CourseLevel} from "../../util/dto/lookup/CourseLevel";
import {MaterialType} from "../../util/dto/lookup/MaterialType";
import {CourseType} from "../../util/dto/lookup/CourseType";
import {CourseData} from "../../util/vto/course/CourseData";
import {CourseReferences} from "./tables/CourseReferences";
import {CoursePreRequisites} from "./tables/CoursePreRequisites";
import {NewCourseLookup} from "../../util/vto/lookup/NewCourseLookup";
import {FullRoutes} from "../../util/constants/FullRoutes";
import {AMError} from "../../util/vto/error/AMError";

@Component({
    selector: 'new-course',
    templateUrl: 'new-course.component.html',
    providers: [RESTClient, CourseService, FormBuilder, CourseReferences, CoursePreRequisites]
})
export class NewCourseComponent implements OnInit{
    newCourseForm: FormGroup;
    courseData:CourseData;

    courseLevels: CourseLevel[];
    courseTypes: CourseType[];
    materialTypes: MaterialType[];

    amError: AMError;
    formInvalid: boolean;

    TOASTR_TITLE :string = "Create New Course";

    constructor(
        private courseService:CourseService,
        private formBuilder:FormBuilder,
        public toastr: ToastsManager,
        private vcr: ViewContainerRef,
        private router:Router,
        public corRefs: CourseReferences,
        public corPreReqs: CoursePreRequisites
    ) {
        this.corRefs = new CourseReferences(formBuilder);
        this.corPreReqs = new CoursePreRequisites(formBuilder);
        this.toastr.setRootViewContainerRef(vcr);
    }

    ngOnInit(): void {
        this.courseData = new CourseData();

        this.courseService.getNewCourseLookups().subscribe(
            res => {
                let lookups: NewCourseLookup = res;

                this.courseLevels = lookups.courseLevelList;
                this.courseTypes = lookups.courseTypeList;
                this.materialTypes = lookups.materialTypeList;
            },
            err => {
                this.amError = err.error;

                if(this.amError.validation != null)
                    this.formInvalid = true;
                else
                    this.toastr.error(this.amError.message, this.TOASTR_TITLE);
            }
        );

        this.newCourseForm = this.formBuilder.group({
            courseName: '',
            courseType: '',
            courseLevel: '',
            estimatedDuration: 0,
            estimatedMinPerDay: 0,
            description: ''
        });
    }

    public submitNewCourse() {
        this.courseData.courseName = this.newCourseForm.value.courseName;
        this.courseData.courseType = this.newCourseForm.value.courseType;
        this.courseData.courseLevel = this.newCourseForm.value.courseLevel;
        this.courseData.estimatedDuration = this.newCourseForm.value.estimatedDuration;
        this.courseData.description = this.newCourseForm.value.description;
        this.courseData.estimatedMinPerDay = this.newCourseForm.value.estimatedMinPerDay;

        this.courseData.references = this.corRefs.dataArray;
        this.courseData.preRequisites = this.corPreReqs.dataArray;

        this.courseService.addNewCourse(this.courseData).subscribe(
            res => {
                this.toastr.success("Course is created successfully");
                this.courseData = res;
                this.router.navigate([FullRoutes.COURSE_DETAILS_URL + this.courseData.courseID]);
            },
            err => {
                this.amError = err.error;

                if(this.amError.validation != null)
                    this.formInvalid = true;
                else
                    this.toastr.error(this.amError.message, this.TOASTR_TITLE);
            }
        );
    }
}
