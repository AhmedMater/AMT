
/**
 * Created by ahmed.motair on 12/29/2017.
 */

import {PaginationInfo} from "./common/PaginationInfo";
export class ListResultSet<T>{
    data: T[];
    pagination:PaginationInfo;
}