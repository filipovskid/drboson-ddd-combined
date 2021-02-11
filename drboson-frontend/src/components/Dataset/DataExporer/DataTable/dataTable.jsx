import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { Grid, ScrollSync, AutoSizer } from 'react-virtualized';
import DatasetService from '../../../../actions/dataset';
import HeaderCell from './HeaderCell/headerCell';
import './dataTable.css';
var classNames = require('classnames');


const DataTable = (props) => {
    const [loading, setLoading] = useState(true);
    const [columns, setColumns] = useState([]);
    const { datasetId } = useParams();

    const grid_props = {
        columnWidth: 75,
        columnCount: columns.length !== 0 ? columns.length : 10,
        height: 300,
        overscanColumnCount: 0,
        overscanRowCount: 5,
        rowHeight: 40,
        headerHeight: 52,
        rowCount: 1000,
    };

    useEffect(() => {
        DatasetService.refreshColumns(datasetId)
            .then(response => {
                setColumns(response.data.headers);
                setLoading(false);

            });
    }, [datasetId]);

    const _renderHeaderCell = ({ columnIndex, key, rowIndex, style }) => {
        if (loading)
            return (
                <HeaderCell
                    key={key}
                    style={style}
                    name={`R${rowIndex}, C${columnIndex}`}
                    data={{ 'noMissingValues': 0, 'noOK': 1000 }}
                />
            );

        return (
            <HeaderCell
                key={key}
                style={style}
                name={columns[columnIndex].name}
                data={columns[columnIndex]}
            />
        );
    };

    const _renderBodyCell = ({ columnIndex, key, rowIndex, style }) => {
        const isEven = rowIndex % 2 === 0;
        const cellClasses = classNames('cell', { 'odd-row': !isEven }, { 'even-row': isEven });

        return (
            <div className={cellClasses} key={key} style={style}>
                {`R${rowIndex}, C${columnIndex}`}
            </div>
        );
    };

    const table = <ScrollSync>
        {({
            clientHeight,
            clientWidth,
            onScroll,
            scrollHeight,
            scrollLeft,
            scrollTop,
            scrollWidth,
        }) => {

            return (
                <AutoSizer>
                    {({ width, height }) => (
                        <div>
                            <Grid
                                className="header-grid"
                                columnWidth={grid_props.columnWidth}
                                columnCount={grid_props.columnCount}
                                height={grid_props.headerHeight}
                                overscanColumnCount={grid_props.overscanColumnCount}
                                cellRenderer={_renderHeaderCell}
                                rowHeight={grid_props.headerHeight}
                                rowCount={1}
                                scrollLeft={scrollLeft}
                                width={width}
                            />
                            <Grid
                                className="data-grid"
                                columnWidth={grid_props.columnWidth}
                                columnCount={grid_props.columnCount}
                                height={height}
                                onScroll={onScroll}
                                overscanColumnCount={grid_props.overscanColumnCount}
                                overscanRowCount={grid_props.overscanRowCount}
                                cellRenderer={_renderBodyCell}
                                rowHeight={grid_props.rowHeight}
                                rowCount={grid_props.rowCount}
                                width={width}
                            />
                        </div>
                    )}
                </AutoSizer>
            );
        }}
    </ScrollSync>

    return (
        <div className="data-table">
            {table}
        </div>
    );
};

export default DataTable;
