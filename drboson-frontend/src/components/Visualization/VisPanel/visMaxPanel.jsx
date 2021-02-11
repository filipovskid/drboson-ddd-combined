import React from 'react';
import { ReactSVG } from 'react-svg';
import Modal from 'react-modal';
import x from '../../../images/x.svg';
import './visPanel.css';

const maxPanelModalStyles = {
    content: {
        padding: 0,
    },
    overlay: {
        backgroundColor: 'rgba(0, 0, 0, 0.85)'
    }
}

const VisMaxPanel = (props) => {
    const { preview } = props;

    return (
        <Modal ariaHideApp={false} isOpen={props.isMaximized}
            style={maxPanelModalStyles}
            onRequestClose={props.minimizePanel}>

            <div className="panel max-panel">
                <div className="panel__actions">
                    <span onClick={props.minimizePanel} className="panel__actions--action">
                        <ReactSVG src={x} />
                    </span>
                </div>
                <div className="panel__content">
                    <h6 className="panel__content--title">{preview.title}</h6>
                    <div className="panel__content--vis">
                        {preview.plot}
                    </div>
                </div>
            </div>
        </Modal>
    );
}

export default VisMaxPanel;