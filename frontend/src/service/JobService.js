import axios from "axios";
import {openNotificationWithIcon} from "./NotificationService";

export function getJob(id) {
    return axios.get('/jobs/' + id);
}

export function addJob(job) {
    return axios.post('/jobs', job)
        .then(() => {
            openNotificationWithIcon('success', 'OK', 'Job created successfully');
            return true;
        })
        .catch(error => {
            const errorMessage = error.response.data.message ? ': ' + error.response.data.message : '';
            openNotificationWithIcon('error', 'Error', 'Error creating job' + errorMessage);
            return false;
        })
}

export function editJob(id, job) {
    return axios.put(`/jobs/${id}`, job)
        .then(() => {
            openNotificationWithIcon('success', 'OK', 'Job updated successfully');
        })
        .catch(error => {
            const errorMessage = error.response.data.message ? ': ' + error.response.data.message : '';
            openNotificationWithIcon('error', 'Error', 'Error updating job' + errorMessage);
        })
}

export function deleteJob(id) {
    return axios.delete(`/jobs/${id}`)
        .then(() => {
            openNotificationWithIcon('success', 'OK', 'Job deleted successfully');
        })
        .catch(error => {
            const errorMessage = error.response.data.message ? ': ' + error.response.data.message : '';
            openNotificationWithIcon('error', 'Error', 'Error deleting job' + errorMessage);
        })
}
