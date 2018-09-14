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
                openNotificationWithIcon('error', 'Error', 'Error creating job' + errorMessage);
            })
    };

    componentDidMount() {
        this.doEbookRequest();
    }

    render() {
        if (this.state.ebook == null) {
            return (
                <div style={{textAlign: "center"}}>
                    <Icon type="loading" theme="outlined" style={{fontSize: '36px', color: '#08c'}}/>
                </div>
            )
        } else
            return (
                <div>
                    <Row>
                        <Col xs={24} sm={24} md={12} lg={6} xl={6} style={{textAlign: 'center'}}>
                            <img src={this.state.ebook.imageUrl}/>
                        </Col>
                        <Col  xs={24} sm={24} md={12} lg={16} xl={16} >
                            <Card title={this.state.ebook.title} bordered={false} style={{width: '100%'}}>
                                <p>{this.state.ebook.description}</p>
                                <div style={{float: "right"}}>
                                    <Button href={this.state.ebook.bookUrl} type="dashed" style={{marginRight: "15px"}}>
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