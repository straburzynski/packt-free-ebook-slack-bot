import React from 'react';
import {Link} from 'react-router-dom';
import {Avatar, Button, List, Modal} from 'antd';
import {sendEbookToSlack} from '../service/SlackService';
import {deleteJob} from '../service/JobService';
import './job.css'

export class Job extends React.Component {

    getSchedulerTime = () => {
        const timeArray = this.props.scheduler.split(" ");
        return timeArray[2] + ":" + timeArray[1];
    };

    getBotName = () => {
        const botName = this.props.botName;
        if (botName) {
            return (
                <span>
                     <strong>, bot name: </strong> {botName}
                </span>
            )
        }
    };

    render() {
        let that = this;

        function showDeleteConfirm() {
            Modal.confirm({
                title: 'Are you sure delete this job?',
                content: 'This can\'t be undone',
                okText: 'Yes',
                okType: 'danger',
                cancelText: 'No',
                iconType: 'delete',
                iconClassName: 'red',
                onOk() {
                    deleteJob(that.props.id).then(() => {
                        that.props.onDeletedJob();
                    });
                }
            });
        }

        function showSendConfirm() {
            Modal.confirm({
                title: 'Are you sure send ebook?',
                content: 'Ebook will be send to configured slack workspace',
                okText: 'Yes',
                okType: 'primary',
                cancelText: 'No',
                iconType: 'right',
                iconClassName: 'blue',
                onOk() {
                    sendEbookToSlack(that.props.id);
                }
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
                    <Button type="danger" icon="delete" onClick={showDeleteConfirm}>Delete</Button>,
                    <Button type="primary" icon="play-circle" onClick={showSendConfirm}>Send now</Button>
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
                        <p>
                            <strong>sending time: </strong> {this.getSchedulerTime()} {this.getBotName()}
                        </p>
                    }
                />
            </List.Item>
        )
    }
}

export default Job;