import axios_instance from '../axios/axios-config'

const ProjectService = {
    fetchProjects: () => {
        return axios_instance.get('/project');
    },
    fetchProject: (projectId) => {
        return axios_instance.get(`/project/${projectId}`);
    },
    createProject: (project) => {
        return axios_instance.post('/project/create', project);
    }
};

export default ProjectService;