import * as React from "react";
import JobForm from "../../components/JobForm";
import {getJob} from '../../service/JobService';

class JobEditContainer extends React.Component {

    state = {
        job: null
    };

    constructor(props) {
        super(props);
        getJob(this.props.match.params.id).then(response => {
            console.log(response.data);
            this.setState({
                job: response.data
            });
        })
            .catch(error => {
                console.log(error);
            })
            .then(() => {
                console.log('end request')
            });
    }

    render() {
        if (this.state.job == null) {
            return (
                <div className="job-edit-container">
                    <h3>Job edit:</h3>
                    <p>id: {this.props.match.params.id}</p>
                    <JobForm />
                </div>
            )
        } else {
            return (
                <div className="job-edit-container">
                    <h3>Job edit:</h3>
                    <p>id: {this.props.match.params.id}</p>
                    <JobForm job={this.state.job}/>
                </div>
            )
        }
    }

}

export default JobEditContainer;