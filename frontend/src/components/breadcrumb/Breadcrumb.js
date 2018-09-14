import {Breadcrumb} from "antd";
import * as React from "react";
import {Component} from "react";
import './breadcrumbs.css';

class CustomBreadcrumb extends Component {

    render() {
        return (
            <Breadcrumb className="breadcrumbs">
                {this.props.breadcrumbs.map((name, index) => {
                    return <Breadcrumb.Item key={index}>{name}</Breadcrumb.Item>
                })}
            </Breadcrumb>
        )
    }

}

export default CustomBreadcrumb