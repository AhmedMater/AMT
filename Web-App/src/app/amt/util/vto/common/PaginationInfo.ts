/**
 * Created by ahmed.motair on 12/2/2017.
 */

export class PaginationInfo{
    total: number;
    pageSize: number;
    pageNum: number;

    constructor(){
        this.total = 0;
        this.pageNum = 0;
        this.pageSize = 10;
    }
}