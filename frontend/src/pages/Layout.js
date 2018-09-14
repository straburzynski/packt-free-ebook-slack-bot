import React, {Component} from 'react';
import './layout.css'
import {Icon, Layout, Menu} from 'antd';
import AppRouter from "../routers/AppRouter";
import {NavLink} from "react-router-dom";

const {Header, Content, Footer} = Layout;
export default class AppLayout extends Component {

    render() {
        return (
            <Layout className="layout">
                <Header>
                    <span className="title">
                        Slackt <Icon type="right" theme="outlined" className="orange"/> Packt Slack Bot
                    </span>
                    <Menu
                        className="top-menu"
                        theme="dark"
                        mode="horizontal"
                        defaultSelectedKeys={['2']}
                    >
                        <Menu.Item key="job-list">
                            <NavLink to="/" activeClassName="active" exact={true}>
                                <Icon type="ordered-list" theme="outlined" /> All jobs
                            </NavLink>
                        </Menu.Item>
                        <Menu.Item key="job-create">
                            <NavLink to="/add" activeClassName="active" exact={true}>
                                <Icon type="plus" theme="outlined" /> Create job
                            </NavLink>
                        </Menu.Item>
                    </Menu>
                </Header>
                <Content className="content">
                        <AppRouter/>
                </Content>
                <Footer style={{textAlign: 'center'}}>
                    Slackt | Packt Slack Bot ©2018 <a href="http://github.com/straburzynski">Straburzyński</a>
                </Footer>
            </Layout>
        );
    }

}