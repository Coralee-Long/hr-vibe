import { Link, useNavigate } from "react-router-dom";
import Logo from "@/images/logo/hrvibe-logo.png";
import LogoText from "@/images/logo/hrvibe-text2.png";
import { useAuth } from "@/context/AuthContext.tsx";
import { SuccessNotification } from "@/common/SuccessNotification.tsx";
import { useState } from "react";

export const Login = () => {
  const { loginAsGuest } = useAuth(); // Use AuthContext for guest login
  const navigate = useNavigate();
  const [notificationVisible, setNotificationVisible] =
    useState<boolean>(false);
  const [notificationType, setNotificationType] = useState<
    "admin" | "guest" | null
  >(null);

  // Compute backend URL (if needed)
  const backendUrl =
    window.location.host === "localhost:5173"
      ? "http://localhost:8080"
      : window.location.origin;

  // Temporary debug flag to disable redirect so we can style the notification.
  const debugNoRedirect = false; // Set to false in order to re-enable redirect

  /**
   * GitHub OAuth Login:
   * Redirects the browser to the backend OAuth2 endpoint.
   * For demo purposes, we set the notification type to "admin".
   */
  const handleGithubLogin = () => {
    setNotificationType("admin");
    setNotificationVisible(true);
    if (!debugNoRedirect) {
      // In a real-world scenario the OAuth2 flow will eventually redirect the browser.
      window.location.href = `${backendUrl}/oauth2/authorization/github`;
    }
  };

  /**
   * Guest Login:
   * Calls the loginAsGuest function and navigates to /dashboard on success.
   */
  const handleGuestLogin = async () => {
    await loginAsGuest();
    setNotificationType("guest");
    setNotificationVisible(true);
    if (!debugNoRedirect) {
      setTimeout(() => {
        navigate("/dashboard");
      }, 1000);
    }
  };

  return (
    <>
      {/* Login Form */}
      <div className="rounded-sm border border-stroke bg-white shadow-default dark:border-strokedark dark:bg-boxdark">
        <div className="flex flex-col xl:flex-row items-center">
          {/* Left Side (Logo) -> on Top on smaller screens */}
          <div className="w-full xl:w-1/2">
            <div className="text-center py-10 px-26 xl:py-17.5">
              <Link
                className="flex flex-row justify-center  mb-5.5 items-center xl:flex-col"
                to="/"
              >
                <img
                  className="min-w-[150px] w-[50%] md:w-[25%] xl:w-[65%]"
                  src={Logo}
                  alt="HRVibe Logo"
                />
                <img
                  className="hidden md:block px-8 w-[40%] xl:w-[65%]"
                  src={LogoText}
                  alt="HRVibe"
                />
              </Link>
            </div>
          </div>

          {/* Right Side (Login) -> on Bottom on smaller screens */}
          <div className="flex flex-col justify-center items-center w-full border-stroke dark:border-strokedark xl:w-1/2 xl:border-l-2">
            <div className="w-full p-4 sm:p-12.5 xl:p-17.5 flex flex-col items-center justify-start">
              <h2 className="text-center mb-9 text-2xl font-bold text-black dark:text-white sm:text-title-xl2">
                Welcome to HRVibe
              </h2>

              {/* GitHub Login Button */}
              <button
                onClick={handleGithubLogin}
                className="w-full cursor-pointer rounded-lg border border-primary bg-primary p-4 text-white transition hover:bg-opacity-90 flex items-center justify-center gap-3"
              >
                {/* SVG for GitHub icon */}
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                  <path
                    fillRule="evenodd"
                    clipRule="evenodd"
                    d="M10 0C4.477 0 0 4.477 0 10c0 4.418 2.865 8.166 6.839 9.5.5.093.682-.217.682-.482 0-.237-.009-.866-.014-1.699-2.782.605-3.369-1.342-3.369-1.342-.454-1.152-1.11-1.46-1.11-1.46-.907-.62.069-.608.069-.608 1.002.07 1.529 1.03 1.529 1.03.89 1.526 2.337 1.086 2.905.831.09-.645.348-1.086.634-1.337-2.22-.253-4.555-1.111-4.555-4.945 0-1.091.39-1.983 1.03-2.682-.103-.253-.446-1.271.097-2.647 0 0 .84-.269 2.75 1.026A9.57 9.57 0 0110 4.853c.85.004 1.705.115 2.503.337 1.91-1.295 2.75-1.026 2.75-1.026.543 1.376.2 2.394.097 2.647.64.7 1.03 1.591 1.03 2.682 0 3.844-2.338 4.689-4.564 4.938.359.308.678.917.678 1.851 0 1.337-.012 2.418-.012 2.746 0 .268.18.579.688.482C17.137 18.165 20 14.417 20 10c0-5.523-4.477-10-10-10z"
                    fill="currentColor"
                  />
                </svg>
                Sign in with GitHub
              </button>
              {/* Guest Mode Login Button */}
              <button
                onClick={handleGuestLogin}
                className="mt-4 w-full cursor-pointer rounded-lg border border-gray-500 bg-gray-200 p-4 text-gray-800 transition hover:bg-gray-300 dark:border-gray-700 dark:bg-gray-700 dark:text-white dark:hover:bg-gray-600"
              >
                Try as Guest
              </button>
              <div className="h-20 w-full xl:h-24">
                {/* Notification for login success */}
                {notificationType && (
                  <SuccessNotification
                    debugNoRedirect={debugNoRedirect} // Set to false in order to re-enable redirect
                    loginType={notificationType}
                    visible={notificationVisible}
                    onClose={() => setNotificationVisible(false)}
                  />
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};
