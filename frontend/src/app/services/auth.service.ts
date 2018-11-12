import { shareReplay, map } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/user.model';

@Injectable()
export class AuthService {

    constructor(private http: HttpClient) {
    }

    login(email:string, password:string ) {
        return this.http.post<User>('/login', {email, password}, { observe: 'response' }).pipe(
            // this is just the HTTP call,
            // we still need to handle the reception of the token
            shareReplay(),
            map(resp => {
                const keys = resp.headers.keys();

                // login successful if there's a jwt token in the response
                const token = resp.headers.get('authorization');
                if (token) {
                    localStorage.setItem('id_token', token);
                }
            })
        );
    }

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
    }
}
