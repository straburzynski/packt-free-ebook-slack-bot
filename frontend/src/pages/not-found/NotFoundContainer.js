import * as React from "react";
import CustomBreadcrumb from "../../components/breadcrumb/Breadcrumb";

export default class NotFoundContainer extends React.Component {

    render() {
        return (
            <div>
                <CustomBreadcrumb breadcrumbs={['Home', 'Not Found']}/>
                <div className="container-card">
                    <h2>404 - Not found</h2>
                </div>
            </div>
        )
    }
}
