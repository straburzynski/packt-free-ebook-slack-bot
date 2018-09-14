import * as React from "react";
import {Button, Form, Input, Switch, TimePicker} from 'antd';
import './jobForm.css';
import moment from 'moment';
import 'moment-timezone';
import {addJob, editJob} from "../service/JobService";

function hasErrors(fieldsError) {
    return Object.keys(fieldsError).some(field => fieldsError[field]);
}

class JobForm extends React.Component {

    handleSubmit = (e) => {
        e.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                values = this.prepareToSave(values);
                if (this.props.job) {
                    editJob(this.props.job.id, values);
                } else {
                    addJob(values).then(res => {
                        if (res) {
                            this.props.form.resetFields();
                        }
                    });
                }
            }
        });
    };

    prepareToSave(values) {
        const hour = values.time.hour();
        const minute = values.time.minute();
        values.scheduler = `0 ${minute} ${hour} ? * * *`;
        delete values.time;
        values.active = !!values.active;
        return values;
    }

    componentDidMount() {
        this.props.form.validateFields();
    }

    render() {

        const formItemLayout = {
            labelCol: {
                xs: {span: 24},
                sm: {span: 24},
                md: {span: 6},
                lg: {span: 6},
                xl: {span: 6},
            },
            wrapperCol: {
                xs: {span: 24},
                sm: {span: 24},
                md: {span: 18},
                lg: {span: 16},
                xl: {span: 12},
            },
        };

        const formItemBtn = {
            labelCol: {span: 4},
            wrapperCol: {span: 8, offset: 4},
        };

        const {getFieldDecorator, getFieldsError, getFieldError, isFieldTouched} = this.props.form;

        const jobNameError = isFieldTouched('jobName') && getFieldError('jobName');
        const webhookError = isFieldTouched('webhook') && getFieldError('webhook');
        const timeError = isFieldTouched('time') && getFieldError('time');

        return (
            <div className="job">
                <Form layout="vertical" onSubmit={this.handleSubmit}>
                    <Form.Item
                        label="Job name"
                        {...formItemLayout}
                        validateStatus={jobNameError ? 'error' : ''}
                        help={jobNameError || ''}
                    >
                        {getFieldDecorator('jobName', {
                            rules: [{required: true, message: 'Please input job name'}],
                        })(
                            <Input placeholder="Job name"/>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Sending time"
                        {...formItemLayout}
                        validateStatus={timeError ? 'error' : ''}
                        help={timeError || ''}>
                        {getFieldDecorator('time', {
                            rules: [
                                {type: 'object', required: true, message: 'Please select sending time'}],
                        })(
                            <TimePicker format="HH:mm"/>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Webhook URL"
                        {...formItemLayout}
                        validateStatus={webhookError ? 'error' : ''}
                        help={webhookError || ''}
                    >
                        {getFieldDecorator('webhook', {
                            rules: [{required: true, message: 'Please slack webhook URL'}],
                        })(
                            <Input placeholder="Slack webhook URL"/>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Active"
                        {...formItemLayout}
                    >
                        {getFieldDecorator('active', {valuePropName: 'checked'})(
                            <Switch/>
                        )}
                    </Form.Item>
                    <Form.Item
                        label="Bot Name"
                        {...formItemLayout}>
                        {getFieldDecorator('botName', {})(
                            <Input placeholder="Slack bot name (optional)"/>
                        )}
                    </Form.Item>
                    <Form.Item
                        {...formItemBtn}
                    >
                        <Button type="primary" htmlType="submit" disabled={hasErrors(getFieldsError())}>
                            {this.props.job ? 'Save' : 'Create'}
                        </Button>
                    </Form.Item>
                </Form>
            </div>
        )
    }

}

export default Form.create(
    {
        mapPropsToFields(props) {
            if (props.job != null) {
                const time = moment(props.job.scheduler.split(" ")[2] + ":" + props.job.scheduler.split(" ")[1], 'HH:mm');
                return {
                    jobName: Form.createFormField({...props.jobName, value: props.job.jobName}),
                    time: Form.createFormField({...props.scheduler, value: time}),
                    webhook: Form.createFormField({...props.webhook, value: props.job.webhook}),
                    active: Form.createFormField({...props.active, value: props.job.active}),
                    botName: Form.createFormField({...props.botName, value: props.job.botName}),
                };
            }
        }
    }
)(JobForm);