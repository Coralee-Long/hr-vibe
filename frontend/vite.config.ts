// vite.config.ts
import { defineConfig } from "vite";
import svgr from "vite-plugin-svgr";
import react from "@vitejs/plugin-react";
import path from "node:path";

export default defineConfig({
  plugins: [
    // SVGR plugin is configured without exportAsDefault
    svgr({
      // Optional SVGR options, for example:
      svgrOptions: {
        icon: true,
      },
    }),
    react(),
  ],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "src"),
    },
  },
});
