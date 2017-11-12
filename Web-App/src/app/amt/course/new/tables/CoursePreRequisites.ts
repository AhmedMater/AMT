
/**
 * Created by ahmed.motair on 11/8/2017.
 */

import {GenericTable} from "../../../util/generic/GenericTable";
import {FormBuilder} from "@angular/forms";
import {CoursePRData} from "../../../util/dto/course/CoursePRData";
import {Injectable} from "@angular/core";

@Injectable()
export class CoursePreRequisites extends GenericTable{
    public constructor(private _formBuilder:FormBuilder){
        super(_formBuilder);
        this.emptyForm = {num: 0, name: '', type: '', url: ''};
        this.objToBeEdited = new CoursePRData();

        this.initializeForm();
    }
}