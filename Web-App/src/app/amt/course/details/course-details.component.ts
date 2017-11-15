/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Component, ViewContainerRef, OnInit} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {Router, ActivatedRoute, Params} from "@angular/router";

import {RESTClient} from "../../services/RESTClient";
import {CourseService} from "../../services/CourseService";
import {CourseData} from "../../util/dto/course/CourseData";

@Component({
    selector: 'course-detail',
    templateUrl: 'course-details.component.html',
    providers: [RESTClient, CourseService, FormBuilder],
})
export class CourseDetailComponent implements OnInit{

    courseID: string;
    courseData: CourseData;

    // loginForm:FormGroup;
    // loginData:LoginData;
    //
    // HOME_URL: string = FullRoutes.HOME_URL;
    // REGISTER_URL: string = FullRoutes.REGISTER_URL;

    constructor(
        private courseService:CourseService,
        private formBuilder:FormBuilder,
        public toastr: ToastsManager,
        private vcr: ViewContainerRef,
        private router:Router,
        private activatedRoute: ActivatedRoute
    ) {
        this.toastr.setRootViewContainerRef(vcr);
    }


    ngOnInit(): void {
        this.activatedRoute.params.subscribe((params: Params) => {
            console.log(params);
            this.courseID = params['courseID'];
            console.log(this.courseID);

            this.courseService.getCourseByID(this.courseID).subscribe(
                response => {
                    this.courseData = JSON.parse(response['_body']);
                    console.log(this.courseData);
                },
                error => this.toastr.error("Failed while loading Course Data Due to: " + error['_body'], "Error")
            );
        });
    }
}
