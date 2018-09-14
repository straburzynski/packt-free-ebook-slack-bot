import React from 'react';
import {getEbook} from '../../service/PacktService';
import {Button, Card, Col, Icon, Row} from 'antd';
import {openNotificationWithIcon} from "../../service/NotificationService";

export class Ebook extends React.Component {

    state = {
        ebook: null,
    };

    doEbookRequest = () => {
        getEbook()
            .then((res) => {
                this.setState({
                    ebook: res.data
                });
            })
            .catch(error => {
                const errorMessage = error.response.data.message ? ': ' + error.response.data.message : '';
                openNotificationWithIcon('error', 'Error', 'Error creating job: ' + errorMessage);
            })
    };

    componentDidMount() {
        this.doEbookRequest();
    }

    render() {
        if (this.state.ebook == null) {
            return (
                <div className="text-center">
                    <Icon type="loading" theme="outlined" className="custom-loader"/>
                </div>
            )
        } else
            return (
                <div>
                    <Row>
                        <Col xs={24} sm={24} md={12} lg={8} xl={6} className="text-center">
                            <img src={this.state.ebook.imageUrl} alt={this.state.ebook.title}/>
                        </Col>
                        <Col xs={24} sm={24} md={12} lg={16} xl={18}>
                            <Card title={this.state.ebook.title} bordered={false} className="w-100">
                                <p className="text-justify">{this.state.ebook.description}</p>
                                <div className="float-right">
                                    <Button href={this.state.ebook.bookUrl} type="dashed" className="m-15">
                                        More details
                                    </Button>
                                    <Button href="https://www.packtpub.com/packt/offers/free-learning">
                                        Download
                                    </Button>
                                </div>
                            </Card>
                        </Col>
                    </Row>
                </div>
            )
    }
}

export default Ebook;