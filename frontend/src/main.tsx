import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";
import { App } from "./App.tsx";
// Import the Axios configuration to apply defaults globally.
import "./config/axiosConfig.ts";
import "./css/style.css";
import "./css/satoshi.css";
import "./css/simple-datatables.css";

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>
);
