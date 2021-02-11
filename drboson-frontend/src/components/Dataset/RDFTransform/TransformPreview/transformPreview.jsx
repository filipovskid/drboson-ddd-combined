import React from 'react';
import { MultiGrid, AutoSizer } from 'react-virtualized';
import './transformPreview.css';

const TransformPreview = (props) => {
    var classNames = require('classnames');
    const { literals } = props;

    const gridProps = {
        columnWidth: 120,
        columnCount: literals.length,
        height: 300,
        overscanColumnCount: 0,
        overscanRowCount: 5,
        rowHeight: 30,
        rowCount: 10,
    };

    const {
        columnCount,
        columnWidth,
        // height,
        overscanColumnCount,
        // overscanRowCount,
        rowHeight,
        rowCount,
    } = gridProps;

    // const _cellRenderer = ({ columnIndex, key, rowIndex, style }) => {
    //     let cellClasses = classNames('cell', { 'header-cell': rowIndex === 0 });

    //     return (
    //         <div className={cellClasses} key={key} style={style}>
    //             {`R${rowIndex}, C${columnIndex}`}
    //         </div>
    //     );
    // };

    const _renderHeader = (columnIndex, key, rowIndex, style) => {
        let cellClasses = classNames('cell', 'header-cell');

        return (
            < div className={cellClasses} key={key} style={style} >
                <div className="ellipsis">{literals[columnIndex].name}</div>
            </div >
        );
    }

    const _renderBody = (columnIndex, key, rowIndex, style) => {
        return <div className="cell" key={key} style={style}> </div>;
    }

    const _cellRenderer = ({ columnIndex, key, rowIndex, style }) => {
        return rowIndex === 0
            ? _renderHeader(columnIndex, key, rowIndex, style)
            : _renderBody(columnIndex, key, rowIndex, style);
    };

    return (
        <div className="result-table">
            <AutoSizer>
                {({ height, width }) => (
                    <MultiGrid
                        cellRenderer={_cellRenderer}
                        classNameTopRightGrid={'HeaderGrid'}
                        height={height}
                        width={width}
                        columnCount={columnCount}
                        columnWidth={columnWidth}
                        overscanColumnCount={overscanColumnCount}
                        rowCount={rowCount}
                        rowHeight={rowHeight}
                        enableFixedRowScroll
                        enableFixedColumnScroll
                        fixedRowCount={1}
                        hideTopRightGridScrollbar
                        hideBottomLeftGridScrollbar
                    />
                )}
            </AutoSizer>
        </div>
    );
}

export default TransformPreview;