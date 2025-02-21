import React from "react";

type GraphCardWrapperProps = {
  /**
   * The title displayed in the card header.
   */
  title: string;
  /**
   * The icon to display next to the title.
   */
  icon: React.ReactNode;
  /**
   * The content to display within the card.
   */
  children: React.ReactNode;
  /**
   * Additional CSS classes for custom styling (optional).
   */
  className?: string;
};

/**
 * GraphCardWrapper provides a consistent layout for graph components.
 * This version does not enforce a fixed height, so it will shrink to fit its content.
 */
export const GraphCardWrapper: React.FC<GraphCardWrapperProps> = ({
                                                                    title,
                                                                    icon,
                                                                    children,
                                                                    className = "",
                                                                  }) => {
  return (
    <div
      className={`mb-12 p-12 rounded-lg border border-stroke bg-white shadow-md dark:border-strokedark dark:bg-boxdark ${className}`}
    >
      {/* Card Header */}
      <div className="mb-8 flex items-center gap-3">
        {icon}
        <h2 className="text-xl font-bold text-black dark:text-white">{title}</h2>
      </div>
      {/* Card Content */}
      <div className="children">
        {children}
      </div>
    </div>
  );
};
