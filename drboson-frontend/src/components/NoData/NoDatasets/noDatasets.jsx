import React from 'react';
import box from '../../../images/undraw_collecting_fjjl.svg';
import '../noData.css';

const NoDatasets = (props) => {
    return (
        <div className="no-data">
            <div className="no-data__art">
                <img alt="" src={box} />
            </div>
            <div className="no-data__content text-muted">
                <p className="h5">There are no datasets yet.</p>
                <p>Upload datasets to use for your runs.</p>
            </div>
        </div>
    );
}

export default NoDatasets;