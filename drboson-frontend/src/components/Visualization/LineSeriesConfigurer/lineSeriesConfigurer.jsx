import React, { useState, useEffect } from 'react';
import Select from 'react-select';
import VisConfigurer from '../VisConfigurer/visConfigurer';
import './lineSeriesConfigurer.css';

import {
    XAxis,
    YAxis,
    // ChartLabel,
    HorizontalGridLines,
    // VerticalGridLines,
    // Highlight,
    // Crosshair,
    LineSeriesCanvas,
    FlexibleXYPlot,
} from 'react-vis';

const LineSeriesConfigurer = (props) => {
    const [title, setTitle] = useState('');
    const [x, setX] = useState('');
    const [y, setY] = useState('');
    const [dataBatches, setDataBatches] = useState([]);

    // Visualization
    // const [lastDrawLocation, setLastDrawLocation] = useState(null);
    // const [crosshairValues, setCrosshairValues] = useState([]);

    // const _onMouseLeave = () => { setCrosshairValues([]); }
    // const _onNearestX = (value, { index }) => {
    //     setCrosshairValues(dataBatches.map(line => line.data[index]));
    // };
    //
    const { data, fields } = props;
    const x_options = fields
        .filter(field => field.startsWith('_'))
        .map(field => { return { value: field, label: field } })

    const y_options = fields
        .filter(field => !field.startsWith('_'))
        .map(field => { return { value: field, label: field } })

    const onTitleChange = (e) => {
        setTitle(e.target.value)
    }

    useEffect(() => {
        // create compatible data
        let paramsNotSelected = ![x, y].every(param => fields.includes(param));

        if (paramsNotSelected) {
            setDataBatches([]);
            return;
        }

        const compatibleData = data.map(runData => {
            var shortid = require('shortid');

            const compatibleDataPerRun = runData.logs.filter(data => {
                return [x, y].every(param => Object.keys(data).includes(param));
            }).map(data => {
                return { x: data[x], y: data[y] }
            });

            return { data: compatibleDataPerRun, id: shortid.generate() };
        });

        console.log('LineSeries data batches: ', compatibleData);
        setDataBatches(compatibleData);
    }, [x, y, data, fields]);

    const lineSeries = dataBatches.map(runData => {
        return <LineSeriesCanvas data={runData.data} key={runData.id} /> //onNearestX={_onNearestX}
    });

    const plot = (
        <FlexibleXYPlot
        // xDomain={
        //     lastDrawLocation && [
        //         lastDrawLocation.left,
        //         lastDrawLocation.right
        //     ]
        // }
        // yDomain={
        //     lastDrawLocation && [
        //         lastDrawLocation.bottom,
        //         lastDrawLocation.top
        //     ]
        // }

        // onMouseLeave={_onMouseLeave}
        >
            <HorizontalGridLines style={{ strokeWidth: 0.5 }} />
            <XAxis />
            <YAxis style={{ line: { stroke: "none" } }} />
            {/* <Highlight
                onBrushEnd={area => setLastDrawLocation(area)}
                onDrag={area => setLastDrawLocation({
                    classes: " zoomed-in",
                    bottom: lastDrawLocation.bottom + (area.top - area.bottom),
                    left: lastDrawLocation.left - (area.right - area.left),
                    right: lastDrawLocation.right - (area.right - area.left),
                    top: lastDrawLocation.top + (area.top - area.bottom)
                })}
            /> */}
            {/* <Crosshair values={crosshairValues} /> */}

            {lineSeries}
        </FlexibleXYPlot>
    );

    const preview = {
        title: title,
        plot: plot,
        dataBatches: dataBatches
    }

    const lineSeriesConfigurer = (
        <div className="line-series-config">
            <h4 className="config-title">Line Plot Configurer</h4>
            <hr />
            <div className="input-group row no-gutters mb-2">
                <label htmlFor="plotTitle" className="col-2 col-form-label">Title</label>
                <div className="col-10 ml-sm-n4">
                    <input type="text" className="form-control" id="plotTitle"
                        onChange={onTitleChange} />
                </div>
            </div>

            <div className="input-group row no-gutters mb-2">
                <label htmlFor="x" className="col-2 col-form-label">X</label>
                <div className="col-10 ml-sm-n4">
                    <Select options={x_options} name="x" value={{ value: x, label: x }}
                        onChange={opt => setX(opt.value)} />
                </div>
            </div>

            <div className="input-group row no-gutters mb-2">
                <label htmlFor="y" className="col-2 col-form-label">Y</label>
                <div className="col-10 ml-sm-n4">
                    <Select options={y_options} name="y" value={{ value: y, label: y }}
                        onChange={opt => setY(opt.value)} />
                </div>
            </div>
        </div>
    );

    return (
        <VisConfigurer
            isOpen={props.isOpen}
            preview={preview}
            configurer={lineSeriesConfigurer}
            onRequestClose={props.onRequestClose}
            onCreate={props.onCreate}
        />
    );
}

export default LineSeriesConfigurer;