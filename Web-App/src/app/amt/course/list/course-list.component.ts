/**
 * Created by ahmed.motair on 11/4/2017.
 */

import {Component, ViewContainerRef, OnInit, Input, Output, EventEmitter} from "@angular/core";
import {FormBuilder, Form, FormGroup} from "@angular/forms";
import {ToastsManager} from "ng2-toastr";
import {Router} from "@angular/router";

import {RESTClient} from "../../services/RESTClient";
import {CourseService} from "../../services/CourseService";
import {PaginationService, PaginatePipe, PaginationControlsDirective} from "ngx-pagination";
import {NewCourseLookup} from "../../util/vto/lookup/NewCourseLookup";
import {CourseType} from "../../util/dto/lookup/CourseType";
import {CourseLevel} from "../../util/dto/lookup/CourseLevel";
import {AMError} from "../../util/vto/error/AMError";
import {CourseListFilter} from "../../util/dto/filters/CourseListFilter";
import {SortingInfo} from "../../util/dto/common/SortingInfo";
import {CourseListFilters} from "../../util/vto/lookup/CourseListFilters";
import {SortService} from "../../util/components.sorting/SortService";
import {CourseLevelConstants} from "../../util/constants/lookups/CourseLevelConstants";
import {CourseStatusConstants} from "../../util/constants/lookups/CourseStatusConstants";
import {FullRoutes} from "../../util/constants/FullRoutes";
import {PaginationInfo} from "../../util/vto/common/PaginationInfo";
import {isNull} from "util";
import {ConfigUtils} from "../../util/generic/ConfigUtils";
import {ListResultSet} from "../../util/vto/ListResultSet";
import {CourseListUI} from "../../util/vto/ui/CourseListUI";
import {CourseListRS} from "../../util/vto/CourseListRS";

@Component({
    selector: 'course-list',
    templateUrl: 'course-list.component.html',
    providers: [RESTClient, CourseService, FormBuilder, PaginationService,
        {provide: SortService, useClass: SortService}],
})
export class CourseListComponent implements OnInit{
    courseList: CourseListRS = new CourseListRS();
    filters: CourseListFilter = new CourseListFilter();
    courseLevels: CourseLevel[];
    courseTypes: CourseType[];

    courseListForm: FormGroup;

    amError: AMError;
    formInvalid: boolean;

    TOASTR_TITLE :string = "Course List";
    COURSE_DETAILS_URL = FullRoutes.COURSE_DETAILS_URL;
    CLConstants: CourseLevelConstants = CourseLevelConstants;
    CSConstants: CourseStatusConstants = CourseStatusConstants;

    paging: PaginationInfo = new PaginationInfo();
    sorting: SortingInfo = new SortingInfo('courseName', 'Asc');
    doPaging(pageNum): void {
        this.paging.pageNum = pageNum - 1;
        this.search();
    }

    doSort(order): void{
        this.sorting.by = order.sortColumn;
        this.sorting.direction = order.sortDirection;
        this.search();
    }

    constructor(
        private courseService:CourseService,
        private formBuilder:FormBuilder,
        public toastr: ToastsManager,
        private vcr: ViewContainerRef,
        public sortService: SortService,
        private router:Router
    ) {
        this.toastr.setRootViewContainerRef(vcr);
    }

    ngOnInit(): void {
        this.courseService.getCourseListFilters().subscribe(
            res => {
                let lookups: CourseListFilters = res;

                this.courseLevels = lookups.courseLevelList;
                this.courseTypes = lookups.courseTypeList;
            },
            err => {
                this.amError = err.error;

                if(this.amError.validation != null)
                    this.formInvalid = true;
                else
                    this.toastr.error(this.amError.message, this.TOASTR_TITLE);
            }
        );

        this.courseListForm = this.formBuilder.group({
            courseName: '',
            courseType: '',
            courseLevel: ''
        });

        this.search();
    }

    search(): void{
        let values = this.courseListForm.value;
        this.filters.courseName = !ConfigUtils.isNull(values.courseName) ? values.courseName : null;
        this.filters.courseType = !ConfigUtils.isNull(values.courseType) ? values.courseType : null;
        this.filters.courseLevel = !ConfigUtils.isNull(values.courseLevel) ? values.courseLevel : null;

        this.filters.pageNum = this.paging.pageNum;
        this.filters.sorting = this.sorting;

        this.courseService.getCourseList(this.filters).subscribe(
            res=>{
                this.courseList = res;
                this.paging = res.pagination;
                // this.paging.pageNum = res.pagination.pageNum;
            },
            err=>{
                this.amError = err.error;

                if(this.amError.validation != null)
                    this.formInvalid = true;
                else
                    this.toastr.error(this.amError.message, this.TOASTR_TITLE);
            }
        )
    }

    clear(): void{
        this.courseListForm = this.formBuilder.group({
            courseName: '',
            courseType: '',
            courseLevel: ''
        });
        this.search();
    }
}
