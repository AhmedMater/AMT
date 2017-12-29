/**
 * Created by ahmed.motair on 12/26/2017.
 */
import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

@Injectable()
export class SortService {

    constructor() { }

    private columnSortedSource = new Subject<ColumnSortedEvent>();

    columnSorted$ = this.columnSortedSource.asObservable();

    columnSorted(event: ColumnSortedEvent) {
        this.columnSortedSource.next(event);
    }

}

export interface ColumnSortedEvent {
    sortColumn: string;
    sortDirection: string;
}