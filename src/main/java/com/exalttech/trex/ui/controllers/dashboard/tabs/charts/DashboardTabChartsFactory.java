package com.exalttech.trex.ui.controllers.dashboard.tabs.charts;

import javafx.beans.property.IntegerProperty;


public class DashboardTabChartsFactory {
    public static class ChartTypes {
        public static final String TX_PKTS = "Tx pkts";
        public static final String RX_PKTS = "Rx pkts";
        public static final String TX_BYTES = "Tx bytes";
        public static final String RX_BYTES = "Rx bytes";
        public static final String TX_PPS = "Tx pps";
        public static final String RX_PPS = "Rx pps";
        public static final String TX_BPS_L1 = "Tx Bps L1";
        public static final String TX_BPS_L2 = "Tx Bps L2";
        public static final String RX_BPS_L2 = "Rx Bps L2";
        public static final String MAX_LATENCY = "Max Latency";
        public static final String LATENCY_HISTOGRAM = "Latency Histogram";
    }

    public static DashboardTabChartsUpdatable create(String chartType, IntegerProperty interval) {
        switch (chartType) {
            case ChartTypes.TX_PKTS:
                return new DashboardTabChartsTxPkts(interval);
            case ChartTypes.RX_PKTS:
                return new DashboardTabChartsRxPkts(interval);
            case ChartTypes.TX_BYTES:
                return new DashboardTabChartsTxBytes(interval);
            case ChartTypes.RX_BYTES:
                return new DashboardTabChartsRxBytes(interval);
            case ChartTypes.TX_PPS:
                return new DashboardTabChartsTxPps(interval);
            case ChartTypes.RX_PPS:
                return new DashboardTabChartsRxPps(interval);
            case ChartTypes.TX_BPS_L1:
                return new DashboardTabChartsTxBpsL1(interval);
            case ChartTypes.TX_BPS_L2:
                return new DashboardTabChartsTxBpsL2(interval);
            case ChartTypes.RX_BPS_L2:
                return new DashboardTabChartsRxBps(interval);
            case ChartTypes.MAX_LATENCY:
                return new DashboardTabChartsMaxLatency(interval);
            case ChartTypes.LATENCY_HISTOGRAM:
                return new DashboardTabChartsLatencyHistogram();
        }
        throw new IllegalArgumentException(String.format("Unknown chart type: %s", chartType));
    }
}
