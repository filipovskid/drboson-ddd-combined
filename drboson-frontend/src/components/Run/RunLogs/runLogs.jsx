import React from 'react';
import './runLogs.css';

const RunLogs = (props) => {
    return (
        <div className="run-logs">
            b'&#123;"run_id": "b1e34d8f-2fdf-4d87-a143-88f2c0032e0b", "project_id": "a2ed48b4-d824-4046-b657-ba93df290d44", "type": "log", "payload": &#123;"step": 1, "random": 0.19333206224147115, "additional": 0.9602595888657252, "_step": 8&#125;&#125;'
            % Message delivered to run_logs [0] @ 37 <br />
            b'&#123;"run_id": "b1e34d8f-2fdf-4d87-a143-88f2c0032e0b", "project_id": "a2ed48b4-d824-4046-b657-ba93df290d44", "type": "log", "payload": &#123;"step": 1, "random": 0.061323139279181205, "additional": 0.6904132939007596, "_step": 9&#125;&#125;'
            % Message delivered to run_logs [0] @ 38 <br />
            b'&#123;"run_id": "b1e34d8f-2fdf-4d87-a143-88f2c0032e0b", "project_id": "a2ed48b4-d824-4046-b657-ba93df290d44", "type": "file", "payload": "/drboson/workdir/something.txt"&#125;'
            % Message delivered to run_files [0] @ 3 <br />
        b'&#123;"run_id": "b1e34d8f-2fdf-4d87-a143-88f2c0032e0b", "project_id": "a2ed48b4-d824-4046-b657-ba93df290d44", "type": "status", "payload": "completed"&#125;'
            % Message delivered to run_statuses[0] @6 <br />
        </div >
    );
}

export default RunLogs;