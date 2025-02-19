import React from "react";

type DataFieldProps = {
  /**
   * The icon to display next to the label.
   */
  icon: React.ReactNode;
  /**
   * The label or heading for this data field.
   */
  label: string;
  /**
   * The value to display.
   */
  value: string;
}

/**
 * DataField Component
 *
 * A reusable component that displays a single data field with an icon,
 * a label, and a value. Designed for use in a grid layout.
 *
 * @param props - The properties for the component.
 * @returns A React element representing a data field.
 */
export const DataField: React.FC<DataFieldProps> = ({ icon, label, value }) => {
  return (
    <div className="flex items-center p-2">
      <div className="mr-3">{icon}</div>
      <div>
        <p className="text-xs">{label}</p>
        <p className="text-title-md text-black dark:text-white">{value}</p>
      </div>
    </div>
  );
};
