import React, { useState } from 'react';
import { visTypeConfigs, visTypeConfigurers } from '../VisTypeSelector/VisTypes';
import VisTypeSelector from '../VisTypeSelector/visTypeSelector';

const defaultConfigurer = {
    isOpen: false,
    configurer: null,
}

const VisBuilder = (props) => {
    const [isSelectorOpen, setIsSelectorOpen] = useState(props.isOpen);
    const [configurer, setConfigurer] = useState(defaultConfigurer);
    const { runLogs } = props;
    const fields = [...new Set(runLogs.map(run => run.logs).flat()
        .map(log => Object.keys(log)).flat())]


    const closeBuilder = () => {
        // setIsSelectorOpen(false);
        setConfigurer(defaultConfigurer);
        props.closeBuilder();
    }

    const selectVisualizationType = (visType) => {
        const newConfigurer = {
            isOpen: true,
            configurer: visTypeConfigurers[visType],
        }
        setIsSelectorOpen(false);
        setConfigurer(newConfigurer);
    }

    let Configurer = configurer.configurer;
    Configurer = Configurer ?
        <Configurer
            isOpen={configurer.isOpen}
            onRequestClose={closeBuilder}
            fields={fields}
            data={runLogs}
            onCreate={props.onVisualizationCreate}
        /> : null;

    return (
        <React.Fragment>
            <VisTypeSelector
                isOpen={isSelectorOpen}
                visTypeConfigs={visTypeConfigs}
                onTypeSelection={selectVisualizationType}
                onRequestClose={closeBuilder}
            />
            {Configurer}
        </React.Fragment>
    );
}

export default VisBuilder;