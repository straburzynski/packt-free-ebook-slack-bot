import * as React from "react";
import JobForm from "../../components/job/JobForm";
import {getJob} from '../../service/JobService';
import {openNotificationWithIcon} from "../../service/NotificationService";
import CustomBreadcrumb from "../../components/breadcrumb/Breadcrumb";

class JobEditContainer extends React.Component {

    state = {
        job: null
    };

    constructor(props) {
        super(props);
        getJob(this.props.match.params.id)
            .then(response => {
                this.setState({
                    job: response.data
                });
            })
            .catch(() => {
                openNotificationWithIcon('error', 'Error', 'Error loading jobs');
            })
    }

    render() {
        if (this.state.job == null) {
            return (
                <div>
                    <CustomBreadcrumb breadcrumbs={['Home', 'Edit job']}/>
                    <div className="container-card">
                        <h3>Job edit:</h3>
                        <p>id: {this.props.match.params.id}</p>
                        <JobForm/>
                    </div>
                </div>
            )
        } else {
            return (
                <div>
                    <CustomBreadcrumb breadcrumbs={['Home', 'Edit job']}/>
                    <div className="container-card">
                        <h3>Job edit:</h3>
                        <p>id: {this.props.match.params.id}</p>
                        <JobForm job={this.state.job}/>
                    </div>
                </div>
            )
        }
    }

}

export default JobEditContainer;