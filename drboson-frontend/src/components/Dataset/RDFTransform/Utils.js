class Utils {

    static cloneLiteral(literal) {
        return Object.assign({}, literal);
    };

    static cloneEdgeLiterals(literals) {
        return Object.keys(literals).reduce((o, edgeId) => {
            const edgeLiterals = literals[edgeId].map(this.cloneLiteral);

            return { ...o, [edgeId]: edgeLiterals }
        }, {});
    };

    static cloneEdge(edge) {
        return {
            ...edge,
            'source_node': { ...edge.source_node },
            'target_node': { ...edge.target_node }
        };
    };

    // static cloneBranch(branch) {
    //     let branchEdges
    //     return {
    //         ...branch
    //     }
    // }

    // static cloneBranchesState(branchesState) {

    // }

    // static shalowCopyBranchesState(branchesState) {
    //     return { .}
    // }


}

export default Utils;