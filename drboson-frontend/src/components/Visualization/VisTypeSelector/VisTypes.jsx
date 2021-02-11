import LineSeriesConfigurer from '../LineSeriesConfigurer/lineSeriesConfigurer';

import lineSeriesIcon from './chart-art/line-chart.svg';

export const visTypes = Object.freeze({
    lineSeries: "lineSeries",
});

export const visTypeConfigs = [
    { name: "Line Series", icon: lineSeriesIcon, type: visTypes.lineSeries },
]

export const visTypeConfigurers = {
    [visTypes.lineSeries]: LineSeriesConfigurer,
}