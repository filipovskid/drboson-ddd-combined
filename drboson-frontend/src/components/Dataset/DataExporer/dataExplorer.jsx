import React from 'react';
import DataControl from './DataControl/dataControl';
import ExplorerHeader from './ExplorerHeader/explorerHeader';
import DataTable from './DataTable/dataTable';
import './dataExplorer.css'

const DataExplorer = (props) => {
    return (
        <div className="data-explorer">
            <div className="data-explorer__control">
                <DataControl />
            </div>
            <div className="data-explorer__main-pane">
                <div className="data-explorer__header">
                    <ExplorerHeader />
                </div>
                <div className="data-explorer__workspace">
                    <DataTable />
                </div>
            </div>
        </div>
    );
}

export default DataExplorer;