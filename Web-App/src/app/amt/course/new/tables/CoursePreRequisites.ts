
/**
 * Created by ahmed.motair on 11/8/2017.
 */

import {FormBuilder} from "@angular/forms";
import {Injectable} from "@angular/core";
import {CoursePRData} from "../../../util/vto/course/CoursePRData";
import {GenericTable} from "../../../util/generic/GenericTable";

@Injectable()
export class CoursePreRequisites extends GenericTable{
    public constructor(private _formBuilder:FormBuilder){
        super(_formBuilder);
        this.emptyForm = {num: 0, name: '', type: '', url: ''};
        this.objToBeEdited = new CoursePRData();

        this.initializeForm();
    }
}