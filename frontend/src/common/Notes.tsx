export const Notes = () => {
  return (
    <div className="flex flex-col gap-3 pt-4 border-t border-gray-300 dark:border-gray-700 mt-16">
      <h3 className="text-lg font-semibold text-gray-900 dark:text-white">📝 My Rough Notes - Data Needed:</h3>
      <ul className="space-y-2 text-gray-700 dark:text-gray-300 my-2">
        <li className="text-sm font-medium">SINGLE DAY:</li>
        <li className="text-sm font-medium">• Average Resting Heart Rate</li>
        <li className="text-sm font-medium">• Highest Heart Rate</li>

        <li className="text-sm font-medium">1 WEEK:</li>
        <li className="text-sm font-medium">• Average Resting Heart Rate</li>
        <li className="text-sm font-medium">• Highest Heart Rate</li>

        <li className="text-sm font-medium">1 MONTH:</li>
        <li className="text-sm font-medium">• Average Resting Heart Rate</li>
        <li className="text-sm font-medium">• Highest Heart Rate</li>
      </ul>
    </div>
  );
};
