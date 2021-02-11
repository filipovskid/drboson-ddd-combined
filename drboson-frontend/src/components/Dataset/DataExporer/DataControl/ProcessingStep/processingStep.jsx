import React, { useState } from 'react';
import { ReactSVG } from 'react-svg';
import trash from '../../../../../images/trash-2.svg'
import './processingStep.css';
var classNames = require('classnames');

const ProcessingStep = (props) => {
    const [configOpened, toggleOpened] = useState(false);

    const toggleConfig = () => toggleOpened(state => !state);
    const processorClasses = classNames('processor', { 'active': configOpened });

    return (
        <li className="processing-step" onClick={toggleConfig}>
            <div className={processorClasses}>
                <div className="processor__main">
                    <div className="processor-ops"></div>
                    <div className="processor-info">
                        <div className="processor-info__header">
                            Remove rows where columns matching.
                        </div>
                        <div className="processor-info__footer">
                            <span className="processor-info__changes">
                                -10000
                            </span>
                            <div className="processor-info__actions">
                                <a className="action"><ReactSVG src={trash} /></a>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="processor__config">
                    <hr />
                    {/* Create processor dependent config components.
                        These components could possibly contain reusable
                        components for individual information such as
                        applyTo (which column/columns shoult the processor
                        be applied to).
                    */}
                </div>
                {/* <div className="processor__footer"></div> */}
            </div>
        </li>
    );
};

export default ProcessingStep;
