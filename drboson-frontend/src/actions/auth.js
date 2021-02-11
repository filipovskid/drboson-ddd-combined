import axios_instance from '../axios/axios-config'

const AuthenticationService = {
    registerUser: (userData) => {
        return axios_instance.post('/user/join', userData);
    },
    loginUser: (userCredentials) => {
        return axios_instance.post('/auth/login', userCredentials);
    },
    checkLoginStatus: () => {
        return axios_instance.get('/user/me')
    }
};

export default AuthenticationService;