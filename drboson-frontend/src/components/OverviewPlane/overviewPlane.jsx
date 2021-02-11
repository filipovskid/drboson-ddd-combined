import React, { useState, useEffect } from 'react';
import './overviewPlane.css';

const OverviewPlane = (props) => {
    const [items, setItems] = useState([]);
    const { overview } = props;

    useEffect(() => {
        const shortid = require('shortid');
        const newItems = overview.items.map(item => {
            return { id: shortid.generate(), key: item.key, value: item.value }
        });

        setItems(newItems);
    }, [overview.items]);

    const overviewItems = items.map(item => {

        return (
            <div className="overview-item" key={item.id}>
                <div className="overview-item--key">{item.key}</div>
                <div className="overview-item--value">{item.value}</div>
            </div>
        );
    })

    return (
        <div className="overview-plane">
            <div className="overview-plane--wrapper">
                <div className="overview-header">
                    <div className="overview-header--heading">
                        <h1 className="header">{overview.heading}</h1>
                    </div>
                    <div className="overview-header--desc">{overview.desc}</div>
                    {overviewItems}
                </div>
            </div>
        </div>
    );
}

export default OverviewPlane;