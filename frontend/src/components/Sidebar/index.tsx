import { useEffect, useRef, useState } from "react";
import { NavLink, useMatch } from "react-router-dom";
import SidebarLinkGroup from "./SidebarLinkGroup";
import Logo from "@/images/logo/hrvibe-logo.png";
import clsx from "clsx";

interface SidebarProps {
  sidebarOpen: boolean;
  setSidebarOpen: (arg: boolean) => void;
}

const Sidebar = ({ sidebarOpen, setSidebarOpen }: SidebarProps) => {
  const match = useMatch("/dashboard/*");

  const trigger = useRef<HTMLButtonElement | null>(null);
  const sidebar = useRef<HTMLElement | null>(null);

  const [sidebarExpanded, setSidebarExpanded] = useState(
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
        <NavLink to="/">
          <img src={Logo} alt="HRVibe Logo" />
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
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              d="M19 8.175H2.98748L9.36248 1.6875C9.69998 1.35 9.69998 0.825 9.36248 0.4875C9.02498 0.15 8.49998 0.15 8.16248 0.4875L0.399976 8.3625C0.0624756 8.7 0.0624756 9.225 0.399976 9.5625L8.16248 17.4375C8.31248 17.5875 8.53748 17.7 8.76248 17.7C8.98748 17.7 9.17498 17.625 9.36248 17.475C9.69998 17.1375 9.69998 16.6125 9.36248 16.275L3.02498 9.8625H19C19.45 9.8625 19.825 9.4875 19.825 9.0375C19.825 8.55 19.45 8.175 19 8.175Z"
              fill=""
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
                  <NavLink
                    to="#"
                    className={clsx(
                      "group relative flex items-center gap-2.5 rounded-sm px-4 py-2 font-medium text-bodydark1 transition-colors duration-300 ease-in-out hover:bg-graydark dark:hover:bg-meta-4",
                      match && "bg-graydark dark:bg-meta-4"
                    )}
                    onClick={(e) => {
                      e.preventDefault();
                      if (sidebarExpanded) {
                        handleClick();
                      } else {
                        setSidebarExpanded(true);
                      }
                    }}
                  >
                    <svg className="fill-current" width="18" height="18">
                      {/* Icon path */}
                    </svg>
                    Dashboard
                    <svg
                      className={clsx(
                        "absolute right-4 top-1/2 -translate-y-1/2 fill-current transition-transform",
                        open && "rotate-180"
                      )}
                      width="20"
                      height="20"
                    >
                      <path d="M4.41107 6.9107C4.73651 6.58527 5.26414 6.58527 5.58958 6.9107L10.0003 11.3214L14.4111 6.91071C14.7365 6.58527 15.2641 6.58527 15.5896 6.91071C15.915 7.23614 15.915 7.76378 15.5896 8.08922L10.5896 13.0892C10.2641 13.4147 9.73651 13.4147 9.41107 13.0892L4.41107 8.08922C4.08563 7.76378 4.08563 7.23614 4.41107 6.9107Z" />
                    </svg>
                  </NavLink>
                </>
              )}
            </SidebarLinkGroup>

            {/* Other Menu Items */}
            <li>
              <NavLink to="/profile" className="sidebar-link">
                Profile
              </NavLink>
            </li>
          </ul>
        </nav>
      </div>
    </aside>
  );
};

export default Sidebar;
