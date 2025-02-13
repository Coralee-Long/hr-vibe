import React from "react";

type CardProps = {
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
 * A generic Card component that provides a consistent layout and style.
 */
export const Card: React.FC<CardProps> = ({ title, icon, children, className = "" }) => {
  return (
    <div
      // Fixed height of 650px, along with existing padding, borders, and other styling.
      className={`flex flex-col items-center justify-center w-full h-[650px] rounded-lg border border-stroke bg-white p-4 shadow-md dark:border-strokedark dark:bg-boxdark md:p-6 xl:p-7.5 ${className}`}
    >
      {/* Card Header */}
      <div className="mb-8 flex w-full flex-row items-center gap-3">
        {icon}
        <h2 className="text-title-md2 font-bold text-black dark:text-white">{title}</h2>
      </div>
      <div className="flex-1 min-w-[400]]">
        {/* Card Content */}
        {children}
      </div>
    </div>
  );
}