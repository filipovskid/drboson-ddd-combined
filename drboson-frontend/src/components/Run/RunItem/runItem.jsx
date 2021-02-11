import React from 'react';
import { ReactSVG } from 'react-svg';
import { Link, useHistory } from "react-router-dom";
import RunStatus from '../RunStatus/runStatus';
import trash from '../../../images/trash-2.svg';
import terminal from '../../../images/terminal.svg';
import folder from '../../../images/folder.svg';
import './runItem.css';

const RunItem = (props) => {
    const { run, projectId } = props;
    const history = useHistory();

    const redirectTo = (destination) => {
        history.push(`/${projectId}/run/${run.id}/${destination}`);
    }

    return (
        <div className="run-container">
            <div className="run">
                <div className="run__info-item">
                    <div className="run__info-item--heading">Name</div>
                    <div className="run__info-item--info active">
                        <Link style={{ textDecoration: 'none', color: 'inherit' }} to={`/${projectId}/run/${run.id}/info`}>
                            {run.name}
                        </Link>
                    </div>
                </div>

                <div className="run__info-item">
                    <div className="run__info-item--heading">Status</div>
                    <div className="run__info-item--info"><RunStatus status={run.status} /></div>
                </div>

                <div className="run__info-item">
                    <div className="run__info-item--heading">Created</div>
                    <div className="run__info-item--info">N/A</div>
                </div>

                <div className="run__info-item run__actions">
                    <button onClick={props.deleteRun} className="btn run--action" type="submit">
                        <ReactSVG src={trash} />
                    </button>
                    <button onClick={() => redirectTo('files')} className="btn run--action">
                        <ReactSVG src={folder} />
                    </button>
                    <button onClick={() => redirectTo('logs')} className="btn run--action">
                        <ReactSVG src={terminal} />
                    </button>
                </div>
            </div>
        </div>
    );
}

export default RunItem;