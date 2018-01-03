import {Role} from "../../dto/lookup/Role";
/**
 * Created by ahmed.motair on 12/31/2017.
 */

export class Roles{
    static OWNER: Role = new Role('Ow', 'Owner');
    static ADMIN: Role = new Role('Ad', 'Admin');
    static TUTOR: Role = new Role('Tu', 'Tutor');
    static STUDENT: Role = new Role('St', 'Student');
}