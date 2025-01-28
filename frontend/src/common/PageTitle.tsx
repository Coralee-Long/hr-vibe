import { useEffect } from "react";
import { useLocation } from "react-router-dom";

type PageTitleProps = {
  title: string;
};

export const PageTitle = ({ title }: PageTitleProps) => {
  const location = useLocation();

  useEffect(() => {
    document.title = title;
  }, [location.pathname, title]); // Only track pathname changes

  return null; // This component doesn't render anything
};
