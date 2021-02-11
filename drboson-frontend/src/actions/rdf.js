import axios_instance from '../axios/axios-config'

const RDFService = {
    storeRdfDataset: (projectId, datasetId) => {
        return axios_instance.get('/rdf/store', {
            params: {
                project_id: projectId,
                dataset_id: datasetId
            }
        });
    },
    fetchRdfClasses: (projectId, datasetId) => {
        return axios_instance.get('/rdf/classes', {
            params: {
                project_id: projectId,
                dataset_id: datasetId
            }
        });
    },
    fetchRdfEdges: (projectId, datasetId) => {
        return axios_instance.get('/rdf/edges', {
            params: {
                project_id: projectId,
                dataset_id: datasetId
            }
        });
    },
    fetchRdfLiteralEdges: (projectId, datasetId) => {
        return axios_instance.get('/rdf/literal-edges', {
            params: {
                project_id: projectId,
                dataset_id: datasetId
            }
        });
    },
    transformDataset: (data) => {
        return axios_instance.post('/rdf/transform', data);
    }
}

export default RDFService;