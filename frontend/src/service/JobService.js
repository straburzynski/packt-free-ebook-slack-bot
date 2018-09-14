import axios from "axios";
import {openNotificationWithIcon} from "./NotificationService";

export function getJob(id) {
    console.log('start request');
    return axios.get('/jobs/' + id);
}

export function addJob(job) {
    axios.post('/jobs', job)
        .then(response => {
            console.log(response);
            openNotificationWithIcon('success', 'OK', 'Job saved successfully');
            //todo: redirect to job edit page
        })
        .catch(error => {
            const errorMessage = error.response.data.message ? ': ' + error.response.data.message : '';
            openNotificationWithIcon('error', 'Error', 'Error saving job' + errorMessage);
        })
}