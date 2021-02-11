import React, { useState, useEffect } from 'react';
import { useParams, useHistory } from "react-router-dom";
import { useEnqueueSnackbar } from '../../Notification/useEnqueueSnackbar';
import Select from 'react-select';
import DatasetService from '../../../actions/dataset';
import ProjectService from '../../../actions/project';
import RunService from '../../../actions/run';

const CreateRun = (props) => {
    const [datasets, setDatasets] = useState([]);
    const [isCreating, setIsCreating] = useState(false);
    const { projectId } = useParams();
    const [project, setProject] = useState();
    const history = useHistory()

    // Notification
    const enqueueSnackbar = useEnqueueSnackbar();

    useEffect(() => {
        DatasetService.fetchDatasets(projectId)
            .then(response => {
                setDatasets(response.data);
            });

        ProjectService.fetchProject(projectId)
            .then(response => {
                setProject(response.data);
            })
            .catch(error => {
                history.push(`/${projectId}/info`);
            })
    }, [projectId]);

    const onRunCreate = (e) => {
        e.preventDefault();
        if (isCreating) {
            return;
        }
        setIsCreating(true);

        const { name, description, datasetId } = e.target;
        const run = {
            projectId: projectId, 
            name: name.value,
            description: description.value,
            datasetId: datasetId.value,
            repository: project.repository
        }

        RunService.createRun(projectId, run)
            .then(response => {
                history.push(`/${projectId}/info`);
                enqueueSnackbar('Run job successfully created', { variant: 'success' });
            })
            .catch(error => {
                enqueueSnackbar('Run creation failed', { variant: 'error' });
            })
            .then(() => {
                setIsCreating(false);
            });
    }

    const datasetOptions = datasets.map(dataset => { return { value: dataset.id, label: dataset.name } })

    return (
        <div className="w-75 mx-auto mt-5" style={{ maxWidth: '740px' }}>
            <h3>Create run</h3>
            <hr />
            <form onSubmit={onRunCreate}>
                <div className="">
                    <div className="form-group">
                        <label htmlFor="name" className="small-font">Name</label>
                        <input type="text" className="form-control col-md-4"
                            id="name"
                            name="name"
                        // value={this.state.name} 
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="description" className="small-font">Description<span className="text-muted">(optional)</span></label>
                        <input type="text" className="form-control"
                            id="description"
                            name="description"
                        // value={this.state.description}
                        />
                    </div>
                    <hr />

                    <div className="form-group">
                        <label htmlFor="datasetId" className="small-font">Choose dataset</label>
                        <Select options={datasetOptions} name="datasetId" />
                    </div>
                    <hr />
                    <button type="submit" className="btn btn-primary" disabled={isCreating}>Create run</button>
                </div>
            </form >
        </div >
    );

};

export default CreateRun;