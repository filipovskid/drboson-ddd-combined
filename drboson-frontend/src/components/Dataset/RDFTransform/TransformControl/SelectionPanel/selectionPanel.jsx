import React from 'react';
import './selectionPanel.css';
const SelectionPanel = (props) => {
    return (
        <div className="selection-panel">
            <ul className="selection-items">
                {props.children}
            </ul>
        </div>
    );
}

export default SelectionPanel;