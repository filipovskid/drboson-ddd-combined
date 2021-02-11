import React from 'react';
import projectsArt from '../../../images/drawkit-charts-and-graphs-monochrome.svg';
import '../noData.css';

const NoProjects = (props) => (
    <div className="no-data d-flex">
        <div className="no-data__art">
            <img alt="" src={projectsArt} />
        </div>
        <div className="no-data__content text-muted">
            <p className="h5">There are no projects yet.</p>
            <p>Create a project and analyse your results.</p>
        </div>
    </div>
);

export default NoProjects;