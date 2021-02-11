import React from 'react';
import '../visType.css';


const VisTypeItem = (props) => {
    const { visType, onSelect } = props;

    return (
        <div onClick={() => onSelect(visType.type)} className="vis-type-selector__item">
            <div className="vis-type-selector__item--type">
                <img src={visType.icon} alt="" />
            </div>
            <div className="vis-type-selector__item--name">{visType.name}</div>
        </div>
    );
}

export default VisTypeItem;