import React from 'react';
import {Link} from 'react-router-dom';
import {Avatar, Button, List, Modal} from 'antd';
import Moment from "react-moment";
import moment from 'moment';
import './job.css'

export class Job extends React.Component {

    startDate = () => {
        return <Moment format="DD.MM.YYYY HH:mm">{this.props.startDate}</Moment>
    };

    endDate = () => {
        const isDateValid = moment(this.props.endDate).isValid();
        return isDateValid ? <Moment format="DD.MM.YYYY HH:mm">{this.props.startDate}</Moment> : "No end date"
    };

    getSchedulerTime = () => {
        const timeArray = this.props.scheduler.split(" ");
        return timeArray[2] + ":" + timeArray[1];
    };

    render() {

        let that = this;

        function showDeleteConfirm() {
            Modal.confirm({
                title: 'Are you sure delete this job?',
                content: 'Some descriptions',
                okText: 'Yes',
                okType: 'danger',
                cancelText: 'No',
                iconType: 'delete',
                iconClassName: 'red',
                onOk() {
                    console.log('OK', that.props.id);
                },
                onCancel() {
                    console.log('Cancel');
                },
            });
        }

        return (
            <List.Item
                className='job'
                key={this.props.id}
                actions={[
                    <Link to={`/job/${this.props.id}`}>
                        <Button type="default" icon="edit">Edit</Button>
                    </Link>,
                        <Button type="danger" icon="delete" onClick={showDeleteConfirm}>Delete</Button>


                ]}
            >
                <List.Item.Meta
                    avatar={
                        <Avatar className={this.props.active ? 'active-job' : 'inactive-job'}></Avatar>
                    }
                    title={
                        <Link to={`/job/${this.props.id}`}>
                            <h3><strong>{this.props.jobName}</strong> (id: {this.props.id})</h3>
                        </Link>
                    }
                    description={
                        <div>
                            <p>
                                <strong>Time: </strong> {this.getSchedulerTime()}, <strong>Date from: </strong>
                                {this.startDate()}, <strong>Date to: </strong> {this.endDate()}, <strong>Bot name:
                                </strong> {this.props.botName}
                            </p>
                        </div>
                    }
                />
            </List.Item>
        )
    }
}


export default Job;