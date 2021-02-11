import React from 'react';
import './profileSidebar.css';

const ProjectSidebar = (props) => {
    return (
        <div className="profile col-lg-3 col-md-4 col-12 pr-md-5 px-3 py-2 mt-md-3" style={{ backgroundColor: "white" }} >
            <div className="row justify-content-start">
                <div className="col-md-12 col-3">
                    <div className="profile-avatar">
                        <img alt="" width="260" height="260"
                            src="https://avatars1.githubusercontent.com/u/37289276" />
                    </div>
                </div>
                <div className="profile-info col-md-12 col-9 mt-md-2 ml-md-0 ml-n3 py-1">
                    <span className="profile-info__fullname d-block">{props.userDetails.name}</span>
                    <span className="profile-info__username d-block">{props.userDetails.username}</span>
                </div>
            </div>
        </div >
    );
}

export default ProjectSidebar;