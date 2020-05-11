import React, {Component} from 'react';
import './App.css';
import {authenticationService} from "./services/authentication.service";
import {Link} from "react-router-dom";
import {createBrowserHistory} from 'history';
import {Route, Router} from "react-router";
import {PrivateRoute} from "./components/PrivateRoute";
import {HomePage} from "./HomePage/HomePage";
import {PolarityPage} from "./PolarityPage/PolarityPage";
import {LoginPage} from "./LoginPage/LoginPage";

export const history = createBrowserHistory();

class App extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: null,
            isAdmin: false
        };
    }

    componentDidMount() {
        authenticationService.currentUser.subscribe(x => this.setState({
            currentUser: x,
            isAdmin: x && x.authorities.filter(role => role.authority === 'ROLE_ADMIN').length > 0
        }));
    }

    logout() {
        authenticationService.logout();
        history.push('/login');
    }

    render() {
        const { currentUser, isAdmin } = this.state;
        return (
            <Router history={history}>
                <div>
                    {currentUser &&
                    <nav className="navbar navbar-expand navbar-dark bg-dark">
                        <div className="navbar-nav">
                            <Link to="/" className="nav-item nav-link">Home</Link>
                            {isAdmin && <Link to="/admin" className="nav-item nav-link">Admin</Link>}
                            <a onClick={this.logout} className="nav-item nav-link">Logout</a>
                        </div>
                    </nav>
                    }
                    <div className="jumbotron">
                        <div className="container">
                            <div className="row">
                                <div className="col-md-6 offset-md-3">
                                    <PrivateRoute exact path="/" component={HomePage} />
                                    <PrivateRoute path="/admin" roles={['ROLE_ADMIN']} component={PolarityPage} />
                                    <Route path="/login" component={LoginPage} />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </Router>
        );
    }
}

export default App;
