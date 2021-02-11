import React from 'react';
import './progressBar.css';
var classNames = require('classnames');

const ProgressBar = (props) => {
    const { validity } = props;
    const total = Object.values(validity).reduce((a, b) => a + b);
    const validity_checks = ['nbOK', 'nbBad', 'nbEmpty'];
    const classes_map = { 'nbOK': 'bar--ok', 'nbBad': 'bar--bad', 'nbEmpty': 'bar--empty' }

    const computePercentage = (count, total) => {
        const percent = (count / total) * 100;

        return Math.round((percent + Number.EPSILON) * 100) / 100
    }
    const percentages = {
        'nbOK': computePercentage(validity.nbOK, total),
        'nbBad': computePercentage(validity.nbBad, total),
        'nbEmpty': computePercentage(validity.nbEmpty, total)
    }

    const message = `OK: ${percentages.nbOK}% - Bad: ${percentages.nbBad}% - Empty: ${percentages.nbEmpty}%`

    const bars = validity_checks.map(check => {
        const barClasses = classNames('bar', classes_map[check]);
        const percent = percentages[check];

        return (
            <div key={check} className={barClasses} title={message} style={{ width: `${percent}%` }}></div>
        )
    });

    return (
        <div className="validity-bar">
            {bars}
        </div>
    );
};

export default ProgressBar;
