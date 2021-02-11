import React, { Fragment } from 'react';
import ReactTooltip from 'react-tooltip'
import './storageStatus.css';

export const StatusEnum = Object.freeze({
    IDLE: { value: "Idle", className: "idle" },
    PENDING: { value: "Pending", className: "pending" },
    RUNNING: { value: "Running", className: "pending" },
    COMPLETED: { value: "Completed", className: "success" },
    FAILED: { value: "Failed", className: "failed" },
    DEFAULT: { value: "Unknown", className: "default" }
})

var classNames = require('classnames');

const StorageStatus = (props) => {
    const { status, tip, id } = props;

    const statusType = status.toUpperCase() in StatusEnum
        ? StatusEnum[status.toUpperCase()]
        : StatusEnum.DEFAULT;

    const statusClasses = classNames('circle-status', statusType.className);

    const tooltip = tip
        ? <ReactTooltip id={id} effect='solid'>{props.tip}</ReactTooltip>
        : null;

    return (
        <Fragment>
            <div data-tip data-for={id} className={statusClasses}></div>
            {tooltip}
        </Fragment >
    );
}

export default StorageStatus;