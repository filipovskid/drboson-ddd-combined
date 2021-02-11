import React from 'react';
import { ReactSVG } from 'react-svg'
import { NavLink } from "react-router-dom";

const NavItem = (props) => {
    const { icon, to } = props;

    return (
        <NavLink to={to} activeClassName='active-item'>
            <div className="nav-sidebar__items__item">
                {/* <img src={props.item.icon} alt={props.item.name} /> */}
                <ReactSVG src={icon} />
            </div>
        </NavLink>
    );
}

export default NavItem;