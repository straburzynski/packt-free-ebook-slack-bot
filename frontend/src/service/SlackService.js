import axios from "axios";
import {openNotificationWithIcon} from "./NotificationService";

export function sendEbookToSlack(jobId) {
    axios.post(`/packt/send-to-slack/${jobId}`, {})
        .then(() => {
            openNotificationWithIcon('success', 'OK', 'Job created successfully');
        })
        .catch(error => {
            const errorMessage = error.response.data.message ? ': ' + error.response.data.message : '';
            openNotificationWithIcon('error', 'Error', 'Error sending ebook to slack' + errorMessage);
        })
}
