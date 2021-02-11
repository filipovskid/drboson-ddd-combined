import React from 'react';
import { Link } from "react-router-dom";
import './header.css'
import logo from '../../images/logos/drboson-light-yl.svg';

const Header = (props) => {
    const isAuthenticated = props.isAuthenticated;

    const avatar = (
        <div className="header-actions actions-right">
            <div className="header-actions__item avatar">
                <div data-toggle="dropdown">
                    <img alt="" src="https://avatars1.githubusercontent.com/u/37289276" />
                </div>
                <div className="dropdown-menu dropdown-menu-right">
                    <button className="dropdown-item" type="button">Action</button>
                    <button className="dropdown-item" type="button">Another action</button>
                    <button className="dropdown-item" type="button">Something else here</button>
                </div>
            </div>
        </div>
    )

    const actions = (
        <div className="header-actions actions-right">
            <div className="header-actions__item d-inline">
                <Link to="/login">
                    <button className="btn pale-btn">Login</button>
                </Link>
            </div>
            <div className="header-actions__item d-inline">
                <Link to="/join">
                    <button className="btn accent-btn">Join</button>
                </Link>
            </div>
        </div>
    )

    return (
        <header>
            <div className="header-actions actions-left">
                <div className="header-actions__item">
                    <div className="header-logo">
                        <Link to="/"><img src={logo} alt="" /></Link>
                    </div>
                </div>
            </div>

            {isAuthenticated
                ? avatar
                : actions
            }

        </header>
    );
}

export default Header