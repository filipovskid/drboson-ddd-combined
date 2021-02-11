import React, { useState, useEffect, useRef } from 'react';
import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import SplitterLayout from 'react-splitter-layout';
import SelectionPanel from './SelectionPanel/selectionPanel';
import SelectionItem from './SelectionItem/selectionItem';
import BranchItem from './BranchItem/branchItem';
import 'react-splitter-layout/lib/index.css';
import './transformControl.css';

const TransformControl = (props) => {
    const [panelHeight, setPanelHeight] = useState(0);
    const controlPanelRef = useRef(null);
    const { literals, branches } = props;

    const literalItems = literals.map(literal => (
        <SelectionItem
            label={literal.property_name}
            uri={literal.property}
            selected={literal.selected}
            key={literal.id}
            onToggle={() => props.onLiteralToggle(literal)}
        />
    ));

    const branchItems = branches.map(branch => (
        <BranchItem
            label={branch.name}
            selected={branch.selected}
            key={branch.id}
            onToggle={() => props.onBranchToggle(branch.id)}
            onDelete={() => props.onBranchDelete(branch.id)}
        />
    ));

    useEffect(() => {
        setPanelHeight(controlPanelRef.current.clientHeight)
    }, [controlPanelRef]);

    const subjectName = props.subjectName ? props.subjectName : 'Class name';
    const literalCount = literalItems.length;

    return (
        <Tabs className="control">
            <TabList className="control__tabs">
                <Tab className="control__tabs--tab">
                    <div className="control__tab--content">
                        <span className="tab-title">{subjectName}</span>
                        <span className="tab-subtitle"><strong>{literalCount}</strong> literals</span>
                    </div>
                </Tab>
            </TabList>
            <TabPanel className="control__panel" >
                <div className="splitter-wrapper" ref={controlPanelRef}>
                    <SplitterLayout
                        vertical={true}
                        primaryMinSize={panelHeight / 2}
                        secondaryMinSize={100}
                    >
                        <SelectionPanel>
                            {literalItems}
                        </SelectionPanel>
                        <SelectionPanel>
                            {branchItems}
                        </SelectionPanel>
                    </SplitterLayout>
                </div>
            </TabPanel>
        </Tabs >
    );
}

export default TransformControl;