/**
 * Created by ahmed.motair on 11/8/2017.
 */

import {CourseRefData} from "../../../util/dto/course/CourseRefData";
import {FormGroup, FormBuilder} from "@angular/forms";
import {OnInit, Injectable} from "@angular/core";
import {GenericTable} from "../../../util/generic/GenericTable";

@Injectable()
export class CourseReferences extends GenericTable{
    public constructor(private _formBuilder: FormBuilder){
        super(_formBuilder);
        this.emptyForm = {num: 0, name: '', type: '', url: ''};
        this.objToBeEdited = new CourseRefData();

        this.initializeForm();
    }
}