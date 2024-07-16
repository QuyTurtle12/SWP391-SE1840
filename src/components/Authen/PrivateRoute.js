import React from 'react';
import { Route, Redirect } from 'react-router-dom';
import { getToken, getUserRole } from '../Authen/Auth'; // Assuming you have a way to get the user role

const PrivateRoute = ({ component: Component, role, ...rest }) => {
  const token = getToken(); // Retrieve the token
  const userRole = getUserRole(); // Retrieve the user role

  return (
    <Route
      {...rest}
      render={props =>
        token && userRole === role ? (
          <Component {...props} />
        ) : (
          <Redirect to="/not-authorized" /> // Redirect to a not authorized page
        )
      }
    />
  );
};

export default PrivateRoute;
