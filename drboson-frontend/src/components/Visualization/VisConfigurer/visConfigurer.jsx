import React from 'react';
import Modal from 'react-modal';
import './visConfigurer.css';

const modalStyle = {
    content: {
        padding: '0'
    },
    overlay: {
        backgroundColor: 'rgba(0, 0, 0, 0.85)'
    }
}

const VisConfigurer = (props) => {
    const { preview, configurer } = props;

    return (
        <Modal
            isOpen={props.isOpen}
            onRequestClose={props.onRequestClose}
            ariaHideApp={false}
            style={modalStyle}
        >
            <div className="visualization-modal">
                <div className="visualization-config">
                    <div className="visualization-config__preview">
                        <h6 className="plot-title">{preview.title}</h6>
                        {preview.plot}
                    </div>
                    <div className="visualization-config__settings">
                        {configurer}
                    </div>
                </div>
                <div className="visualization-actions">
                    <button onClick={props.onRequestClose} type="button" className="btn btn-cancel">Cancel</button>
                    <button onClick={() => props.onCreate(preview)} type="button" className="btn btn-ok">Create</button>
                </div>
            </div>
        </Modal>

    );
}

export default VisConfigurer;