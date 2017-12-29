import {AMError} from "../vto/error/AMError";
/**
 * Created by ahmed.motair on 12/1/2017.
 */

export class ConfigUtils{

    static handleError(err, toastr, TOASTR_TITLE) {
        let amError: AMError = err.error;

        if (amError.validation == null) {
            toastr.error(amError.message, TOASTR_TITLE);
            return false;
        }else
            return true;
    }

    static isNull(value){
        return (value == null || value == '');
    }
}