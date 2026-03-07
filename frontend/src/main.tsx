import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import "./index.scss";
import { EmailVerification } from "./features/authentication/pages/email-verification/EmailVerification";
import { Login } from "./features/authentication/pages/login/Login";
import { Signup } from "./features/authentication/pages/signup/Signup";
import { PasswordReset } from "./features/authentication/pages/password-reset/PasswordReset";

import { AuthenticationContextProvider } from "./features/authentication/contexts/AuthenticationContextProvider";
import { AuthenticationLayout } from "./features/authentication/components/AuthenticationLayout/AuthenticationLayout";
import { ApplicationLayout } from "./components/ApplicationLayout/ApplicationLayout";
import { Messaging } from "./features/feed/pages/messaging/Messaging";
import { Network } from "./features/feed/pages/network/Network";
import { Notifications } from "./features/feed/pages/notifications/Notifications";
import { Feed } from "./features/feed/pages/Feed/Feed";
import { Profile } from "./features/authentication/pages/profile/Profile";

const router = createBrowserRouter([
  {
    element: <AuthenticationContextProvider />,
    children: [
      {
        path: "/",
        element: <ApplicationLayout />,
        children: [
          {
            index: true,
            element: <Feed />,
          },
          {
            path: "messaging",
            element: <Messaging />,
          },
          {
            path: "network",
            element: <Network />,
          },
          {
            path: "notifications",
            element: <Notifications />,
          },
        ],
      },

      {
        path: "/authentication",
        element: <AuthenticationLayout />,
        children: [
          {
            path: "login",
            element: <Login />,
          },
          {
            path: "signup",
            element: <Signup />,
          },
          {
            path: "request-password-reset",
            element: <PasswordReset />,
          },
          {
            path: "verify-email",
            element: <EmailVerification />,
          },
          {
            path: "profile/:id",
            element: <Profile />,
          },
        ],
      },
      {
        path: "*",
        element: <Feed />,
      },
    ],
  },
]);

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <RouterProvider router={router} />
  </StrictMode>,
);
