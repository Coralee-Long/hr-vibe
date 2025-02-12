import React, { useState } from "react";

type TooltipProps = {
  position: "top" | "right" | "bottom" | "left";
  tooltipText: string;
  active: boolean; // Determines whether the tooltip should show on click.
  children: React.ReactNode;
  debugForceVisible?: boolean; // For debugging: always show the tooltip.
};

export const Tooltip = ({
                          position,
                          tooltipText,
                          active,
                          children,
                          debugForceVisible = false,
                        }: TooltipProps) => {
  const [visible, setVisible] = useState(false);

  const handleClick = (e: React.MouseEvent) => {
    // Only intercept clicks if the tooltip is active.
    if (active) {
      e.stopPropagation();
      setVisible((prev) => !prev);
    }
  };

  let tooltipClasses = "";
  let arrowClasses = "";

  switch (position) {
    case "top":
      tooltipClasses =
        "absolute bottom-full left-1/2 z-20 mb-3 -translate-x-1/2 whitespace-nowrap rounded bg-black px-4.5 py-1.5 text-sm font-medium text-white";
      arrowClasses =
        "absolute bottom-[-3px] left-1/2 -z-10 h-2 w-2 -translate-x-1/2 rotate-45 rounded-sm bg-black";
      break;
    case "right":
      tooltipClasses =
        "absolute left-full top-1/2 z-20 ml-3 -translate-y-1/2 whitespace-nowrap rounded bg-black px-4.5 py-1.5 text-sm font-medium text-white";
      arrowClasses =
        "absolute left-[-3px] top-1/2 -z-10 h-2 w-2 -translate-y-1/2 rotate-45 rounded-sm bg-black";
      break;
    case "bottom":
      tooltipClasses =
        "absolute left-1/2 top-full z-20 mt-3 -translate-x-1/2 whitespace-nowrap rounded bg-black px-4.5 py-1.5 text-sm font-medium text-white";
      arrowClasses =
        "absolute left-1/2 top-[-3px] -z-10 h-2 w-2 -translate-x-1/2 rotate-45 rounded-sm bg-black";
      break;
    case "left":
      tooltipClasses =
        "absolute right-full top-1/2 z-20 mr-3 -translate-y-1/2 whitespace-nowrap rounded bg-black px-4.5 py-1.5 text-sm font-medium text-white";
      arrowClasses =
        "absolute right-[-3px] top-1/2 -z-10 h-2 w-2 -translate-y-1/2 rotate-45 rounded-sm bg-black";
      break;
    default:
      break;
  }

  // For debugging, if debugForceVisible is true, always show the tooltip.
  const shouldShow = active && (visible || debugForceVisible);

  return (
    <div className="relative inline-block" onClick={active ? handleClick : undefined}>
      {children}
      {shouldShow && (
        <div className={tooltipClasses}>
          <span className={arrowClasses}></span>
          {tooltipText}
        </div>
      )}
    </div>
  );
};
