import { DefaultLayout } from "@/layout/DefaultLayout.tsx";
import { InsightsChart } from "@/components/Charts/InsightsChart.tsx";

export const Insights = () => {
  return (
    <>
      <DefaultLayout>
        <div className="grid grid-cols-1">
          <InsightsChart />
        </div>
      </DefaultLayout>
    </>
  );
};
