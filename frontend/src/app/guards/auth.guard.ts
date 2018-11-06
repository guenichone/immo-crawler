import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable()
export class AuthGuard implements CanActivate {

    constructor(private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        console.log('guard');
        const token = localStorage.getItem('id_token');
        if (token) {
            // logged in so return true
            console.log('token found ' + token);
            return true;
        }

        // not logged in so redirect to login page with the return url
        console.log('token not found, redirect to login');
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url }});
        return false;
    }
}
