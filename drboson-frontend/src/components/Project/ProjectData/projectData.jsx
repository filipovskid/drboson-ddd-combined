import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import DataUploader from './DataUploader/dataUploader';
import DatasetCreator from './DatasetCreator/datasetCreator';
import DatasetItem from './DatasetItem/datasetItem';
import NoDatasets from '../../NoData/NoDatasets/noDatasets';
import DatasetService from '../../../actions/dataset';
import RDFService from '../../../actions/rdf';
import './projectData.css';

const ProjectData = (props) => {

    const [datasets, setDatasets] = useState([]);
    const [dataComposers, setDataComposers] = useState([]);
    const { projectId } = useParams();

    useEffect(() => {
        DatasetService.fetchDatasets(projectId)
            .then(response => {
                setDatasets(response.data);
            });
    }, [projectId]); // dataComposers

    const createDatasetComposer = (file) => {
        const shortid = require('shortid');

        const datasetComposer = {
            "id": shortid.generate(),
            "file": file
        };

        setDataComposers(oldData => [datasetComposer, ...oldData]);
    };

    const removeDatasetComposer = (composerId) => {
        setDataComposers(oldData => oldData.filter(composer => composer.id !== composerId));
    };

    const onDatasetUpload = (composerId, data) => {
        removeDatasetComposer(composerId);
        setDatasets(datasets => [...datasets, data]);
    };

    const removeDataset = (datasetId) => {
        DatasetService.removeDataset(projectId, datasetId)
            .then(response => {
                setDatasets(datasets => datasets.filter(dataset => dataset.id !== datasetId));
            });
    };

    const downloadDataset = (datasetId, name) => {
        return DatasetService.downloadDataset(projectId, datasetId)
            .then(response => {
                var fileDownload = require('js-file-download');
                fileDownload(response.data, name);
            }).catch(error => { });
    }

    const storeRdfDataset = (datasetId) => {
        RDFService.storeRdfDataset(projectId, datasetId)
            .then(response => {
                console.log(response)
                setDatasets(datasets => datasets.map(dataset => {
                    if (dataset.id === datasetId)
                        return response.data;

                    return dataset;
                }));
            });
    }

    const storeCommonDataset = (datasetId) => {
        DatasetService.storeDataset(datasetId)
            .then(response => {
                console.log(response)
                setDatasets(datasets => datasets.map(dataset => {
                    if (dataset.id === datasetId)
                        return response.data;

                    return dataset;
                }));
            });
    }

    const composers = dataComposers.map(composer => {
        return <DatasetCreator
            onComposerRemove={removeDatasetComposer}
            onDatasetUpload={onDatasetUpload}
            id={composer.id}
            key={composer.id}
            file={composer.file}
            filename={composer.file.name}
        />;
    });

    const datasetItems = datasets.map(dataset => <DatasetItem onDatasetRemove={removeDataset}
        onRdfStore={storeRdfDataset}
        onCommonStore={storeCommonDataset}
        onDatasetDownload={downloadDataset}
        dataset={dataset}
        project_id={projectId}
        key={dataset.id} />);

    return (
        <div className="project-data">
            <div className="project-data__uploader">
                <DataUploader onFileAttach={createDatasetComposer} />
            </div>
            {
                (!Array.isArray(composers) || !composers.length) && (!Array.isArray(datasets) || !datasets.length)
                    ? <NoDatasets />
                    : (
                        <div className="project-data__content">

                            <div className="project-data__content--composers">
                                {composers}
                            </div>
                            <div className="project-data__content--datasets">
                                {datasetItems}
                            </div>
                        </div>
                    )
            }
        </div >
    );
}

export default ProjectData;