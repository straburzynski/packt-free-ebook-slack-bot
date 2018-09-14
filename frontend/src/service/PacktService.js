import axios from "axios";

export function getEbook() {
    return axios.get('/packt/today-ebook');
}
