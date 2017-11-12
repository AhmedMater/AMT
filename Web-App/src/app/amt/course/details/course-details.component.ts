/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Component, ViewContainerRef, OnInit} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {Router} from "@angular/router";

import {RESTClient} from "../../services/RESTClient";
import {CourseService} from "../../services/CourseService";

@Component({
    selector: 'course-detail',
    templateUrl: 'course-details.component.html',
    providers: [RESTClient, CourseService, FormBuilder],
})
export class CourseDetailComponent implements OnInit{

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
        private router:Router
    ) {
        this.toastr.setRootViewContainerRef(vcr);
    }


    ngOnInit(): void {

    }
}
