import React from 'react';
import 'antd/dist/antd.css';
import AppLayout from "./pages/Layout";
import {BrowserRouter} from "react-router-dom";

class App extends React.Component {

    render() {
        return (
            <BrowserRouter>
                <AppLayout/>
            </BrowserRouter>
        );
    }
}

export default App;
