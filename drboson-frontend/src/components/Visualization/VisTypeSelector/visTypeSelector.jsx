import React, { useState, useEffect } from 'react';
import VisTypeItem from './VisTypeItem/visTypeItem';
import Modal from 'react-modal';
import './visType.css'

const modalStyle = {
    content: {
        width: '740px',
        height: 'auto',
        minHeight: '500px',
        maxHeight: '80vh',
        overflow: 'auto',
        margin: 'auto',
        position: 'relative',
        top: '10%'
    },
    overlay: {
        backgroundColor: 'rgba(0, 0, 0, 0.85)'
    }
}

const VisTypeSelector = (props) => {
    const [visTypes, setVisTypes] = useState([]);
    const { visTypeConfigs, onTypeSelection } = props;

    useEffect(() => {
        const shortid = require('shortid');
        setVisTypes(visTypeConfigs.map(type => { return { ...type, 'id': shortid.generate() } }));
    }, [visTypeConfigs]);


    const visTypeItems = visTypes.map(type => {
        return <VisTypeItem visType={type} key={type.id} onSelect={onTypeSelection} />
    });

    return (
        <Modal
            isOpen={props.isOpen}
            style={modalStyle}
            onRequestClose={props.onRequestClose}
            ariaHideApp={false}
        >
            <div className="vis-type-selector">
                <div className="vis-type-selector__section">
                    <div className="vis-type-selector__section--heading">
                        Charts
                </div>
                    <div className="vis-type-selector__section--content">
                        {visTypeItems}
                    </div>
                </div>
                {/* <div className="vis-type-selector__section disabled">
                <div className="vis-type-selector__section--heading">
                    Charts
                </div>
                <div className="vis-type-selector__section--content">
                    <VisTypeItem />
                </div>
            </div> */}
            </div>
        </Modal>
    );
}

export default VisTypeSelector;