import React, {Component} from 'react';
import './layout.css'
import {Layout, Menu} from 'antd';
import AppRouter from "../routers/AppRouter";
import {NavLink} from "react-router-dom";

const {Header, Content, Footer} = Layout;
export default class AppLayout extends Component {

    render() {
        return (
            <Layout className="layout">
                <Header>
                    <div className="logo"/>
                    <span className="title">Packt Slack Bot</span>
                    <Menu
                        className="top-menu"
                        theme="dark"
                        mode="horizontal"
                        defaultSelectedKeys={['2']}
                    >
                        <Menu.Item key="job-list">
                            <NavLink to="/" activeClassName="active" exact={true}>All jobs</NavLink>
                        </Menu.Item>
                        <Menu.Item key="job-create">
                            <NavLink to="/add" activeClassName="active" exact={true}>Create job</NavLink>
                        </Menu.Item>
                    </Menu>
                </Header>
                <Content className="content">
                    <div style={{background: '#fff', padding: 24, minHeight: 280}}>

                        <AppRouter/>

                    </div>
                </Content>
                <Footer style={{textAlign: 'center'}}>
                    Packt Slack Bot ©2018 <a href="http://github.com/straburzynski">Straburzyński</a>
                </Footer>
            </Layout>
        );
    }

}