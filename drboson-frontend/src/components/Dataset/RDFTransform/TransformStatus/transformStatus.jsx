import React from 'react';
import './transformStatus.css';

const TransformStatus = (props) => {
    const { nodesCount, edgesCount } = props;
    const { saveDisabled } = props;

    const onAddBranch = (e) => {
        e.stopPropagation();
        !props.onAddBranch || props.onAddBranch();
    }

    const onSave = (e) => {
        e.stopPropagation();
        !props.onSave || props.onSave();
    };

    return (
        <div className="transform-status">
            <div className="transform-status__info">
                <div className="transform-status__info--header">Transform preview</div>
                <div className="transform-status__info--info">
                    <span><strong>{nodesCount}</strong> nodes,</span> <span><strong>{edgesCount}</strong> edges</span>
                </div>
            </div>
            <div className="transform-status__actions">
                <button className="btn action" onClick={onAddBranch}>Add branch</button>
                <button className="btn action accent" onClick={onSave} disabled={saveDisabled}>Save</button>
            </div>
        </div >
    );
}

export default TransformStatus;