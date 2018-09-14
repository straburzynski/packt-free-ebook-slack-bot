import * as React from "react";
import JobList from "../../components/job/JobList";
import Ebook from "../../components/ebook/Ebook";
import CustomBreadcrumb from "../../components/breadcrumb/Breadcrumb";

class JobListContainer extends React.Component {

    render() {
        return (
            <div>
                <CustomBreadcrumb breadcrumbs={['Home', 'All jobs']}/>
                <div className="container-card">
                    <Ebook/>
                </div>
                <div className="container-card">
                    <h3>Jobs:</h3>
                    <JobList/>
                </div>
            </div>
        )
    }

}

export default JobListContainer;