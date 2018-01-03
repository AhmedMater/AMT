/**
 * Created by ahmed.motair on 11/30/2017.
 */

import {Component, OnInit, ViewContainerRef} from "@angular/core";
import {UserService} from "../../services/UserService";
import {FormBuilder, FormGroup} from "@angular/forms";
import {AuthenticationService} from "../../services/AuthenticationService";
import {UserListRS} from "../../util/vto/resultSet/UserListRS";
import {UserListFilter} from "../../util/dto/filters/UserListFilter";
import {AMError} from "../../util/vto/error/AMError";
import {FullRoutes} from "../../util/constants/FullRoutes";
import {Roles} from "../../util/constants/lookups/Roles";
import {PaginationInfo} from "../../util/vto/common/PaginationInfo";
import {SortingInfo} from "../../util/dto/common/SortingInfo";
import {Role} from "../../util/dto/lookup/Role";
import {CourseService} from "../../services/CourseService";
import {SortService} from "../../util/components.sorting/SortService";
import {ToastsManager} from "ng2-toastr";
import {Router} from "@angular/router";
import {ConfigUtils} from "../../util/generic/ConfigUtils";
import {PaginationService} from "ngx-pagination";
import {UserListLookup} from "../../util/vto/lookup/UserListLookup";
import {StringToDate} from "../../util/pipe/StringToDate";
import {IMyDpOptions} from "mydatepicker";
import {ConfigParam} from "../../util/constants/ConfigParam";

@Component({
    selector: 'user-list',
    templateUrl: 'user-list.component.html',
    providers: [UserService, FormBuilder, PaginationService,
        {provide: SortService, useClass: SortService}]
})
export class UserListComponent implements OnInit {
    userList: UserListRS = new UserListRS();
    filters: UserListFilter = new UserListFilter();
    roles: Role[];
    userListForm: FormGroup;

    amError: AMError;
    formInvalid: boolean;

    TOASTR_TITLE :string = "Course List";
    FULL_ROUTES = FullRoutes;
    ROLES = Roles;

    paging: PaginationInfo = new PaginationInfo();
    sorting: SortingInfo = new SortingInfo('username', 'asc');
    doPaging(pageNum): void {
        this.search();
    }

    doSort(order): void{
        this.sorting.by = order.sortColumn;
        this.sorting.direction = order.sortDirection;
        this.search();
    }

    constructor(
        private userService:UserService,
        private formBuilder:FormBuilder,
        public toastr: ToastsManager,
        private vcr: ViewContainerRef,
        public sortService: SortService,
        private router:Router
    ) {
        this.toastr.setRootViewContainerRef(vcr);
    }

    ngOnInit(): void {
        this.userService.getUserListLookup().subscribe(
            res => {
                let lookups: UserListLookup = res;

                this.roles = lookups.roles;
            },
            err => {
                this.amError = err.error;

                if(this.amError.validation != null)
                    this.formInvalid = true;
                else
                    this.toastr.error(this.amError.message, this.TOASTR_TITLE);
            }
        );

        this.userListForm = this.formBuilder.group({
            realName: '',
            role: '',
            creationDateFrom: null,
            creationDateTo: null
        });

        this.search();
    }

    search(): void{
        let values = this.userListForm.value;
        this.filters.realName = !ConfigUtils.isNull(values.realName) ? values.realName : null;
        this.filters.role = !ConfigUtils.isNull(values.role) ? values.role : null;
        this.filters.creationDateFrom = !ConfigUtils.isNull(values.creationDateFrom) ? values.creationDateFrom.jsdate : null;
        this.filters.creationDateTo = !ConfigUtils.isNull(values.creationDateTo) ? values.creationDateTo.jsdate : null;

        this.filters.pageNum = this.paging.pageNum;
        this.filters.sorting = this.sorting;

        this.userService.getUserList(this.filters).subscribe(
            res=>{
                this.userList = res;
                this.paging = res.pagination;
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

        this.userListForm = this.formBuilder.group({
            realName: '',
            role: '',
            creationDateFrom: null,
            creationDateTo: null
        });
        this.search();
    }

    DATE_PICKER_OPTIONS = ConfigParam.DATE_PICKER_OPTIONS;
    setDate(){
        ConfigUtils.setDate(this.userListForm);
    }
}