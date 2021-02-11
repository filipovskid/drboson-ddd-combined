import React from 'react';
import ReactEcharts from 'echarts-for-react';
import Loading from '../../../Loading/loading';
import GridLoader from "react-spinners/GridLoader";
// import { data } from './data';

const RDFGraph = (props) => {

    const tooltipFormatter = (params, ticket, callback) => {
        const dataType = params.dataType;
        const formats = {
            'node': (node) => (node.name),
            'edge': (edge) => (edge.value)
        }

        return dataType in formats
            ? formats[dataType](params.data)
            : 'Default';
    }

    const options = {
        tooltip: {},
        series: {
            type: 'graph',
            layout: 'circular',
            // force: {
            //     initLayout: 'circular',
            //     repulsion: 1000,
            //     edgeLength: [130, 350]
            // },
            circular: {
                rotateLabel: true
            },
            roam: true,
            draggable: true,
            symbolSize: 18,
            focusNodeAdjacency: true,
            edgeSymbol: ['none', 'arrow'],
            edgeSymbolSize: 6,
            itemStyle: {
                color: '#61a0a8', // $accent-color-light
                borderColor: '#4b636e' // $icon-color
            },
            lineStyle: {
                color: '#4b636e' // $accent-color-dark
            },
            label: {
                show: true,
                position: 'top',
                formatter: '{c}'
            },
            tooltip: {
                formatter: tooltipFormatter
            },
            autoCurveness: 1,
            nodes: props.nodes,
            // nodes: data.nodes.map((node) => {
            //     node.name = node.id;
            //     node.value = node.id + ' meow';
            //     return node;
            // }),
            links: props.edges
            // links: data.links
        }
    }

    const onEdgeClick = (e) => {
        props.onEdgeClick(e.data);
    }

    const onNodeClick = (e) => {
        props.onNodeClick(e.data);
    }

    const clickEventHandlers = {
        'edge': onEdgeClick,
        'node': onNodeClick
    }

    const onChartClick = (e) => {
        const handler = e.dataType in clickEventHandlers
            ? clickEventHandlers[e.dataType]
            : (e) => e.stopPropagation();

        handler(e);
    }

    let onEvents = {
        'click': onChartClick
    }

    const loading = (
        <Loading
            loading={props.loading}
            loader={GridLoader}
            color="#fdd703"
        />
    );

    return (
        <div style={{ 'width': '100%', 'height': '100%', 'position': 'relative' }}>
            <ReactEcharts
                option={options}
                style={{ height: '100%', width: '100%' }}
                onEvents={onEvents}
            />
            {loading}
        </div>
    );
}

export default React.memo(RDFGraph);