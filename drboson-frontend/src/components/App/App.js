import React, { Component } from 'react';
import {
  BrowserRouter as Router, Route
} from "react-router-dom";
// import './App.css';
import '../../styles/main.css';
import Header from '../Header/header';
import Registration from '../Auth/Registration'
import Login from '../Auth/Login'
import AuthenticationService from '../../actions/auth';
import ProtectedRoute from '../ProtectedRoute/ProtectedRoute';
import HomePage from '../Page/HomePage/homePage';
import CreateProject from '../Projects/CreateProject/CreateProject';
import ProjectPage from '../Project/ProjectPage/projectPage';
import ProjectData from '../Project/ProjectData/projectData';
import ProjectInfo from '../Project/ProjectInfo/projectInfo';
import ProjectWorkspace from '../Project/ProjectWorkspace/projectWorkspace';
import CreateRun from '../Run/CreateRun/createRun';
import RunPage from '../Run/RunPage/runPage';
import RunInfo from '../Run/RunInfo/runInfo';
import RunWorkspace from '../Run/RunWorkspace/runWorkspace';
import RunFiles from '../Run/RunFiles/runFiles';
import RunLogs from '../Run/RunLogs/runLogs';
import RDFTransform from '../Dataset/RDFTransform/rdfTransform';
import DataExplorer from '../Dataset/DataExporer/dataExplorer';

class App extends Component {

  constructor(props) {
    super(props);

    this.state = {
      isAuthenticated: null,
      userDetails: {}
    }
  }

  componentDidMount() {
    this.checkLoginStatus();
  }

  onUserLogin = () => {
    return this.checkLoginStatus();
  }

  checkLoginStatus = () => {
    return AuthenticationService.checkLoginStatus()
      .then(response => {
        this.setState({
          isAuthenticated: true,
          userDetails: response.data
        });
      }).catch(error => {
        this.setState({
          isAuthenticated: false,
          userDetails: {}
        });
      });
  }

  render() {
    if (this.state.isAuthenticated == null) {
      return null;
    }

    return (
      <Router>
        <Header isAuthenticated={this.state.isAuthenticated} />
        <div className='container-xl'>
          <Route exact path='/join'>
            <Registration isAuthenticated={this.state.isAuthenticated} />
          </Route>
          <Route exact path='/login'>
            <Login isAuthenticated={this.state.isAuthenticated} onUserLogin={this.onUserLogin} />
          </Route>
          <ProtectedRoute exact path='/' isAuthenticated={this.state.isAuthenticated}>
            <HomePage userDetails={this.state.userDetails} />
          </ProtectedRoute>
          <ProtectedRoute exact path='/new' isAuthenticated={this.state.isAuthenticated}>
            <CreateProject />
          </ProtectedRoute>
          <ProtectedRoute exact path='/:projectId/run/new' isAuthenticated={this.state.isAuthenticated}>
            <CreateRun />
          </ProtectedRoute>
        </div>

        <ProtectedRoute exact path='/:projectId/info' isAuthenticated={this.state.isAuthenticated}>
          <ProjectPage component={ProjectInfo} />
        </ProtectedRoute>

        <ProtectedRoute exact path='/:projectId/workspace' isAuthenticated={this.state.isAuthenticated}>
          <ProjectPage component={ProjectWorkspace} />
        </ProtectedRoute>

        <ProtectedRoute exact path='/:projectId/data' isAuthenticated={this.state.isAuthenticated}>
          <ProjectPage component={ProjectData} />
        </ProtectedRoute>

        <ProtectedRoute exact
          path='/projects/:projectId/datasets/:datasetId/rdf'
          isAuthenticated={this.state.isAuthenticated}
        >
          <ProjectPage component={RDFTransform} />
          {/* <RDFTransform /> */}
        </ProtectedRoute>

        <Route path='/test'>
          <ProjectPage component={RDFTransform} />
        </Route>

        <ProtectedRoute path='/projects/:projectId/datasets/:datasetId/processing' isAuthenticated={this.state.isAuthenticated}>
          <ProjectPage component={DataExplorer} />
        </ProtectedRoute>

        <ProtectedRoute exact path='/:projectId/run/:runId/info' isAuthenticated={this.state.isAuthenticated}>
          <RunPage component={RunInfo} />
        </ProtectedRoute>

        <ProtectedRoute exact path='/:projectId/run/:runId/workspace' isAuthenticated={this.state.isAuthenticated}>
          <RunPage component={RunWorkspace} />
        </ProtectedRoute>

        <ProtectedRoute exact path='/:projectId/run/:runId/files' isAuthenticated={this.state.isAuthenticated}>
          <RunPage component={RunFiles} />
        </ProtectedRoute>

        <ProtectedRoute exact path='/:projectId/run/:runId/logs' isAuthenticated={this.state.isAuthenticated}>
          <RunPage component={RunLogs} />
        </ProtectedRoute>
      </Router >
    );
  }
}

export default App;
