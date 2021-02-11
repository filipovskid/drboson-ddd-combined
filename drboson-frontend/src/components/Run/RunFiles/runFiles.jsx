import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";
import { ReactSVG } from 'react-svg';
import NoFiles from '../../NoData/NoFiles/noFiles';
import RunService from '../../../actions/run';
import download from '../../../images/download.svg';
import fileIcon from '../../../images/file-text.svg';
import './runFiles.css';

const RunFiles = (props) => {
    const [files, setFiles] = useState([]);
    const { projectId, runId } = useParams();

    useEffect(() => {
        RunService.fetchRunFiles(projectId, runId)
            .then(response => {
                setFiles(response.data);
                console.log(response.data)
            });
    }, [projectId, runId]);

    const downloadFile = (fileId, fileName) => {
        RunService.downloadRunFile(projectId, runId, fileId)
            .then(response => {
                var fileDownload = require('js-file-download');
                fileDownload(response.data, fileName);
            });
    }

    const fileTableItems = files.map(file => (
        <tr key={file.id}>
            <th scope="row">
                <div className="table-icon"><ReactSVG src={fileIcon} /></div>
            </th>
            <td>{file.name}</td>
            <td>N/A</td>
            <td className="center">
                <div className="run-files__actions">
                    <button onClick={() => downloadFile(file.id, file.name)} className="btn run-files__actions--action">
                        <ReactSVG src={download} />
                    </button>
                </div>
            </td>
        </tr>
    ));

    const table = (
        <div className="run-files__table">
            <table className="table">
                <tbody>
                    {fileTableItems}
                </tbody>
            </table>
        </div>
    )

    return (
        <div className="run-files">
            {
                !Array.isArray(fileTableItems) || !fileTableItems.length
                    ? <NoFiles />
                    : table
            }
        </div>

    );
}

export default RunFiles;