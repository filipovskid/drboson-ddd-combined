import React from 'react';
import ProgressBar from '../ProgressBar/progressBar';
import './headerCell.css';
var classNames = require('classnames');

const HeaderCell = (props) => {
    const { className = '', name, data, ...rest } = props;
    const headerClasses = classNames(className, 'header-cell');

    const validity = {
        'nbOK': data['noOK'],
        'nbBad': data['noMissingValues'],
        'nbEmpty': 0
    }

    return (
        <div className={headerClasses} {...props}>
            <div className="column-name elipsed" title={name}>
                {name}
            </div>
            <div className="column-type elipsed">
                {data.type}
            </div>
            <div className="column-validity">
                <ProgressBar validity={validity} />
            </div>
        </div>
    );
};

export default HeaderCell;
