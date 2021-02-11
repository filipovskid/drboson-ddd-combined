import React from 'react';
import transfer from '../../../images/undraw_transfer_files_6tns.svg';
import '../noData.css';

const NoFiles = (props) => {
    return (
        <div className="no-data mt-5">
            <div className="no-data__art">
                <img alt="" src={transfer} />
            </div>
            <div className="no-data__content text-muted">
                <p className="h5">No files were uploaded</p>
                <p>Your saved files will be available here.</p>
            </div>
        </div>
    );
}

export default NoFiles;