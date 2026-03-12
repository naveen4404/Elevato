import { usePageTitle } from "../../../../hooks/usePageTitle";
import classes from "./Network.module.scss";

export function Network() {
  usePageTitle("Network");
  return <div className={classes.root}>Network</div>;
}
