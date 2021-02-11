import React, { useEffect, useState } from 'react';
import { useParams, useHistory } from 'react-router-dom';
import { useEnqueueSnackbar } from '../../Notification/useEnqueueSnackbar';
import RDFGraph from './RDFGraph/rdfGraph';
import TransformControl from './TransformControl/transformControl';
import TransformPreview from './TransformPreview/transformPreview';
import EditableHeadingTable from './EditableHeadingTable/editableHeadingTable';
import TransformStatus from './TransformStatus/transformStatus';
import RDFService from '../../../actions/rdf';
import BranchMode from './BranchMode';
import Utils from './Utils';
import './rdfTransform.css';

var Graph = require("@dagrejs/graphlib").Graph;
// var _ = require('lodash/core');
const shortid = require('shortid');

export const TransformMode = Object.freeze({
    'TRANSFORM': 0,
    'BRANCH': 1
});

// Initially it is required to choose a node.
// Node whose literal is chosen first is taken as a start node (initial node).
// After choosing an initial node, graph will branch on its own.

const RDFTransform = (props) => {
    const [classes, setClasses] = useState([]);
    const [fullGraph, setFullGraph] = useState(new Graph());
    const [edges, setEdges] = useState([]);
    const [literals, setLiterals] = useState({ 'displayed': [], 'edge': {} });
    const [subjectLiterals, setSubjectLiterals] = useState({});
    const [selectionPair, setSelectionPair] = useState({ 'node': {}, 'edge': {} });
    const [branchesState, setBranchesState] = useState({
        'startNode': null,
        // 'startLiterals': [],
        'branches': []
    });
    const [branchLiterals, setBranchLiterals] = useState({
        'startLiterals': [],
        'branches': {}
    });
    const [selectable, setSelectable] = useState({
        'startNode': null,
        'graph': new Graph({ multigraph: true, directed: true }),
        'branchEdges': [],
        'nodes': [],
        'edges': [],
    });
    const { projectId, datasetId } = useParams();
    const history = useHistory();

    // Mode
    const [modeState, setModeState] = useState({
        'mode': TransformMode.TRANSFORM,
        'selectedBranchId': null
    });

    // Loading data
    const [loading, setLoading] = useState(true);
    const [loadingFailed, setLoadingFailed] = useState(false);

    // Notification
    const enqueueSnackbar = useEnqueueSnackbar();

    // Save
    const [isSaving, setIsSaving] = useState(false);

    useEffect(() => {
        function nameFromURL(url_string) {
            let url = new URL(url_string);
            let hash = url.hash

            return hash
                ? hash.substr(1)
                : url.pathname.split('/').pop();
        };

        function createGraphAndInitializeEdges(edges) {
            let graph = new Graph({ multigraph: true, directed: true });

            let initEdges = edges.map(edge => ({
                id: [edge.subject, edge.property, edge.object].join(','),
                source_node: {
                    id: edge.subject,
                    name: edge.subject,
                    value: nameFromURL(edge.subject)
                },
                target_node: {
                    id: edge.object,
                    name: edge.object,
                    value: nameFromURL(edge.object)
                },
                source: edge.subject,
                target: edge.object,
                name: edge.property,
                value: nameFromURL(edge.property)
            }));

            setEdges(initEdges);
            initEdges.forEach(e => graph.setEdge(e.source, e.target, e, e.id));

            return graph;
        };

        const groupBy = (arr, key) => {
            return arr.reduce((acc, curr) => {
                (acc[curr[key]] = acc[curr[key]] || []).push(curr);
                return acc;
            }, {});
        };

        function fetchRdfClasses() {
            return RDFService.fetchRdfClasses(projectId, datasetId)
                .then(response => {
                    let nodes = response.data.map(rdfClass => ({
                        id: rdfClass.class,
                        name: rdfClass.class,
                        value: nameFromURL(rdfClass.class)
                    }));

                    setClasses(nodes);
                })
                .catch(error => {
                    setLoadingFailed(true);
                });
        }

        function fetchRdfEdges() {
            return RDFService.fetchRdfEdges(projectId, datasetId)
                .then(response => {
                    setFullGraph(createGraphAndInitializeEdges(response.data));
                })
                .catch(error => {
                    setLoadingFailed(true);
                });
        }

        function fetchRdfLiteralEdges() {
            return RDFService.fetchRdfLiteralEdges(projectId, datasetId)
                .then(response => {
                    let literalEdges = response.data.map(le => ({
                        subject: le.subject,
                        property: le.property,
                        id: [le.subject, le.property].join(','),
                        subject_name: nameFromURL(le.subject),
                        property_name: nameFromURL(le.property),
                        value: nameFromURL(le.property),
                        selected: false,
                        disabled: false,
                        name: '' // Name given by the user
                    }));

                    let groupedLiteralEdges = groupBy(literalEdges, 'subject');
                    setSubjectLiterals(groupedLiteralEdges);
                })
                .catch(error => {
                    setLoadingFailed(true);
                });
        }

        Promise.all([fetchRdfClasses(), fetchRdfEdges(), fetchRdfLiteralEdges()])
            .then(r => {
                setLoading(false);

                if (loadingFailed) {
                    enqueueSnackbar('Could not retrieve data', { variant: 'error' });
                    history.push(`${projectId}/data`);
                }
            });

    }, [projectId, datasetId]);

    const resetLiterals = () => {
        setLiterals({ 'displayed': [], 'edge': {} })
    }

    const initSelectable = ({ node, edge }) => { // Initial node and edge
        const graph = new Graph({ multigraph: true, directed: true });
        const selectableEdges = fullGraph.outEdges(node.id);
        let edges = [];
        let nodes = new Map();
        nodes.set(node.id, node);

        selectableEdges.forEach(e => {
            let edge = fullGraph.edge(e.v, e.w, e.name);
            // let source = { id: edge.subject, name: edge.subject, value: edge.subject_name };
            let target = { ...edge.target_node };

            // graph.setNode(source); nodes.push(source);
            graph.setNode(target); nodes.set(target.id, target);
            graph.setEdge(edge.source, edge.target, edge, edge.id); edges.push(edge);
        });

        return {
            'startNode': node,
            'graph': graph,
            'branchEdges': [],
            'nodes': Array.from(nodes.values()),
            'edges': edges
        };
    };

    const updateSelectable = (curr, pair) => {
        const graph = new Graph({ multigraph: true, directed: true });
        const selectableEdges = fullGraph.outEdges(pair.node.id);
        let branchEdges = curr['branchEdges'];
        let branches = new Map(); branchEdges.forEach(e => branches.set(e.id, e));
        let edges = new Map();
        let nodes = new Map();

        if (!branches.has(pair.edge.id)) {
            branchEdges.push(pair.edge);
            branches.set(pair.edge.id, { ...pair.edge })
        }

        branchEdges.forEach(e => {
            graph.setEdge(e.source, e.target, e, e.id);
            let selectedEdge = {
                ...e,
                'lineStyle': {
                    'color': 'red',
                }
            };
            edges.set(selectedEdge.id, selectedEdge);

            nodes.has(e.target) || nodes.set(e.target, e.target_node);
            nodes.has(e.source) || nodes.set(e.source, e.source_node);
        });

        selectableEdges.forEach(e => {
            let edge = fullGraph.edge(e.v, e.w, e.name);
            let target = { ...edge.target_node };

            if (!edges.has(edge.id)) {
                nodes.has(target.id) || nodes.set(target.id, target);
                edges.set(edge.id, edge);

                graph.setNode(target);
                graph.setEdge(edge.source, edge.target, edge, edge.id);
            }
        });

        return {
            ...curr,
            'graph': graph,
            'nodes': Array.from(nodes.values()),
            'edges': Array.from(edges.values()),
            'branchEdges': Array.from(branches.values())
        }
    };

    const updateSelectableOnLiteralToggle = (pair, literal, subjectLiterals) => {
        setSelectable(currSelectable => {
            if (!currSelectable.startNode)
                return initSelectable(pair);

            const selectedCount = subjectLiterals.reduce((acc, curr) => acc + curr.selected, 0);
            const newInitialLiteral = selectedCount === 1 && !literal.selected

            if (currSelectable.startNode.id !== literal.subject || pair.edge.id !== 'start')
                return selectedCount === 0 || !newInitialLiteral
                    ? currSelectable
                    : updateSelectable(currSelectable, pair);

            // Start node settings
            // Reset RDF transform
            if (selectedCount === 0) {
                resetLiterals();
                return {
                    ...currSelectable,
                    'startNode': null,
                    'graph': new Graph({ directed: true }),
                    'nodes': [],
                    'edges': [],
                    'branchEdges': []
                }
            }

            return currSelectable;
        });
    };

    const onLiteralToggle = (pair, literal) => {
        // Ugly func
        const toggleLiteral = (literals, literal) => {
            return literals.map(l => {
                // if (l.property === literal.property)
                if (l.id === literal.id)
                    return {
                        ...l,
                        selected: !l.selected
                    };

                return l;
            });
        };

        setLiterals(current => {
            const currentLiterals = toggleLiteral(current.displayed, literal);

            updateSelectableOnLiteralToggle(pair, literal, currentLiterals);

            return {
                'displayed': currentLiterals,
                'edge': {
                    ...current.edge,
                    [pair.edge.id]: currentLiterals
                }
            };
        });
    };

    const onBranchModeLiteralToggle = (literal) => {
        BranchMode.onLiteralToggle(literal, modeState.selectedBranchId, selectionPair,
            setBranchLiterals);

        // BranchMode.updateLiterals(selectionPair, branchesState, setLiterals);
    };

    const onNodeClick = React.useCallback((node) => {
        if (selectable.startNode) {
            if (selectable.startNode.id === node.id) {
                setSelectionPair({
                    'edge': { id: 'start' },
                    'node': node
                });

                setLiterals(current => ({
                    ...current,
                    'displayed': current.edge['start']
                }));
            }

            return;
        }

        // On initial selected node set selection pair and initial start edge literals
        setSelectionPair({
            'edge': { id: 'start' },
            'node': node
        });

        setLiterals(current => ({
            ...current,
            'displayed': subjectLiterals[node.id].map(Utils.cloneLiteral)
        }));
    }, [selectable, subjectLiterals]);

    const onBranchModeNodeClick = React.useCallback((node) => {
        BranchMode.onNodeClick(node, branchesState, modeState.selectedBranchId,
            setSelectionPair, setLiterals);
    }, [branchesState, modeState]);

    const onEdgeClick = React.useCallback((edge) => {
        if (selectable.branchEdges.some(e => e.id === edge.id)) {
            return;
        }

        setSelectionPair({
            'node': edge.target_node,
            'edge': edge
        });

        setLiterals(current => ({
            ...current,
            'displayed': edge.id in current.edge
                ? current.edge[edge.id]
                : subjectLiterals[edge.target].map(Utils.cloneLiteral)
        }));
    }, [fullGraph, subjectLiterals, selectable]);

    const onBranchModeEdgeClick = React.useCallback((edge) => {
        BranchMode.onEdgeClick(edge, branchesState, modeState.selectedBranchId,
            setSelectionPair, setLiterals);
    }, [branchesState, modeState]);

    const branchReset = (startNode, startLiterals) => {
        const newSelectionPair = {
            'edge': { id: 'start' },
            'node': { ...startNode }
        }

        setSelectionPair(newSelectionPair);

        const disabledStartLiterals = startLiterals.map(l => {
            return {
                ...l,
                'disabled': true
            }
        });

        setLiterals(current => ({
            'displayed': disabledStartLiterals,
            'edge': { 'start': startLiterals.map(Utils.cloneLiteral) }
        }));

        setSelectable(initSelectable(newSelectionPair));
    }

    const onAddBranch = () => {
        setBranchesState(current => {
            if (!selectable.startNode)
                return current;

            if (current.startNode && selectable.startNode.id !== current.startNode.id)
                return current;

            let startNode = current.startNode || { ...selectable.startNode };
            const branchEdgeLiterals = Utils.cloneEdgeLiterals(literals['edge']);
            const startLiterals = branchEdgeLiterals['start']
                .map(Utils.cloneLiteral);

            let branch = {
                'id': shortid.generate(),
                'name': `Branch_${current.branches.length + 1}`,
                'startNode': { ...startNode },
                'branchEdges': selectable.branchEdges.map(Utils.cloneEdge),
                // 'edgeLiterals': branchEdgeLiterals
            }

            // branchReset(startNode, startLiterals);
            const newBranches = [...current.branches, branch]

            if (!current.startNode) {
                setBranchLiterals(literals => ({
                    'startLiterals': startLiterals,
                    'branches': {
                        ...literals.branches,
                        [branch.id]: branchEdgeLiterals
                    }
                }));

                branchReset(startNode, startLiterals);

                return {
                    // ...current,
                    'startNode': { ...startNode },
                    // 'startLiterals': startLiterals,
                    'branches': newBranches
                };
            }

            branchReset(startNode, branchLiterals.startLiterals);

            // Do not allow literal change.
            setBranchLiterals(literals => ({
                ...literals,
                'branches': {
                    ...literals.branches,
                    [branch.id]: branchEdgeLiterals
                    // [branch.id]: {
                    //     ...branchEdgeLiterals,
                    //     'start': literals.startLiterals.map(Utils.cloneLiteral)
                    // }
                }
            }));

            return {
                ...current,
                'branches': newBranches
            };
        });
    };

    const onBranchToggle = (branchId) => {
        const branch = branchesState.branches.find(b => b.id === branchId) || null;
        const branchExists = branch

        if (!branchExists)
            return;

        setModeState(current => {
            if (current.selectedBranchId === branchId) {
                BranchMode.invalidateBranchMode(setLiterals, setSelectionPair, setSelectable);

                return { 'mode': TransformMode.TRANSFORM, 'selectedBranchId': null };
            }

            BranchMode.initBranchGraphPreview(branchesState, branch, setSelectable, setSelectionPair);
            return { 'mode': TransformMode.BRANCH, 'selectedBranchId': branchId };
        });
    };

    const onBranchDelete = (branchId) => {
        const branchExists = branchesState.branches.some(b => b.id === branchId);

        if (!branchExists)
            return;

        setModeState(current => {
            if (current.selectedBranchId === branchId) {
                BranchMode.invalidateBranchMode(setLiterals, setSelectionPair, setSelectable);

                return { 'mode': TransformMode.TRANSFORM, 'selectedBranchId': null };
            }

            return current;
        });

        setBranchesState(current => {
            const currentBranches = current.branches.filter(b => b.id !== branchId);

            // There are no branches left, check your stuff man..
            // if (currentBranches.length === 0)
            //     return 
            // There is no issue, we would already be in TRANSFORM mode.
            let startNode = current.startNode;
            if (currentBranches.length === 0)
                startNode = null;

            BranchMode.removeBranchLiterals(branchId, currentBranches.length, setBranchLiterals);

            return {
                ...current,
                'startNode': startNode,
                'branches': currentBranches
            };
        });
    }

    const handleLiteralNameChange = (literalId, value) => {
        const branchId = modeState.selectedBranchId;
        BranchMode.handleLiteralNameChange(branchId, literalId, value, selectionPair, setBranchLiterals);
    };

    const getBranches = () => {
        return branchesState.branches.map(b => {
            if (b.id === modeState.selectedBranchId)
                return { ...b, selected: true };

            return { ...b, selected: false };
        });
    };

    const getTransformAttributes = () => {
        const literals_filter = (literal) => literal.selected && literal.name;

        const start_literals = branchLiterals.startLiterals.filter(literals_filter)
            .map(literal => ({
                'name': literal.name,
                'property': literal.property
            }));

        const branches_literals = branchesState.branches.flatMap(branch => {
            const edge_literals = branch.branchEdges.flatMap(edge => {
                const literals = branchLiterals.branches[branch.id][edge.id]
                    .filter(literals_filter)
                    .flatMap(literal => ({
                        'name': literal.name,
                        'property': literal.property
                    }));

                return literals;
            });

            return edge_literals
        });

        return [...start_literals, ...branches_literals];
    }

    const getGraphPreview = () => {
        if (modeState.mode === TransformMode.BRANCH) {
            return (
                <RDFGraph
                    nodes={selectable.nodes}
                    edges={selectable.edges}
                    onNodeClick={onBranchModeNodeClick}
                    onEdgeClick={onBranchModeEdgeClick}
                    loading={false}
                />
            );
        }

        let selectableNodes = selectable.nodes.length
            ? selectable.nodes
            : classes;

        return (
            <RDFGraph
                nodes={selectableNodes}
                edges={selectable.edges}
                onNodeClick={onNodeClick}
                onEdgeClick={onEdgeClick}
                loading={loading}
            />
        );
    };

    const getTransformControl = () => {
        if (modeState.mode === TransformMode.BRANCH) {
            const branchId = modeState.selectedBranchId;

            return (
                <TransformControl
                    // literals={literals.displayed}
                    literals={BranchMode
                        .getLiterals(branchId, selectionPair, branchLiterals)}
                    subjectName={selectionPair.node.value}
                    branches={getBranches()}
                    onLiteralToggle={onBranchModeLiteralToggle}
                    onBranchToggle={onBranchToggle}
                    onBranchDelete={onBranchDelete}
                />
            );
        }

        return (
            <TransformControl
                literals={literals.displayed}
                subjectName={selectionPair.node.value}
                branches={getBranches()}
                onLiteralToggle={(literal) => onLiteralToggle(selectionPair, literal)}
                onBranchToggle={onBranchToggle}
                onBranchDelete={onBranchDelete}
            />
        );
    };

    const getTransformPreview = () => {
        if (modeState.mode === TransformMode.BRANCH) {
            const branchId = modeState.selectedBranchId;

            return <EditableHeadingTable
                literals={BranchMode
                    .getSelectedLiterals(branchId, selectionPair, branchLiterals)}
                handleLiteralNameChange={(literalId, value) => handleLiteralNameChange(literalId, value)}
            />
        }

        return <TransformPreview
            literals={getTransformAttributes()}
        />;
    };

    function getTransformData() {
        if (!branchesState.startNode || !branchesState.branches.length)
            return null;

        const literals_filter = (literal) => literal.selected && literal.name;

        const data = {};
        const attributes = getTransformAttributes().map(a => a.name);

        // Start node.
        const start_literals = branchLiterals.startLiterals.filter(literals_filter)
            .map(literal => ({
                'name': literal.name,
                'property': literal.property
            }));

        const branches = branchesState.branches.map(branch => {
            const edges = branch.branchEdges.map(edge => {
                const target_node = edge.target;
                const relationship = edge.name;
                console.log('branch_id: ', branch.id, 'literals: ', branchLiterals.branches[branch.id][edge.id]);
                const literals = branchLiterals.branches[branch.id][edge.id]
                    .filter(literals_filter)
                    .map(literal => ({
                        'name': literal.name,
                        'property': literal.property
                    }));

                return {
                    'target_node': target_node,
                    'relationship': relationship,
                    'literals': literals
                };
            });

            return {
                'edges': edges
            };
        });

        data['node'] = branchesState.startNode.name;
        data['attributes'] = attributes;
        data['literals'] = start_literals;
        data['branches'] = branches;

        return data;
    };

    function transformDataset(projectId, datasetId) {
        if (isSaving) {
            return;
        }

        if (!branchesState.startNode || !branchesState.branches.length) {
            enqueueSnackbar('Create transformation branches', { variant: 'error' });
            return;
        }
        setIsSaving(true);

        const data = getTransformData();
        data['project_id'] = projectId;
        data['dataset_id'] = datasetId;

        if (!data.attributes.length) {
            enqueueSnackbar('No attributes available', { variant: 'error' });
        }

        RDFService.transformDataset(data)
            .then(response => {
                enqueueSnackbar('Transform job successfully created', { variant: 'success' });
            })
            .catch(error => {
                enqueueSnackbar('Failed creating a transform job', { variant: 'error' });
            })
            .then(() => {
                setIsSaving(false);
            });
    };

    let selectableNodes = selectable.nodes.length
        ? selectable.nodes
        : classes;

    console.log('Branches state:', branchesState);
    console.log('Branch literals:', branchLiterals);
    console.log('Data', getTransformData())
    // console.log('Mode state: ', modeState);

    return (
        <div className="rdf-transform">
            <div className="transform-control">
                {/* <TransformControl
                    literals={literals.displayed}
                    subjectName={selectionPair.node.value}
                    branches={getBranches()}
                    onLiteralToggle={(literal) => onLiteralToggle(selectionPair, literal)}
                    onBranchToggle={onBranchToggle}
                    onBranchDelete={onBranchDelete}
                /> */}
                {getTransformControl()}
            </div>
            <div className="transform-manager">
                <div className="transform-status-wrapper">
                    <TransformStatus
                        nodesCount={selectableNodes.length}
                        edgesCount={selectable.edges.length}
                        onAddBranch={onAddBranch}
                        onSave={() => transformDataset(projectId, datasetId)}
                        saveDisabled={isSaving}
                    />
                </div>
                <div className="transform-preview">
                    <div className="transform-preview__graph preview">
                        {/* <RDFGraph
                            nodes={selectableNodes}
                            edges={selectable.edges}
                            onNodeClick={onNodeClick}
                            onEdgeClick={onEdgeClick}
                        /> */}
                        {getGraphPreview()}
                    </div>
                    <div className="transform-preview__data preview">
                        {/* <TransformPreview /> */}
                        {/* <EditableHeadingTable /> */}
                        {getTransformPreview()}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default RDFTransform;