import * as React from "react";
import Job from "./Job";
import axios from "axios";
import {List} from "antd";

class JobList extends React.Component {

    getJobList = () => {
        console.log('start request');
        axios.get('/jobs')
            .then(response => {
                console.log(response.data);
                this.setState({
                    jobs: response.data
                });
            })
            .catch(error => {
                console.log(error);
            })
            .then(() => {
                console.log('end request')
            })
    };

    constructor(props) {
        super(props);
        this.state = {
            jobs: []
        }
    }

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
                        <Job {...job}/>
                    )}
                />
            </div>
        )
    }

}

export default JobList;