
/**
 * Created by ahmed.motair on 1/3/2018.
 */

import {Pipe, PipeTransform} from "@angular/core";

@Pipe({name: 'toDate'})
export class StringToDate implements PipeTransform {
    transform(value: string){
        if(value) {
            return new Date(value.replace("Z[UTC]", ""));
        }else
            return null;
    }
}