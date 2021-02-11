import React from 'react';
import { ReactSVG } from 'react-svg';
import { useHistory } from 'react-router-dom';
import trash from '../../../../images/trash-2.svg';
import download from '../../../../images/download.svg';
// import arrow from '../../../../images/arrow-right.svg';
import StorageStatus from '../StorageStatus/storageStatus';
import FloatingMenu from '../../../FloatingMenu/floatingMenu';
import MenuItem from '../../../FloatingMenu/FloatingMenuItem/floatingMenuItem';
// import { StatusEnum as StorageStatusEnum } from '../StorageStatus/storageStatus'
import './datasetItem.css';

const DatasetTypeEnum = Object.freeze({
    RDF: { value: "RDF", desc: "RDF Ready" },
    COMMON: { value: "Common", desc: "Common" },
    DEFAULT: { value: "Common", desc: "Common" }
})

const DatasetItem = (props) => {
    const { dataset } = props;
    console.log(dataset)
    dataset.type = "common"; // TO BE REMOVED

    const dataset_type = dataset.type.toUpperCase() in DatasetTypeEnum
        ? DatasetTypeEnum[dataset.type.toUpperCase()]
        : DatasetTypeEnum.DEFAULT;

    const storage_tip = dataset.type.toUpperCase() === "RDF"
        ? dataset_type.desc.concat(': ', dataset.rdf_status.toUpperCase())
        : dataset_type.desc;

    const storage_status = dataset.type.toUpperCase() === "RDF"
        ? dataset.rdf_status
        : dataset.storage_status

    console.log(dataset.storage_status);

    const history = useHistory();
    const transformRedirect = (type) => {
        if (type.toUpperCase() === "RDF") {
            history.push(`/projects/${props.project_id}/datasets/${dataset.id}/rdf`);
            return;
        }

        history.push(`/projects/${props.project_id}/datasets/${dataset.id}/processing`);
    };

    const canTransform = dataset.type.toUpperCase() === "RDF"
        ? dataset.rdf_status.toUpperCase() === "COMPLETED"
        : dataset.storage_status.toUpperCase() === "COMPLETED";

    const rdfMenu = (
        <FloatingMenu className="btn dataset--action">
            <MenuItem onClick={() => props.onRdfStore(dataset.id)}>Store dataset</MenuItem>
            <MenuItem
                onClick={() => transformRedirect(dataset.type)}
                disabled={!canTransform}>Transform dataset
            </MenuItem>
        </FloatingMenu>
    );

    const commonMenu = (
        <FloatingMenu className="btn dataset--action">
            <MenuItem onClick={() => props.onCommonStore(dataset.id)}>Store dataset</MenuItem>
            <MenuItem
                onClick={() => transformRedirect(dataset.type)}
                disabled={!canTransform}>Transform dataset
            </MenuItem>
        </FloatingMenu>
    );

    const moreMenu = dataset.type.toUpperCase() === "RDF"
        ? rdfMenu
        : commonMenu;

    return (
        <div className="dataset">
            <div className="dataset__item">
                <div className="dataset__item--header">Name</div>
                <div className="dataset__item--input">
                    {props.dataset.name}
                </div>
            </div>

            <div className="dataset__item">
                <div className="dataset__item--header">Type</div>
                <div className="dataset__item--input">
                    File Type
                </div>
            </div>

            <div className="dataset__item">
                <div className="dataset__item--header">Date</div>
                <div className="dataset__item--input">
                    Current Date
                </div>
            </div>

            <div className="dataset__item">
                <div className="dataset__item--header">Status</div>
                <div className="dataset__item--input">
                    <StorageStatus status={storage_status} tip={storage_tip} id={dataset.id} />
                </div>
            </div>

            <div className="dataset__item dataset__actions">
                {moreMenu}
                <button onClick={() => props.onDatasetDownload(props.dataset.id, props.dataset.name)} className="btn dataset--action">
                    <ReactSVG src={download} />
                </button>
                <button onClick={() => props.onDatasetRemove(props.dataset.id)} className="btn dataset--action" type="submit">
                    <ReactSVG src={trash} />
                </button>
            </div>
        </div>
    );
}

export default DatasetItem;