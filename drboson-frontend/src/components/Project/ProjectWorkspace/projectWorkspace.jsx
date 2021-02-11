import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ReactSVG } from 'react-svg';
import VisPlane from '../../Visualization/VisPlane/visPlane';
import VisBuilder from '../../Visualization/VisBuilder/visBuilder';
import NoVisualization from '../../NoData/NoVisualization/noVisualization';
import RunService from '../../../actions/run';
import plus from '../../../images/plus.svg';
import './projectWorkspace.css';
import shortid from 'shortid';

const ProjectWorkspace = (props) => {
    const [runLogs, setRunLogs] = useState([]);
    const [previews, setPreviews] = useState([]);
    const [isBuilderOpen, setIsBuilderOpen] = useState(false);
    const { projectId } = useParams();

    useEffect(() => {
        RunService.fetchProjectRunMetrics(projectId)
            .then(response => {
                setRunLogs(response.data.logs);
            });
    }, [projectId]);

    const openBuilder = () => {
        setIsBuilderOpen(true);
    }

    const closeBuilder = () => {
        setIsBuilderOpen(false);
    }

    const createVisualization = (preview) => {
        console.log(preview);
        const newPreview = {
            id: shortid.generate(),
            title: preview.title,
            plot: preview.plot,
        }

        setPreviews(previousPreviews => [...previousPreviews, newPreview]);
        closeBuilder();
    }

    return (
        <div className="workspace">
            <div className="workspace__header">
                <div className="workspace__header--heading">
                    <h5>Visuailization</h5>
                </div>
                <div className="workspace__header--actions">
                    <span onClick={openBuilder} className="action"><ReactSVG src={plus} /></span>
                </div>
            </div>
            <div className="workspace__content">
                {
                    !Array.isArray(previews) || !previews.length
                        ? <NoVisualization />
                        : <VisPlane previews={previews} />
                }
                <VisBuilder
                    isOpen={isBuilderOpen}
                    runLogs={runLogs}
                    onVisualizationCreate={createVisualization}
                    closeBuilder={closeBuilder}
                    key={isBuilderOpen}
                />
            </div>
        </div>
    );
}

export default ProjectWorkspace;