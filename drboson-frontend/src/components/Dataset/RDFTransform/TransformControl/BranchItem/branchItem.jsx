import React from 'react';
import { ReactSVG } from 'react-svg';
import x from '../../../../../images/x.svg';
import './branchItem.css';

var classNames = require('classnames');

const BranchItem = (props) => {
    // const [selected, setSelection] = useState(false);
    const { selected } = props;

    const onToggle = (e) => {
        e.stopPropagation();

        !props.onToggle || props.onToggle();
    }

    const onDelete = (e) => {
        e.stopPropagation();

        !props.onDelete || props.onDelete();
    };

    let itemClasses = classNames('branch-item', { 'selected': selected })

    return (
        <li className={itemClasses} onClick={onToggle}>
            <div className="item-main">
                <div className="item-info">
                    <div className="item-info__text ellipsis">{props.label}</div>
                </div>
                <div className="item-actions">
                    <button className="btn action" onClick={onDelete}>
                        <ReactSVG src={x} />
                    </button>
                </div>
            </div>
        </li>
    );
};

export default BranchItem;