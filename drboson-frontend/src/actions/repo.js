import axios_instance from '../axios/axios-config'
// import qs from 'qs';

const RepoService = {
    fetchOwnerRepos: (owner) => {
        return axios_instance.get('/repo', {
            params: { owner: owner }
        });
    },
};

export default RepoService;