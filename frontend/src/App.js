import React, {Component} from 'react';
import logo from './logo.svg';
import './App.css';
import axios from "axios";

class App extends Component {

    constructor(props) {
        super(props);
        this.state = {
            book: {},
        };
    }

    componentDidMount() {
        this.getPacktBook();
    }

    getPacktBook = () => {
        console.log('start request');
        axios.get('/packt/today-ebook')
            .then(response => {
                console.log(response.data);
                this.setState({
                    book: response.data
                });
            })
            .catch(error => {
                console.log(error);
            })
            .then(() => {
                console.log('end request')
            })
    };

    render() {
        return (
            <div className="App">
                <header className="App-header">
                    <img src={logo} className="App-logo" alt="logo"/>
                    <h1 className="App-title">Packt Free Ebook</h1>
                </header>
                <p>{this.state.book.title}</p>
                <p>{this.state.book.description}</p>
                <a href='https://www.packtpub.com/packt/offers/free-learning'>
                    <img src={this.state.book.imageUrl} alt={this.state.book.title}/>
                </a>
            </div>
        );
    }
}

export default App;
