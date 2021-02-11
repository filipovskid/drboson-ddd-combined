import Utils from './Utils';
var Graph = require("@dagrejs/graphlib").Graph;

class BranchMode {

    // Managing the actions of RDF transform in BRANCH mode
    // constructor() { }

    static initBranchGraphPreview(branchesState, branch, setSelectable, setSelectionPair) {
        const { startNode } = branchesState;
        const nodes = new Map();
        const edges = new Map();
        nodes.set(startNode.id, startNode);

        branch.branchEdges.filter(edge => edge.id !== 'start')
            .forEach(edge => {
                nodes.has(edge.source) || nodes.set(edge.source, edge.source_node);
                nodes.has(edge.target) || nodes.set(edge.target, edge.target_node);

                if (!edges.has(edge.id))
                    edges.set(edge.id, edge);
            });

        setSelectionPair({
            'node': startNode,
            'edge': { id: 'start' }
        });

        setSelectable(current => ({
            ...current,
            'startNode': startNode,
            'nodes': Array.from(nodes.values()),
            'edges': Array.from(edges.values())
        }));
    }

    static invalidateBranchMode(setLiterals, setSelectionPair, setSelectable) {
        setLiterals(this.defaultLiterals());
        setSelectionPair(this.defaultSelectionPair());
        setSelectable(this.defaultSelectableState());
    }

    static getLiterals(branchId, selectionPair, branchLiterals) {
        const { edge } = selectionPair;

        if (!branchLiterals.branches[branchId] || !branchLiterals.branches[branchId][edge.id]) {
            console.error('Error!! Branch or edge not found.');
            return [];
        }

        if (edge.id === 'start')
            return branchLiterals.startLiterals;

        return branchLiterals.branches[branchId][edge.id]
    }

    static getSelectedLiterals(branchId, selectionPair, branchLiterals) {
        return this.getLiterals(branchId, selectionPair, branchLiterals)
            .filter(l => l.selected);
    }

    static onNodeClick(node, branchesState, branchId, setSelectionPair, setLiterals) {
        const { startNode } = branchesState;
        const branch = branchesState.branches.find(branch => branch.id === branchId) || null;
        const branchExists = branch;

        if (!branchExists || node.id !== startNode.id)
            return;

        let selectionPair = {
            'node': { ...startNode },
            'edge': { 'id': 'start' }
        };

        setSelectionPair(selectionPair);
        // this.setLiteralsPreview(selectionPair, branch, branchesState, setLiterals);
    }

    static onEdgeClick(edge, branchesState, branchId, setSelectionPair, setLiterals) {
        const branch = branchesState.branches.find(branch => branch.id === branchId) || null;
        const branchExists = branch;

        if (!branchExists)
            return;

        let selectionPair = {
            'node': { ...edge.target_node },
            'edge': Utils.cloneEdge(edge)
        };

        setSelectionPair(selectionPair);
        // this.setLiteralsPreview(selectionPair, branch, branchesState, setLiterals);
    }

    static onLiteralToggle(literal, branchId, selectionPair, setBranchLiterals) {
        const { edge } = selectionPair;

        setBranchLiterals(current => {
            if (!(branchId in current.branches))
                return current;

            const branch = current.branches[branchId]

            let branchLiterals = edge.id === 'start'
                ? current.startLiterals
                : branch[edge.id];

            let toggledLiterals = branchLiterals.map(l => {
                if (l.id === literal.id) {
                    return { ...l, selected: !l.selected };
                }

                return l;
            });

            if (edge.id === 'start')
                return {
                    ...current,
                    'startLiterals': toggledLiterals
                };

            let branches = {
                ...current.branches,
                [branchId]: {
                    ...branch,
                    [edge.id]: toggledLiterals
                }
            };

            return {
                ...current,
                'branches': branches
            }
        });
    };

    static handleLiteralNameChange(branchId, literalId, value, selectionPair, setBranchLiterals) {
        console.log('Entering');
        const { edge } = selectionPair;
        setBranchLiterals(current => {
            let branches = current.branches;

            if (!(branchId in branches) || !(edge.id in branches[branchId]))
                return;

            let branch = branches[branchId]
            let branchLiterals = edge.id === 'start'
                ? current.startLiterals
                : branch[edge.id];

            const renamedLiterals = branchLiterals.map(l => {
                if (l.id === literalId) {
                    return {
                        ...l,
                        'name': value
                    };
                }

                return l;
            });

            if (edge.id === 'start')
                return {
                    ...current,
                    'startLiterals': renamedLiterals
                };

            return {
                ...current,
                'branches': {
                    ...branches,
                    [branchId]: {
                        ...branch,
                        [edge.id]: renamedLiterals
                    }
                }
            };
        });
    }

    static setLiteralsPreview(selectionPair, branch, branchesState, setLiterals) {
        if (selectionPair.edge.id === 'start') {
            setLiterals({
                ...this.defaultLiterals(),
                'displayed': branchesState.startLiterals.map(Utils.cloneLiteral)
            });

            return;
        }

        let edgeLiterals = branch.edgeLiterals[selectionPair.edge.id];
        setLiterals({
            ...this.defaultLiterals(),
            'displayed': edgeLiterals.map(Utils.cloneLiteral)
        });
    }

    static removeBranchLiterals(branchId, branchCount, setBranchLiterals) {
        if (branchCount === 0) {
            setBranchLiterals(this.defaultBranchLiterals());
            return;
        }

        setBranchLiterals(literals => {
            let branchesLiterals = { ...literals.branches };
            delete branchesLiterals[branchId];

            return {
                ...literals,
                'branches': branchesLiterals
            }
        });
    }

    static defaultLiterals() {
        return { 'displayed': [], 'edge': {} };
    }

    static defaultSelectionPair() {
        return { 'node': {}, 'edge': {} };
    }

    static defaultSelectableState() {
        return {
            'startNode': null,
            'graph': new Graph({ multigraph: true, directed: true }),
            'branchEdges': [],
            'nodes': [],
            'edges': [],
        };
    }

    static defaultBranchLiterals() {
        return {
            'startLiterals': [],
            'branches': {}
        };
    }
};

export default BranchMode;