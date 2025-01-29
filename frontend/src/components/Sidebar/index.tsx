import { useEffect, useRef, useState } from "react";
import { NavLink, useMatch, useNavigate } from "react-router-dom";
import SidebarLinkGroup from "./SidebarLinkGroup";
import Logo from "@/images/logo/hrvibe-logo.png";
import LogoText from "@/images/logo/hrvibe-text2.png";
import { DashboardIcon } from "@/icons/DashboardIcon.tsx";
import { DropDownCaratIcon } from "@/icons/DropDownCaratIcon.tsx";
import clsx from "clsx";

interface SidebarProps {
  sidebarOpen: boolean;
  setSidebarOpen: (arg: boolean) => void;
}

const Sidebar = ({ sidebarOpen, setSidebarOpen }: SidebarProps) => {
  const match = useMatch("/dashboard/*");
  const navigate = useNavigate();

  const trigger = useRef<HTMLButtonElement | null>(null);
  const sidebar = useRef<HTMLElement | null>(null);

  const [sidebarExpanded] = useState(
    localStorage.getItem("sidebar-expanded") === "true"
  );

  // Persist sidebar state in localStorage
  useEffect(() => {
    localStorage.setItem("sidebar-expanded", sidebarExpanded.toString());
  }, [sidebarExpanded]);

  // Close sidebar when clicking outside
  useEffect(() => {
    const clickHandler = (event: MouseEvent) => {
      if (
        sidebar.current &&
        !sidebar.current.contains(event.target as Node) &&
        trigger.current &&
        !trigger.current.contains(event.target as Node)
      ) {
        setSidebarOpen(false);
      }
    };
    document.addEventListener("click", clickHandler);
    return () => document.removeEventListener("click", clickHandler);
  }, [sidebarOpen]);

  // Close sidebar on Escape key press
  useEffect(() => {
    const keyHandler = (event: KeyboardEvent) => {
      if (event.key === "Escape") {
        setSidebarOpen(false);
      }
    };
    document.addEventListener("keydown", keyHandler);
    return () => document.removeEventListener("keydown", keyHandler);
  }, []);

  return (
    <aside
      ref={sidebar}
      className={clsx(
        "absolute left-0 top-0 z-50 flex h-screen w-72 flex-col overflow-y-hidden bg-black transition-transform duration-300 ease-linear dark:bg-boxdark lg:static lg:translate-x-0",
        sidebarOpen ? "translate-x-0" : "-translate-x-full"
      )}
    >
      {/* Sidebar Header */}
      <div className="flex items-center justify-between px-6 py-6">
        <NavLink to="/dashboard">
          <div className="flex items-center">
            <img src={Logo} alt="HRVibe Logo" className="w-16 h-16" />
            <img src={LogoText} alt="HRVibe Text" className="h-10 pl-3" />
          </div>
        </NavLink>

        <button
          ref={trigger}
          onClick={() => setSidebarOpen(!sidebarOpen)}
          aria-label="Toggle Sidebar"
          className="block lg:hidden"
        >
          <svg
            className="fill-current"
            width="20"
            height="18"
            viewBox="0 0 20 18"
          >
            <path
              d="M19 8.175H2.98748L9.36248 1.6875C9.69998 1.35 9.69998 0.825 9.36248 0.4875C9.02498 0.15 8.49998 0.15 8.16248 0.4875L0.399976 8.3625C0.0624756 8.7 0.0624756 9.225 0.399976 9.5625L8.16248 17.4375C8.31248 17.5875 8.53748 17.7 8.76248 17.7C8.98748 17.7 9.17498 17.625 9.36248 17.475C9.69998 17.1375 9.69998 16.6125 9.36248 16.275L3.02498 9.8625H19C19.45 9.8625 19.825 9.4875 19.825 9.0375C19.825 8.55 19.45 8.175 19 8.175Z"
            />
          </svg>
        </button>
      </div>

      {/* Sidebar Content */}
      <div className="no-scrollbar flex flex-col overflow-y-auto">
        <nav className="mt-5 py-4 px-4">
          <h3 className="mb-4 ml-4 text-sm font-semibold text-bodydark2">
            MENU
          </h3>
          <ul className="mb-6 flex flex-col gap-1.5">
            {/* Dashboard */}
            <SidebarLinkGroup activeCondition={!!match}>
              {(handleClick, open) => (
                <>
                  <div
                    className={clsx(
                      "group relative flex items-center justify-between gap-2.5 rounded-sm px-4 py-2 font-medium text-bodydark1 transition-colors duration-300 ease-in-out hover:bg-graydark dark:hover:bg-meta-4",
                      match && "bg-graydark dark:bg-meta-4"
                    )}
                  >
                    {/* Clicking here navigates to /dashboard */}
                    <div
                      onClick={() => navigate("/dashboard")}
                      className="flex items-center gap-2.5 cursor-pointer"
                    >
                      <DashboardIcon />
                      Dashboard
                    </div>

                    {/* Clicking here toggles the dropdown */}
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        handleClick();
                      }}
                      aria-label="Toggle Submenu"
                      className="p-1"
                    >
                      <DropDownCaratIcon open={open} />
                    </button>
                  </div>

                  {/* Dropdown Menu */}
                  <div className={`translate transform overflow-hidden ${!open && "hidden"}`}>
                    <ul className="mt-4 mb-5.5 flex flex-col gap-2.5 pl-6">
                      {[
                        { path: "/dashboard/insights", label: "Insights" },
                        { path: "/dashboard/sleep", label: "Sleep & Stress" },
                        { path: "/dashboard/activities", label: "Activities" },
                        { path: "/dashboard/training", label: "Training" },
                        { path: "/dashboard/hrv", label: "HRV & Recovery" },
                      ].map(({ path, label }) => (
                        <li key={path}>
                          <NavLink
                            to={path}
                            className={({ isActive }) =>
                              `group relative flex items-center gap-2.5 rounded-md px-4 font-medium text-bodydark2 duration-300 ease-in-out hover:text-white ${
                                isActive && "!text-white"
                              }`
                            }
                          >
                            {label}
                          </NavLink>
                        </li>
                      ))}
                    </ul>
                  </div>
                </>
              )}
            </SidebarLinkGroup>

            {/* Other Menu Items */}
            {/*<li>*/}
            {/*  <NavLink to="/dashboard" className="sidebar-link">*/}
            {/*    Other Links*/}
            {/*  </NavLink>*/}
            {/*</li>*/}
          </ul>
        </nav>
      </div>
    </aside>
  );
};

export default Sidebar;
