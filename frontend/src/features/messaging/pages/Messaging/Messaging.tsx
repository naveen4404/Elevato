import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { usePageTitle } from "../../../../hooks/usePageTitle";
import { RightSideBar } from "../../../feed/components/RightSideBar/RightSideBar";
import classes from "./Messaging.module.scss";
import { Conversations } from "../../components/conversations/Conversations";
import { useEffect, useState } from "react";

export function Messaging() {
  usePageTitle("Messaging");
  const navigate = useNavigate();
  const location = useLocation();
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);
  const onConversation = location.pathname.includes("conversations");

  useEffect(() => {
    const handleWidth = () => {
      setWindowWidth(window.innerWidth);
    };

    window.addEventListener("resize", handleWidth);

    return () => window.removeEventListener("resize", handleWidth);
  });

  return (
    <div className={classes.root}>
      <div className={classes.messaging}>
        <div
          className={classes.sidebar}
          style={{
            display: windowWidth >= 1024 || !onConversation ? "block" : "none",
          }}
        >
          <div className={classes.header}>
            <h1>Messaging</h1>
            <button
              onClick={() => {
                navigate("conversations/new");
              }}
              className={classes.new}
            >
              +
            </button>
          </div>
          <Conversations />
        </div>

        <Outlet />
      </div>
      <div className={classes.follows}>
        <RightSideBar />
      </div>
    </div>
  );
}
