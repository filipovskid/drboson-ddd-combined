import React from 'react';
import './loading.css';

var classNames = require('classnames');

const Loading = (props) => {
    const { loading, className, loader, ...loader_props } = props;
    console.log('props', loader_props);

    const classes = classNames('loading', { className: className });
    const Loader = loader;

    const content = loading
        ? (
            <div className={classes}>
                <Loader
                    loading={loading}
                    {...loader_props}
                />
            </div>
        )
        : null;

    return content;
};

export default Loading;