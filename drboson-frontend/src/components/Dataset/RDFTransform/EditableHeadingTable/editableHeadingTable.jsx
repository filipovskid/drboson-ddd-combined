import React from 'react';
import { MultiGrid, AutoSizer } from 'react-virtualized';
import ColumnHeader from './ColumnHeader/columnHeader';
import './editableHeadingTable.css';
import '../TransformPreview/transformPreview.css';

var classNames = require('classnames');

const EditableHeadingTable = (props) => {
    const { literals } = props;
    const gridProps = {
        columnWidth: 120,
        columnCount: literals.length,
        height: 300,
        overscanColumnCount: 5,
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

    const handleNameChange = (literalId, value) => {
        props.handleLiteralNameChange(literalId, value);
    }

    const _renderHeader = (columnIndex, key, rowIndex, style) => {
        let cellClasses = classNames('cell', 'header-cell');
        const literal = literals[columnIndex];

        return (
            < div className={cellClasses} key={key} style={style} >
                <ColumnHeader
                    placeholder={literal.value}
                    value={literal.name}
                    handleNameChange={(value) => handleNameChange(literal.id, value)}
                />
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
    );
};

export default EditableHeadingTable;