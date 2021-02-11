import React from 'react';
import process from '../../../images/undraw_processing_qj6a.svg';
import '../noData.css';

const NoRuns = (props) => {
    return (
        <div className="no-data">
            <div className="no-data__art">
                <img alt="" src={process} />
            </div>
            <div className="no-data__content text-muted">
                <p className="h5">There are no runs yet.</p>
                <p>Create your first run, gather metrics and save files.</p>
            </div>
        </div>
    );
}

export default NoRuns;