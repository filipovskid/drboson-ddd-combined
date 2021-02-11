import React from 'react';
import statistics from '../../../images/undraw_statistics_ctoq.svg';
import '../noData.css';

const NoVisualization = (props) => {
    return (
        <div className="no-data mt-4">
            <div className="no-data__art">
                <img alt="" src={statistics} />
            </div>
            <div className="no-data__content text-muted">
                <p className="h5">This is your workspace</p>
                <p>Add visualizations and analyse your results.</p>
            </div>
        </div>
    );
}

export default NoVisualization;