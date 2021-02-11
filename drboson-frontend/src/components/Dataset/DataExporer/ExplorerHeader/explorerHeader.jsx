import React from 'react';
import './explorerHeader.css';

const ExplorerHeader = (props) => {
    return (
        <div className="explorer-header">
            <div className="explorer-summary">
                <h4 className="explorer-summary__heading">Script output</h4>
                <div className="explorer-summary__info">
                    <span className="dataset-structure">
                        <strong>0</strong> rows, <strong>9</strong> cols
                    </span>
                    (<span className="dataset-changes">-10000</span>)
                </div>
            </div>
            <div className="explorer-actions">
                {/* <button className="btn action"></button> */}
                <button className="btn action accent">Еxecute</button>
            </div>
        </div>
    );
}

export default ExplorerHeader;
