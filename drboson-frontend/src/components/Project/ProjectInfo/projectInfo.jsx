import React, { useState, useEffect } from 'react';
import { useParams, useHistory, Link } from "react-router-dom";
import { ReactSVG } from 'react-svg';
import OverviewPlane from '../../OverviewPlane/overviewPlane';
import RunItem from '../../Run/RunItem/runItem';
import NoRuns from '../../NoData/NoRuns/noRuns';
import ProjectService from '../../../actions/project';
import RunService from '../../../actions/run';
import plus from '../../../images/plus.svg';
import './projectInfo.css';


const ProjectInfo = (props) => {
    const [project, setProject] = useState({});
    const [runs, setRuns] = useState([]);

    const { projectId } = useParams();
    const history = useHistory();

    useEffect(() => {
        ProjectService.fetchProject(projectId)
            .then(response => {
                const projectData = {
                    name: response.data.name,
                    desc: response.data.description,
                    repo: response.data.repository
                };

                setProject(projectData);
            })
            .catch(error => {
                history.goBack();
            });
    }, [projectId, history]);

    useEffect(() => {
        RunService.fetchProjectRuns(projectId)
            .then(response => {
                setRuns(response.data);
            });
    }, [projectId]);

    const deleteRun = (projectId, runId) => {
        RunService.deleteRun(projectId, runId)
            .then(response => {
                setRuns(runs => runs.filter(run => run.id !== runId));
            });
    }

    const overview = {
        heading: project.name,
        desc: project.desc,
        items: [
            { key: "Owner", value: "N/A" },
            { key: "Ceated", value: "N/A" },
            { key: "Repository", value: <a href={project.repo}>{project.repo}</a> },
        ]
    }

    const runItems = runs.map(run => <RunItem
        projectId={projectId}
        deleteRun={() => deleteRun(projectId, run.id)}
        run={run} key={run.id} />)

    return (
        <div className="project-info">
            <OverviewPlane overview={overview} />
            <div className="project-info__runs">
                <div className="runs-info">
                    <div className="runs-info__header">
                        <div className="runs-info__header--heading">Runs</div>
                        <div className="runs-info__header--actions">
                            <Link to={`/${projectId}/run/new`}>
                                <span className="action"><ReactSVG src={plus} /></span>
                            </Link>
                        </div>
                    </div>
                    <div className="runs-info__content">
                        {
                            !Array.isArray(runItems) || !runItems.length
                                ? <NoRuns />
                                : runItems
                        }
                    </div>
                </div>
            </div>
        </div>
    );
}

export default ProjectInfo;