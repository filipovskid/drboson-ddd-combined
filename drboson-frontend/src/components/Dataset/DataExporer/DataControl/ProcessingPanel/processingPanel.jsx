import React from 'react';
import ProcessingStep from '../ProcessingStep/processingStep';
import './processingPanel.css';

const ProcessingPanel = (props) => {
    return (
        <div className="processing-panel">
            <ul className="processing-steps">
                <ProcessingStep />
                <ProcessingStep />
                <ProcessingStep />
            </ul>
        </div>
    );
};

export default ProcessingPanel;
