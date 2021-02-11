import React from 'react';
import './columnHeader.css';

const ColumnHeader = (props) => {
    let { value, placeholder } = props;

    const handleNameChange = (e) => {
        !props.handleNameChange || props.handleNameChange(e.target.value);
    };

    return (
        <div className="column-header">
            <input name="column-name" type="text"
                value={value}
                placeholder={placeholder}
                onChange={handleNameChange}
            />
        </div>
    );
};

export default ColumnHeader;