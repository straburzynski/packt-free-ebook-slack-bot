import React, {Component} from 'react';
import {Route, Switch} from 'react-router-dom';
import JobAddContainer from "../pages/job/JobAddContainer";
import JobListContainer from "../pages/job/JobListContainer";
import JobEditContainer from "../pages/job/JobEditContainer";
import NotFound from "../pages/not-found/NotFound";

export default class AppRouter extends Component {

    render() {
        return (
            <Switch>
                <Route path="/" component={JobListContainer} exact={true}/>
                <Route path="/job/:id" component={JobEditContainer}/>
                <Route path="/job" component={JobListContainer}/>
                <Route path="/add" component={JobAddContainer}/>
                <Route component={NotFound}/>
            </Switch>
        )
    }

}