import React from 'react';
import VisPanel from '../VisPanel/visPanel';
import './visPlane.css';


const VisPlane = (props) => {
    const { previews } = props;

    const visPanels = previews.map(preview => {
        return (
            <div className="visualization-plane__content--panel col-md-6 mb-3" key={preview.id}>
                <VisPanel preview={preview} />
            </div>)
    });

    return (
        <div className="visualization-plane">
            <div className="visualization-plane__content row">
                {visPanels}
            </div>
        </div >
    );
}

export default VisPlane;