import React from 'react';
import {Route, Redirect} from 'react-router-dom';

import {authenticationService} from "../services/authentication.service";

export const PrivateRoute = ({component: Component, roles, ...rest}) => (
    <Route {...rest} render={props => {
        const currentUser = authenticationService.currentUserValue;
        if (!currentUser) {
            // not logged in so redirect to login page with the return url
            return <Redirect to={{pathname: '/login', state: {from: props.location}}}/>
        }

        // check if route is restricted by role
        const currentRoles = currentUser.authorities.map(role => role.authority);
        if (roles && roles.filter(role => currentRoles.includes(role)).length === 0) {
            // role not authorised so redirect to home page
            return <Redirect to={{pathname: '/'}}/>
        }

        // authorised so return component
        return <Component {...props} />
    }}/>
)