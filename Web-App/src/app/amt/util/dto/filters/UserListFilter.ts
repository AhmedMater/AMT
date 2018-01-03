/**
 * Created by ahmed.motair on 12/31/2017.
 */

import {SortingInfo} from "../common/SortingInfo";
export class UserListFilter{
    realName: string;
    role: string;
    creationDateFrom: Date;
    creationDateTo: Date;

    pageNum: number;
    sorting: SortingInfo;
}