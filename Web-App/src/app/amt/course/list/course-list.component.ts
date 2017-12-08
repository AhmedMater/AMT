/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Component, ViewContainerRef, OnInit, Input, Output, EventEmitter} from "@angular/core";
import {FormBuilder} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {Router} from "@angular/router";

import {RESTClient} from "../../services/RESTClient";
import {CourseService} from "../../services/CourseService";
import {CourseListRS} from "../../util/vto/resultset/CourseListRS";
import {PaginationService, PaginatePipe, PaginationControlsDirective} from "ngx-pagination";

@Component({
    selector: 'course-list',
    templateUrl: 'course-list.component.html',
    // directives: [PaginationControlsDirective],
    // pipes: [PaginatePipe],
    providers: [RESTClient, CourseService, FormBuilder, PaginationService],
})
export class CourseListComponent implements OnInit{
    courseList: CourseListRS = new CourseListRS();

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

    p: number = 1;

    // @Input() id: string = 'Pages';
    // @Input() maxSize: number = 200;
    // @Output() pageChange: EventEmitter<number>;
}
