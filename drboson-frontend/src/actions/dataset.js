import axios_instance from '../axios/axios-config'
// import qs from 'qs';

const DatasetService = {
    fetchDatasets: (projectId) => {
        return axios_instance.get("/dataset/project-datasets", {
            params: {
                project_id: projectId
            }
        });
    },
    createDataset: (projectId, dataset) => {
        const data = new FormData();
        data.set('name', dataset.name);
        data.set('projectId', projectId);
        data.set('file', dataset.file);

        return axios_instance.post('/dataset/create', data, {
            headers: { 'Content-Type': 'multipart/form-data' }
        });
    },
    removeDataset: (projectId, datasetId) => {
        return axios_instance.delete(`/dataset/${datasetId}`);
    },
    downloadDataset: (projectId, datasetId, fileName) => {
        return axios_instance.get(`/dataset/${datasetId}/download`);
    },
    storeDataset: (datasetId) => {
        return axios_instance.get(`/dataset/${datasetId}/local-storage`);
    },
    refreshColumns: (datasetId) => {
        return axios_instance.get(`/dataset/${datasetId}/refresh-columns`);
    }
}

export default DatasetService;