import { Outlet } from "react-router-dom";
import classes from "./ApplicationLayout.module.scss";
import { Header } from "../header/Header";
import { WsContextProvider } from "../../features/websocket/WsContextProvider";

export function ApplicationLayout() {
  return (
    <WsContextProvider>
      <div className={classes.root}>
        <Header />
        <main className={classes.container}>
          <Outlet />
        </main>
      </div>
    </WsContextProvider>
  );
}
