import React from 'react';
import './runStatus.css';

const RunStatus = (props) => {
    const StatusEnum = Object.freeze({
        PENDING: { value: "Pending", className: "pending" },
        RUNNING: { value: "Running", className: "running" },
        COMPLETED: { value: "Completed", className: "success" },
        FAILED: { value: "Failed", className: "failed" },
        DEFAULT: { value: "Unknown", className: "default" }
    })

    const status = props.status.toUpperCase() in StatusEnum
        ? StatusEnum[props.status.toUpperCase()]
        : StatusEnum.DEFAULT;

    return (
        <div className={`status ${status.className}`}>{status.value}</div>
    );
};

export default RunStatus;