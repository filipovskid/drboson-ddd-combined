import React from 'react';
import { useParams } from "react-router-dom";
import NavSidebar from '../../NavSidebar/NavSidebar/navSidebar';
import info from '../../../images/menu-info.svg';
import data from '../../../images/menu-data.svg';
import workspace from '../../../images/menu-workspace.svg';
import './projectPage.css';

const ProjectPage = (props) => {
    const { projectId } = useParams();

    const navItemDetails = [
        { name: 'Info', icon: info, to: `/${projectId}/info` },
        { name: 'Workspace', icon: workspace, to: `/${projectId}/workspace` },
        { name: 'Data', icon: data, to: `/${projectId}/data` },
    ];

    const Component = props.component;

    return (
        <div className="project-page">
            <NavSidebar details={navItemDetails} />
            <div className="project-page__content">
                <Component />
            </div>
        </div >
    );
}

export default ProjectPage