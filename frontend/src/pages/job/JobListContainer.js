import * as React from "react";
import JobList from "../../components/JobList";

class JobListContainer extends React.Component {

    render() {
        return (
            <div className="job-list-container">
                <h3>Jobs:</h3>
                <JobList/>
            </div>
        )
    }

}

export default JobListContainer;