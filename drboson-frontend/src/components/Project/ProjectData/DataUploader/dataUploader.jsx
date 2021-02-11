import React, { useMemo, useCallback } from 'react';
import { useDropzone } from 'react-dropzone';
import { ReactSVG } from 'react-svg';
import upload from '../../../../images/upload.svg';
import './dataUploader.css';

const DataUploader = (props) => {

    const onDrop = useCallback(acceptedFiles => {
        props.onFileAttach(acceptedFiles[0]);
    }, [props])

    const {
        getRootProps,
        getInputProps,
        isDragActive,
        isDragAccept,
        isDragReject
    } = useDropzone({ onDrop });

    const classes = useMemo(() => (
        'data-uploader' +
        (isDragActive ? ' active' : '') +
        (isDragAccept ? ' accept' : '') +
        (isDragReject ? ' reject' : '')
    ), [
        isDragActive,
        isDragReject,
        isDragAccept
    ]);


    return (
        <div {...getRootProps({ className: classes })}>
            <input {...getInputProps()} />
            <div className="data-uploader__content">
                <ReactSVG src={upload} className='content-art' />
                {
                    isDragReject ?
                        <div>You can't upload this type of file</div> :
                        <div>Upload some data for your runs</div>
                }
            </div>
        </div>
    );
}

export default DataUploader;