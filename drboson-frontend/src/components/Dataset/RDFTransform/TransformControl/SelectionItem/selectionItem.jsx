import React from 'react';
import './selectionItem.css';

var classNames = require('classnames');

const SelectionItem = (props) => {
    // const [selected, setSelection] = useState(false);
    const { selected, label, uri } = props

    const onToggle = () => {
        // setSelection(prev => (!prev));
        !props.onToggle || props.onToggle();
    }

    let itemClasses = classNames('selection-item', { 'selected': selected })

    return (
        <li className={itemClasses} onClick={onToggle}>
            <div className="item-main">
                <input type="checkbox" className="item-checkbox" checked={selected} onChange={() => { }} />
                <div className="item-info">
                    <div className="item-info__header ellipsis">{label}</div>
                    <div className="item-info__actions">
                        <div className="item-info__actions--action">
                            <a className="item-uri" href={uri}>URI</a>
                        </div>
                    </div>
                </div>
            </div>
        </li>
    );
}

export default SelectionItem;