/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Component, ViewContainerRef, OnInit} from "@angular/core";
import {FormBuilder, FormGroup} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {Router} from "@angular/router";

import {RESTClient} from "../../services/RESTClient";
import {CourseService} from "../../services/CourseService";
import {LoginData} from "../../util/dto/LoginData";
import {CourseData} from "../../util/dto/course/CourseData";
import {CourseType} from "../../util/dto/lookup/CourseType";
import {CourseLevel} from "../../util/dto/lookup/CourseLevel";
import {MaterialType} from "../../util/dto/lookup/MaterialType";
import {NewCourseLookup} from "../../util/dto/course/NewCourseLookup";
import {CourseRefData} from "../../util/dto/course/CourseRefData";
import {CourseReferences} from "./tables/CourseReferences";
import {CoursePreRequisites} from "./tables/CoursePreRequisites";

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
            response => {
                let lookups: NewCourseLookup = JSON.parse(response['_body']);

                this.courseLevels = lookups.courseLevelList;
                this.courseTypes = lookups.courseTypeList;
                this.materialTypes = lookups.materialTypeList;
            },
            error => this.toastr.error("Failed while loading Course Types, Course Levels and Material Types" + error['_body'], "Error")
        );

        this.newCourseForm = this.formBuilder.group({
            courseName: '',
            courseType: '',
            courseLevel: '',
            estimatedDuration: '',
            description: ''
        });
    }

    public submitNewCourse() {
        this.courseData.courseName = this.newCourseForm.value.courseName;
        this.courseData.courseType = this.newCourseForm.value.courseType;
        this.courseData.courseLevel = this.newCourseForm.value.courseLevel;
        this.courseData.estimatedDuration = this.newCourseForm.value.estimatedDuration;
        this.courseData.description = this.newCourseForm.value.description;

        this.courseData.references = this.corRefs.dataArray;
        this.courseData.preRequisites = this.corPreReqs.dataArray;

        this.courseService.addNewCourse(this.courseData).subscribe(
            res => this.toastr.success("Course is created successfully"),
            err => this.toastr.error("Failed while adding new Course" + err['_body'], "Error")
        );
    }
}
