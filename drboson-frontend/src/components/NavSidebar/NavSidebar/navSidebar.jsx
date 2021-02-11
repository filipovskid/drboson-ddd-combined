import React, { useState, useEffect } from 'react';
import NavItem from '../NavItem/navItem'
import '../navSidebar.css';


const NavSidebar = (props) => {
    const [navInfo, setNavInfo] = useState([]);
    const { details } = props;

    useEffect(() => {
        const shortid = require('shortid');
        const navInfoData = details.map(item => {
            return { id: shortid.generate(), name: item.name, icon: item.icon, to: item.to }
        })

        setNavInfo(navInfoData);
    }, [details]);

    const navItems = navInfo.map(item => <NavItem name={item.name} icon={item.icon} to={item.to} key={item.id} />)

    return (
        <div className="nav-sidebar">
            <div className="nav-sidebar__items">
                {navItems}
            </div>
        </div>
    );

}

export default NavSidebar;