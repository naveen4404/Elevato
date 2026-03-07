import { Link, Outlet } from "react-router-dom";
import classes from "./AuthenticationLayout.module.scss";

export function AuthenticationLayout() {
  return (
    <div className={classes.root}>
      <header className={classes.container}>
        <Link to="/">
          <img src="/logo.svg" alt="" className={classes.logo} />
        </Link>
      </header>
      <main className={classes.container}>
        <Outlet />
      </main>
      <footer>
        <ul className={classes.container}>
          <li>
            <img src="/logo-dark.svg" alt="" />
            <span>©2026</span>
          </li>

          <li>
            <a href="">User Agreement</a>
          </li>
          <li>
            <a href="">Privacy Policy</a>
          </li>
          <li>
            <a href="">Copyright Policy</a>
          </li>
        </ul>
      </footer>
    </div>
  );
}
