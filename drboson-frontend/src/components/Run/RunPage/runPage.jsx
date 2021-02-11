import React from 'react';
import { useParams } from "react-router-dom";
import NavSidebar from '../../NavSidebar/NavSidebar/navSidebar';
import info from '../../../images/menu-info.svg';
import workspace from '../../../images/menu-workspace.svg';
import folder from '../../../images/menu-folder.svg';
import terminal from '../../../images/menu-terminal.svg';
import './runPage.css';

const RunPage = (props) => {
    const { projectId, runId } = useParams();

    const navItemDetails = [
        { name: 'Info', icon: info, to: `/${projectId}/run/${runId}/info` },
        { name: 'Workspace', icon: workspace, to: `/${projectId}/run/${runId}/workspace` },
        { name: 'Files', icon: folder, to: `/${projectId}/run/${runId}/files` },
        { name: 'Logs', icon: terminal, to: `/${projectId}/run/${runId}/logs` },
    ];

    const Component = props.component;

    return (
        <div className="run-page">
            <NavSidebar details={navItemDetails} />
            <div className="run-page__content">
                <Component />
            </div>
        </div >
    );
}

export default RunPage