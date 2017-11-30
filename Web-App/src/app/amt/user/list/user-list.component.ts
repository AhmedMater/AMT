/**
 * Created by ahmed.motair on 11/30/2017.
 */

import {OnInit, Component} from "@angular/core";
import {AuthenticationService} from "../../services/AuthenticationService";
import {FormBuilder} from "@angular/forms";
import {RESTClient} from "../../services/RESTClient";
import {UserService} from "../../services/UserService";

@Component({
    selector: 'user-list',
    templateUrl: 'user-list.component.html',
    providers: [RESTClient, UserService, FormBuilder, AuthenticationService]
})
export class UserListComponent implements OnInit {

    ngOnInit(): void {
    }
}