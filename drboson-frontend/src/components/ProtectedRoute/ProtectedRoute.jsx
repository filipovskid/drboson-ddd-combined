import React from 'react';
import {
    Route, Redirect
} from "react-router-dom";

const ProtectedRoute = ({ component: Component, isAuthenticated, children, ...rest }) => {

    return (
        <Route {...rest}
            render={(props) => {
                if (isAuthenticated) {
                    return children; // <Component {...props} {...rest} />;
                } else return <Redirect to='/login' />
            }} />
    );
}

export default ProtectedRoute;