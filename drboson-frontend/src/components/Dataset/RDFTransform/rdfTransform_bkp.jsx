import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import RDFGraph from './RDFGraph/rdfGraph';
import TransformControl from './TransformControl/transformControl';
import TransformPreview from './TransformPreview/transformPreview';
import RDFService from '../../../actions/rdf';
import './rdfTransform.css';

var Graph = require("@dagrejs/graphlib").Graph;

// Initially it is required to choose a node.
// Node whose literal is chosen first is taken as a start node (initial node).
// After choosing an initial node, graph will branch on its own.

const RDFTransform = (props) => {
    const [classes, setClasses] = useState([]);
    const [fullGraph, setFullGraph] = useState(new Graph());
    const [edges, setEdges] = useState([]);
    const [literals, setLiterals] = useState({ 'displayed': [], 'edge': {} });
    const [subjectLiterals, setSubjectLiterals] = useState({});
    const [selectable, setSelectable] = useState({
        'startNode': null,
        'graph': new Graph({ multigraph: true, directed: true }),
        'nodes': [],
        'edges': [],
        'actions': []
    });
    const [selectionPair, setSelectionPair] = useState({ 'node': {}, 'edge': {} });
    const { projectId, datasetId } = useParams();

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

        RDFService.fetchRdfClasses(projectId, datasetId)
            .then(response => {
                let nodes = response.data.map(rdfClass => ({
                    id: rdfClass.class,
                    name: rdfClass.class,
                    value: nameFromURL(rdfClass.class)
                }));

                setClasses(nodes);
            });

        RDFService.fetchRdfEdges(projectId, datasetId)
            .then(response => {
                setFullGraph(createGraphAndInitializeEdges(response.data));
            });

        RDFService.fetchRdfLiteralEdges(projectId, datasetId)
            .then(response => {
                let literalEdges = response.data.map(le => ({
                    subject: le.subject,
                    property: le.property,
                    id: [le.subject, le.property].join(','),
                    subject_name: nameFromURL(le.subject),
                    property_name: nameFromURL(le.property),
                    value: nameFromURL(le.property),
                    selected: false,
                }));

                let groupedLiteralEdges = groupBy(literalEdges, 'subject');
                setSubjectLiterals(groupedLiteralEdges);
            });
    }, []);

    const resetLiterals = () => {
        setLiterals({ 'displayed': [], 'edge': {} })
    }

    function genStartSelectable() {
        return {
            'startNode': null,
            'graph': new Graph({ multigraph: true, directed: true }),
            'nodes': [],
            'edges': [],
            'actions': []
        }
    }

    const initSelectable = (curr, { node, edge }) => { // Initial node and edge
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

        let action = {
            'node': node,
            'edge': edge
        }
        curr.actions.push(action);

        return {
            ...curr,
            'startNode': node,
            'graph': graph,
            'nodes': Array.from(nodes.values()),
            'edges': edges
        };
    };

    // const updateSelectable = (edge) => {
    //     setSelectable(curr => {
    //         const graph = curr['graph'];
    //         const selectableEdges = fullGraph.outEdges(edge.target);
    //         let edges = new Map(); curr['edges'].forEach(e => edges.set(e.id, e));
    //         let nodes = new Map(); curr['nodes'].forEach(n => nodes.set(n.id, n));

    //         selectableEdges.forEach(e => {
    //             let edge = fullGraph.edge(e.v, e.w, e.name);
    //             let target = { ...edge.target_node };

    //             if (!edges.has(edge.id)) {
    //                 nodes.has(target.id) || nodes.set(target.id, target);
    //                 edges.set(edge.id, edge);

    //                 graph.setNode(target);
    //                 graph.setEdge(edge.source, edge.target, edge, edge.id);
    //             }
    //         });

    //         console.log('nodes', Array.from(nodes.values()));
    //         return {
    //             ...curr,
    //             'graph': graph,
    //             'nodes': Array.from(nodes.values()),
    //             'edges': Array.from(edges.values())
    //         }
    //     });
    // };

    const updateSelectable = (curr, pair) => {
        const graph = curr['graph'];
        const selectableEdges = fullGraph.outEdges(pair.node.id);
        let edges = new Map(); curr['edges'].forEach(e => edges.set(e.id, e));
        let nodes = new Map(); curr['nodes'].forEach(n => nodes.set(n.id, n));

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

        let action = {
            'node': pair.node,
            'edge': pair.edge
        }
        curr.actions.push(action);

        return {
            ...curr,
            'graph': graph,
            'nodes': Array.from(nodes.values()),
            'edges': Array.from(edges.values())
        }
    };

    const selectableReloadedToEdge = (currSelectable, fullGraph, edge) => {
        const { startNode, actions } = currSelectable;

        if (edge.id === 'start' || edge.id === actions[0].edge.id)
            return genStartSelectable();

        const graph = new Graph({ multigraph: true, directed: true });
        let edges = new Map();
        let nodes = new Map();
        let newActions = [];

        // Populate initial edges from start node
        const initialEdges = fullGraph.outEdges(startNode.id);
        initialEdges.forEach();
    }

    const updateSelectableOnLiteralToggle = (pair, literal, subjectLiterals) => {
        setSelectable(currSelectable => {
            if (!currSelectable.startNode)
                // return { ...currSelectable, 'startNode': node.id }
                return initSelectable(currSelectable, pair);

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
                    'startNode': null,
                    'graph': new Graph({ directed: true }),
                    'nodes': [],
                    'edges': [],
                    'actions': []
                }
            }

            return currSelectable;
        });
    };

    const onLiteralToggle = (pair, literal) => {
        // Ugly func
        const toggleLiteral = (literals, literal) => {
            return literals.map(l => {
                if (l.property === literal.property)
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
            'displayed': subjectLiterals[node.id].slice()
        }));
    }, [selectable, subjectLiterals]);

    const onEdgeClick = React.useCallback((edge) => {
        // updateSelectable(edge);

        setSelectionPair({
            'node': edge.target_node,
            'edge': edge
        });

        setLiterals(current => ({
            ...current,
            'displayed': edge.id in current.edge
                ? current.edge[edge.id]
                : subjectLiterals[edge.target].slice()
        }));
    }, [fullGraph, subjectLiterals]);

    let selectableNodes = selectable.nodes.length
        ? selectable.nodes
        : classes;

    return (
        <div className="rdf-transform">
            <div className="transform-control">
                <TransformControl
                    literals={literals.displayed}
                    subjectName={selectionPair.node.value}
                    onLiteralToggle={(literal) => onLiteralToggle(selectionPair, literal)}
                // onLiteralToggle={onLiteralToggle}
                />
            </div>
            <div className="transform-preview">
                <div className="transform-preview__graph preview">
                    <RDFGraph
                        nodes={selectableNodes}
                        edges={selectable.edges}
                        onNodeClick={onNodeClick}
                        onEdgeClick={onEdgeClick}
                    />
                </div>
                <div className="transform-preview__data preview">
                    <TransformPreview />
                </div>
            </div>
        </div>
    );
}

export default RDFTransform;