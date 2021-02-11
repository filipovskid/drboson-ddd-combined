import React from 'react';

var classNames = require('classnames');

const FloatingMenuItem = (props) => {
    const { disabled } = props;

    const onClick = () => {
        if (disabled)
            return;

        !props.onClick || props.onClick();
    }

    let classes = classNames('floating-menu__items--item', { 'disabled': disabled });

    return (
        <li onClick={onClick} className={classes}>{props.children}</li>
    );
}

export default FloatingMenuItem;