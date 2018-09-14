import * as React from "react";
import JobForm from "../../components/job/JobForm";

class JobAddContainer extends React.Component {

    render() {
        return (
            <div className="container-card">
                <h3>Add new job:</h3>
                <JobForm/>
            </div>
        )
    }

}

export default JobAddContainer;