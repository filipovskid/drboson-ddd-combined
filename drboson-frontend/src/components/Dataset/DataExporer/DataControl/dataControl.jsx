import React from 'react';
import { Tabs, TabList, TabPanel } from 'react-tabs';
import ControlTab from './ControlTab/controlTab'
import ProcessingPanel from './ProcessingPanel/processingPanel';
import './dataControl.css';

ControlTab.tabsRole = 'Tab';

const DataControl = (props) => {

    return (
        <Tabs className="data-control" defaultIndex={0}>
            <TabList className="data-control__tabs">
                <ControlTab title="Processor" subtitle="rows" />
                <ControlTab title="Control" subtitle="rows" />
            </TabList>
            <TabPanel className="data-control__panel" selectedClassName="data-control__panel--selected">
                <ProcessingPanel />
            </TabPanel>
            <TabPanel className="data-control__panel" selectedClassName="data-control__panel--selected">
                Test 2
            </TabPanel>
        </Tabs>
    );
}

export default DataControl;