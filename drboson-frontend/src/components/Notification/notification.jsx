import React from 'react';
// import { ReactSVG } from 'react-svg';
// import checkmark from '../../images/checkmark.svg';
import './notification.css';

var classNames = require('classnames');

const Notification = React.forwardRef((props, ref) => {
    const { message, variant } = props

    const variants = {
        success: { class: 'success' },
        error: { class: 'error' },
        default: { class: '' }
    }

    const variant_class = variant in variants
        ? variants[variant].class
        : variants['default'].class;

    const classes = classNames('notification', variant_class);

    return (
        <div ref={ref} className={classes}>
            <div className="notification--icon"></div>
            <div className="notification__body">
                <div className="notification__body--message">{message}</div>
            </div>
        </div>
    );
})

export default Notification;