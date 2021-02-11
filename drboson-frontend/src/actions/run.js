import axios_instance from '../axios/axios-config'

const RunService = {
    createRun: (projectId, run) => {
        return axios_instance.post('/run/create', run);
    },
    fetchProjectRuns: (projectId) => {
        return axios_instance.get('/run/project-runs', {
            params: {
                project_id: projectId
            }
        });
    },
    fetchProjectRun: (projectId, runId) => {
        return axios_instance.get(`/run/${runId}`);
    },
    fetchProjectRunMetrics: (projectId) => {
        return axios_instance.get('/run/project-logs', {
            params: {
                project_id: projectId
            }
        });
    },
    fetchRunMetrics: (projectId, runId) => {
        return axios_instance.get(`/run/${runId}/logs`);
    },
    fetchRunFiles: (projectId, runId) => {
        return axios_instance.get(`/run/${runId}/files`);
    },
    downloadRunFile: (projectId, runId, fileId) => {
        return axios_instance.get(`/run/${runId}/download-file`, {
            params: {
                file_id: fileId
            }
        });
    },
    deleteRun: (projectId, runId) => {
        return axios_instance.delete(`/run/${runId}`);
    }
};

export default RunService;