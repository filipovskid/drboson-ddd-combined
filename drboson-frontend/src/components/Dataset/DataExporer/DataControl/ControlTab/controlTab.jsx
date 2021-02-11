import React from 'react';
import { Tab } from 'react-tabs';
import './controlTab.css';

const ControlTab = (props) => {
    const { title, subtitle, ...rest } = props;

    return (
        <Tab {...rest} className='control-tab' selectedClassName='control-tab control-tab--selected'>
            <div className='control-tab__title'>{title}</div>
            <div className='control-tab__subtitle'>{subtitle}</div>
        </Tab>
    );
}

export default ControlTab;