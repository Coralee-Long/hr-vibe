import ThemeToggle from "./ThemeToggle.tsx";
import Logo from "@/images/logo/hrvibe-logo.png";
import LogoText from "@/images/logo/hrvibe-text2.png";
import { Link } from "react-router-dom";

export const ThemeToggleHeader = () => {
  return (
    <header className="sticky top-0 z-999 flex w-full bg-white drop-shadow-1 dark:bg-boxdark dark:drop-shadow-none">
      <div className="flex flex-grow items-center justify-between px-4 py-4 shadow-2 md:px-6 2xl:px-11">
        {/* <!-- Logo & Text --> */}
        <Link to="/">
          <div className="flex items-center justify-center">
            <img src={Logo} alt="HRVibe Logo" className="w-12 h-12" />
            <img src={LogoText} alt="HRVibe Text" className="h-8 pl-3" />
          </div>
        </Link>
        {/* <!-- Theme Toggler --> */}
        <ThemeToggle />
      </div>
    </header>
  );
};
