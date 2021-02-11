import React, { Component } from 'react';
import { withRouter } from "react-router";
import ProjectService from '../../../actions/project';

class CreateProject extends Component {
    constructor(params) {
        super(params);

        this.state = {
            name: '',
            description: '',
            repository: ''
        }
    }

    handleChange = (e) => {
        const target = e.target;
        const name = target.name;

        this.setState({
            [name]: target.value
        });
    }

    createProject = (e) => {
        e.preventDefault();

        ProjectService.createProject({
            ...this.state
        }).then(() => {
            this.props.history.push("/");
        });

    }

    render() {
        return (
            <div className="w-75 mx-auto mt-5" style={{ maxWidth: '740px' }}>
                <h3>Create project</h3>
                <hr />
                <form onSubmit={this.createProject}>
                    <div className="">
                        <div className="form-group">
                            <label htmlFor="name" className="small-font">Project name</label>
                            <input type="text" className="form-control col-md-4"
                                id="name"
                                name="name"
                                value={this.state.name}
                                onChange={this.handleChange} />
                        </div>
                        <div className="form-group">
                            <label htmlFor="description" className="small-font">Description <span className="text-muted">(optional)</span></label>
                            <input type="text" className="form-control"
                                id="description"
                                name="description"
                                value={this.state.description}
                                onChange={this.handleChange} />
                        </div>
                        <hr />
                        <div className="form-group">
                            <label htmlFor="repository" className="small-font">GitHub repository</label>
                            <input type="text" className="form-control"
                                id="repository"
                                name="repository"
                                value={this.state.repository}
                                onChange={this.handleChange} />
                        </div>
                        <hr />
                        <button type="submit" className="btn btn-primary">Create project</button>
                    </div>
                </form >
            </div >
        );
    }
}

export default withRouter(CreateProject);