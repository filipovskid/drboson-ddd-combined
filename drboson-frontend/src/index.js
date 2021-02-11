import React from 'react';
import ReactDOM from 'react-dom';
import { SnackbarProvider } from 'notistack';
import './index.css';
import 'bootstrap';
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap/dist/js/bootstrap.js';
import '../node_modules/react-vis/dist/style.css';
// import $ from 'jquery';
// import Popper from 'popper.js';
import App from './components/App/App';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter } from 'react-router-dom';
import './components/Notification/notification.css';

ReactDOM.render(
    <SnackbarProvider
        maxSnack={2}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
        // preventDuplicate
        dense
    // className='notification'
    // content={(key, message) => (
    //     <Notification id={key} message={message} />
    // )}
    >
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </SnackbarProvider>
    , document.getElementById('root'));

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
