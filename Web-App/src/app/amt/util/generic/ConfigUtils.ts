import {AMError} from "../vto/error/AMError";
import {IMyDpOptions} from "mydatepicker";
import {FormGroup} from "@angular/forms";
/**
 * Created by ahmed.motair on 12/1/2017.
 */

export class ConfigUtils{

    static handleError(err, toastr, TOASTR_TITLE) {
        console.log(err);
        let amError: AMError = err.error;

        if (amError.validation == null) {
            console.log(err);
            toastr.error(amError.message, TOASTR_TITLE);
            return false;
        }else
            return true;
    }

    static isNull(value){
        return (value == null || value == '');
    }
    static setDate(form: FormGroup): void {
        let date = new Date();
        form.patchValue({myDate: {
            date: {
                year: date.getFullYear(),
                month: date.getMonth() + 1,
                day: date.getDate()
            }
        }});
    }
}