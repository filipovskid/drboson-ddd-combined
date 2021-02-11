import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { ReactSVG } from 'react-svg';
import { useEnqueueSnackbar } from '../../../Notification/useEnqueueSnackbar';
import DatasetService from '../../../../actions/dataset';
import Loading from '../../../Loading/loading';
import BarLoader from "react-spinners/BarLoader";
import trash from '../../../../images/trash-2.svg';
import arrow from '../../../../images/arrow-right.svg';
import './datasetCreator.css';

var classNames = require('classnames');

const DatasetCreator = (props) => {
    const [isUploading, setIsUploading] = useState(false);
    const { projectId } = useParams();
    const { id } = props;

    // Notification
    const enqueueSnackbar = useEnqueueSnackbar();

    const onFormSubmit = (e) => {
        e.preventDefault();
        if (isUploading) {
            console.log('Uploading...');
            return;
        }
        setIsUploading(true);

        const dataset = {
            "name": e.target.name.value,
            "file": props.file
        }

        uploadDataset(id, dataset);
    };

    const uploadDataset = (id, dataset) => {
        DatasetService.createDataset(projectId, dataset)
            .then(response => {
                props.onDatasetUpload(id, response.data);
                enqueueSnackbar('Dataset successfully created', { variant: 'success' });
            })
            .catch(error => {
                setIsUploading(false);
                enqueueSnackbar('Dataset creation failed', { variant: 'error' });
            });
    }

    const loading = (
        <Loading
            loading={isUploading}
            // loader={PropagateLoader}
            loader={BarLoader}
            color="#fdd703"
        />
    );

    const creatorClasses = classNames('dataset-creator', { 'disabled': isUploading });

    return (
        <form onSubmit={onFormSubmit} className="dataset-creator__wrapper">
            <div className={creatorClasses}>
                <div className="dataset-creator__item">
                    <div className="dataset-creator__item--header">Name</div>
                    <div className="dataset-creator__item--input">
                        <input name="name" type="text" defaultValue={props.filename} disabled={isUploading} />
                    </div>
                </div>

                <div className="dataset-creator__item">
                    <div className="dataset-creator__item--header">Type</div>
                    <div className="dataset-creator__item--input">
                        File Type
                </div>
                </div>

                <div className="dataset-creator__item">
                    <div className="dataset-creator__item--header">Date</div>
                    <div className="dataset-creator__item--input">
                        Current Date
                </div>
                </div>

                <div className="dataset-creator__item dataset-creator__actions">
                    <button className="btn dataset-creator--action submit">
                        <ReactSVG src={arrow} />
                    </button>
                    <button onClick={() => props.onComposerRemove(props.id)} className="btn dataset-creator--action" type="submit">
                        <ReactSVG src={trash} />
                    </button>
                </div>
                {loading}
            </div>
        </form>
    );
}

export default DatasetCreator;