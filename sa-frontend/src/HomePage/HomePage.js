import React from 'react';
import {authenticationService} from "../services/authentication.service";


class HomePage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: authenticationService.currentUserValue,
            userFromApi: null
        };
    }

    render() {
        const {currentUser} = this.state;
        const authorities = currentUser.authorities.map((role) =>
            <li>{role.authority}</li>
        );
        return (
            <div>
                <h1>Home</h1>
                <p>You're logged in with React & JWT!!</p>
                <p>Your role is: <strong>{currentUser.name}</strong>.</p>
                <p>Your authorities is: <strong>{authorities}</strong>.</p>
                <p>This page can be accessed by all authenticated users.</p>

            </div>
        );
    }
}

export {HomePage};