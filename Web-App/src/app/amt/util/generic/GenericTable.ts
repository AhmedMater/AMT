/**
 * Created by ahmed.motair on 11/8/2017.
 */

import {FormGroup, FormBuilder} from "@angular/forms";
import {OnInit, Injectable} from "@angular/core";

export class GenericTable{
    public emptyForm = {};
    public removingArray: any[] = [];
    public dataArray: any[] = [];
    public objToBeEdited: any;

    public form: FormGroup;
    public itemNumToBeEdit: number;
    public disableAdd: boolean = false;
    public disableNew: boolean = false;
    public disableSave: boolean = false;

    constructor(private formBuilder:FormBuilder){
        this.disableNew = true;
        this.disableSave = true;
    }

    public initializeForm(){
        this.form = this.formBuilder.group(this.emptyForm);
    }

    public addNewItem () {
        this.dataArray.push(this.form.value);
        this.emptyForm['num']++;
        this.clearForm();
    };

    public editItem(num: number) {
        this.disableAdd = true;
        this.disableSave = false;
        this.disableNew = false;
        this.itemNumToBeEdit = num;
        this.objToBeEdited = this.dataArray[this.itemNumToBeEdit];

        this.form = this.formBuilder.group({
            num: this.objToBeEdited.num,
            name: this.objToBeEdited.name,
            type: this.objToBeEdited.type,
            url: this.objToBeEdited.url
        });
    }

    public saveItem() {
        this.disableAdd = false;
        this.disableNew = true;
        this.disableSave = true;

        this.dataArray[this.itemNumToBeEdit] = this.form.value;
        this.clearForm();
    }

    public deleteItem(num: number) {
        this.disableAdd = false;
        this.disableNew = true;
        this.disableSave = true;

        if(num != this.dataArray.length-1) {
            for (let i = num + 1; i < this.dataArray.length; i++) {
                this.dataArray[i-1] = this.dataArray[i];
                this.dataArray[i-1].num--;
            }
        }
        this.dataArray.pop();

        this.emptyForm['num']--;

        this.clearForm();
    }

    public clearForm() {
        this.disableNew = true;
        this.disableSave = true;
        this.disableAdd = false;

        this.form = this.formBuilder.group(this.emptyForm);
    }
}