import {BehaviorSubject} from 'rxjs';

import {authHeader} from "../helpers/auth-header";
import {handleResponse} from "../helpers/handle-resopnse";

const currentUserSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('currentUser')));
const token = new BehaviorSubject(JSON.parse(localStorage.getItem('token')));

export const authenticationService = {
    login,
    loginByCode,
    logout,
    currentUser: currentUserSubject.asObservable(),
    token: token.asObservable(),
    get currentUserValue() {
        return currentUserSubject.value
    },
    get tokenValue() {
        return token.value
    }
};

function loginByCode(code) {
    let params = new URLSearchParams({client_id: 'browser', code: code, grant_type: 'authorization_code', redirect_uri: 'http://localhost:4000/login'})
    const requestOptions = {
        method: 'POST',
        //mode: 'no-cors',
        //credentials: 'include',
        headers: {'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': 'Basic YnJvd3Nlcjo=',
        },
        body: params
    };

    return fetch(`auth/oauth/token`, requestOptions)
        .then(handleResponse)
        .then(res => {
            console.log(res)
            // store user details and jwt token in local storage to keep user logged in between page refreshes
            localStorage.setItem('token', JSON.stringify(res));
            token.next(res);
            return res;
        })
        .then(res => {
            return fetch(`auth/users/current`, {method: 'GET', headers: authHeader()})
                .then(handleResponse)
                .then( user => {
                    console.log(user);
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    currentUserSubject.next(user);
                    document.location.replace('/');
                    return user;
                })
        });
}

function login(username, password) {
    let params = new URLSearchParams({username: username, password: password, grant_type: 'password'})
    const requestOptions = {
        method: 'POST',
        //mode: 'no-cors',
        //credentials: 'include',
        headers: {'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': 'Basic YnJvd3Nlcjo=',
        },
        body: params
    };

    return fetch(`auth/oauth/token`, requestOptions)
        .then(handleResponse)
        .then(res => {
            console.log(res)
            // store user details and jwt token in local storage to keep user logged in between page refreshes
            localStorage.setItem('token', JSON.stringify(res));
            token.next(res);
            return res;
        })
        .then(res => {
            return fetch(`auth/users/current`, {method: 'GET', headers: authHeader()})
                .then(handleResponse)
                .then( user => {
                    console.log(user);
                    localStorage.setItem('currentUser', JSON.stringify(user));
                    currentUserSubject.next(user);
                    return user;
            })
        });
}

function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('currentUser');
    currentUserSubject.next(null);
    localStorage.removeItem('token');
    token.next(null);
    //window.location.replace('http://localhost:8085/oauth/logout?client_id=browser&response_type=code&redirect_uri=http://localhost:3000/login');
    document.cookie.split(";").forEach(function(c) { document.cookie = c.replace(/^ +/, "").replace(/=.*/, "=;expires=" + new Date().toUTCString() + ";path=/"); });
}
