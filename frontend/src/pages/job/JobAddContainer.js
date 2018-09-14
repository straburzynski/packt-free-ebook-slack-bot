import * as React from "react";
import JobForm from "../../components/job/JobForm";
import CustomBreadcrumb from "../../components/breadcrumb/Breadcrumb";

class JobAddContainer extends React.Component {

    render() {
        return (
            <div>
                <CustomBreadcrumb breadcrumbs={['Home', 'Add new job']}/>
                <div className="container-card">
                    <h3>Add new job:</h3>
                    <JobForm/>
                </div>
            </div>
        )
    }

}

export default JobAddContainer;