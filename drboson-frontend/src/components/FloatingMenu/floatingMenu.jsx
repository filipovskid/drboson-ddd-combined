import React, { useState } from 'react';
import { ReactSVG } from 'react-svg';
import useOnclickOutside from "react-cool-onclickoutside";
import more_vertical from '../../images/more-vertical.svg';
import './floatingMenu.css';

const FloatingMenu = (props) => {
    const [isOpen, setIsOpen] = useState(false);
    var classNames = require('classnames');
    let menuClasses = classNames('floating-menu', props.className, { 'open': isOpen });

    const ref = useOnclickOutside(() => {
        setIsOpen(false);
    });

    const toggleMenu = () => setIsOpen(state => !state);

    return (
        <div className={menuClasses} onClick={toggleMenu} ref={ref}>
            <div className="floatin-menu--icon">
                <ReactSVG src={more_vertical} />
            </div>
            <ul className="floating-menu__items floating-menu-right">
                {props.children}
            </ul>
        </div>
    );
};

export default FloatingMenu;
