import axios from 'axios';

const MASTER_URL = 'https://jewelrysalesystem-backend.onrender.com';

export const apost = async (path, data) => {
    const url = MASTER_URL + path;
    const token = localStorage.getItem('token');

    return axios.post(url, data, {
        headers: {
            'Content-Type': 'application/json',
            // 'Authorization': `Bearer ${token}`
        },
    })
        .then(response => {
            return response;
        })
        .catch(error => {
            throw error;
        });
};

export const apostfile = async (path, selectedFile, dataObject) => {
    const url = MASTER_URL + path;
    const token = localStorage.getItem('token');

    const formData = new FormData();
    formData.append('file', selectedFile);
    Object.entries(dataObject).forEach(([key, value]) => {
        formData.append(key, value);
    });

    return axios.post(url, formData, {
        headers: {
            'Authorization': `Bearer ${token}`
        },
    })
        .then(response => {
            return response
        })
        .catch(error => {
            throw error
        })
};

export const aget = async (path) => {
    const url = MASTER_URL + path;
    const token = localStorage.getItem('accessToken');

    return axios.get(url, {
        headers: {
            'Authorization': `Bearer ${token}`
        },
    })
        .then(response => {
            return response;
        })
        .catch(error => {
            throw error;
        });
};

export const adelete = async (path) => {
    const url = MASTER_URL + path;
    const token = localStorage.getItem('token');

    return axios.delete(url, {
        headers: {
            'Authorization': `Bearer ${token}`
        },
    })
        .then(response => {
            return response;
        })
        .catch(error => {
            throw error;
        });
};

export const aupdate = async (path, data) => {
    const url = MASTER_URL + path;
    const token = localStorage.getItem('token');

    return axios.put(url, data, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
    })
        .then(response => {
            return response;
        })
        .catch(error => {
            throw error;
        });
};