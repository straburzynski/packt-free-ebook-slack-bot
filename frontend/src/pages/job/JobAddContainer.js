import * as React from "react";
import JobForm from "../../components/JobForm";

class JobAddContainer extends React.Component {

    render() {
        return (
            <div className="job-add-container">
                <h3>Add new job:</h3>
                <JobForm/>
            </div>
        )
    }

}

export default JobAddContainer;