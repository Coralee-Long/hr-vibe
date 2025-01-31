import { useCurrentDaySummary } from "@/context/CurrentDaySummaryContext";
import { ApexOptions } from "apexcharts";
import React, { useEffect, useState } from "react";
import ReactApexChart from "react-apexcharts";

interface DonutChartState {
  series: number[];
}

const options: ApexOptions = {
  chart: {
    fontFamily: "Satoshi, sans-serif",
    type: "donut",
    width: "100%",
  },
  colors: [
    "#F8D66D",
    "#FFB54C",
    "#FF6961",
    "transparent",
    "#88CFF1",
    "#8CD47E",
  ],
  labels: ["Zone 3", "Zone 4", "Zone 5", "EMPTY", "Zone 1", "Zone 2"],
  legend: {
    show: false,
    position: "bottom",
  },

  plotOptions: {
    pie: {
      donut: {
        size: "70%",
        background: "transparent",
      },
    },
  },
  dataLabels: {
    enabled: false,
  },
  responsive: [
    {
      breakpoint: 2600,
      options: {
        chart: {
          width: 380,
        },
      },
    },
    {
      breakpoint: 640,
      options: {
        chart: {
          width: "90%",
        },
      },
    },
  ],
};



export const HeartRateDonutChart: React.FC = () => {
  const { summary } = useCurrentDaySummary();

  const [state] = useState<DonutChartState>({
    //      [ "Zone 3", "Zone 4", "Zone 5", "EMPTY", "Zone 1", "Zone 2"],
    series: [20, 10, 10, 20, 20, 20],
  });

  // const handleReset = () => {
  //   setState((prevState) => ({
  //     ...prevState,
  //     series: [20, 10, 10, 20, 20, 20],
  //   }));
  // };
  // handleReset;

  useEffect(() => {
    console.log(summary)
  })

  return (
    <>
    <div className="mb-2">
      <div id="chartThree" className="mx-auto flex justify-center">
        <ReactApexChart
          options={options}
          series={state.series}
          type="donut"
        />
      </div>
      </div>
      <div>
        <h5 className="text-xl font-semibold text-black dark:text-white">
          {summary.summary?.rhrAvg} bpm
        </h5>
        <p className="text-sm text-gray-500">Average Resting Heart Rate</p>
      </div>
    </>
  );
};
