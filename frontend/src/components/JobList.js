import * as React from "react";
import Job from "./Job";
import axios from "axios";
import {List} from "antd";
import {openNotificationWithIcon} from "../service/NotificationService";

class JobList extends React.Component {

    getJobList = () => {
        axios.get('/jobs')
            .then(response => {
                this.setState({
                    jobs: response.data
                });
            })
            .catch(() => {
                openNotificationWithIcon('error', 'Error', 'Error loading jobs');
            })
    };

    constructor(props) {
        super(props);
        this.state = {
            jobs: []
        }
    }

    handleRefresh = () => {
        this.getJobList();
    };

    componentDidMount() {
        this.getJobList();
    }

    render() {
        return (
            <div>
                <List
                    itemLayout="horizontal"
                    dataSource={this.state.jobs}
                    renderItem={job => (
                        <Job {...job} onDeletedJob={this.handleRefresh}/>
                    )}
                />
            </div>
        )
    }

}

export default JobList;

