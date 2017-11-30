/**
 * Created by ahmed.motair on 11/27/2017.
 */

import {Role} from "../../dto/lookup/Role";
import {Response} from "@angular/http";

export class UserProfileData{
    fullName: string;
    email: string;
    role: string;

    canEdit: boolean;
    canUpgradeRole: boolean;

    roleList: Role[];

    constructor(){
        this.canEdit = false;
        this.canUpgradeRole = false;
        this.fullName = "";
        this.email = "";
        this.role = "";
    }
}