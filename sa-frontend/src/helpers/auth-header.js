
import {authenticationService} from "../services/authentication.service";

export function authHeader() {
    // return authorization header with jwt token
    const token = authenticationService.tokenValue;
    if (token) {
        console.log(token.access_token);
        return { Authorization: `Bearer ${token.access_token}` };
    } else {
        return {};
    }
}