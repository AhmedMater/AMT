/**
 * Created by ahmed.motair on 11/27/2017.
 */

export class Role{
    role: string;
    description: string;
    admin: boolean;

    constructor(role: string, description: string){
        this.role = role;
        this.description = description;
    }
}