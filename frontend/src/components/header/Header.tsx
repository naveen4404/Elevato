import { NavLink } from "react-router-dom";
import classes from "./Header.module.scss";
import { Input } from "../input/Input";
import { useAuthentication } from "../../features/authentication/contexts/AuthenticationContextProvider";
import { useEffect, useState } from "react";
import { Profile } from "./components/profile/Profile";

export function Header() {
  const { user } = useAuthentication();
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const [showNavigationMenu, setShowNavigationMenu] = useState(
    window.innerWidth > 1080 ? true : false,
  );

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 1080) {
        setShowNavigationMenu(true);
      } else {
        setShowNavigationMenu(false);
      }
    };
    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  return (
    <header className={classes.root}>
      <div className={classes.container}>
        <div className={classes.left}>
          <NavLink to="/">
            <img className={classes.logo} src="/favicon.ico" alt="logo" />
          </NavLink>
          <Input placeholder="Search" size="medium" />
        </div>
        <div className={classes.right}>
          {showNavigationMenu && (
            <ul>
              <li>
                <NavLink
                  to="/"
                  className={({ isActive }) => (isActive ? classes.active : "")}
                  onClick={() => {
                    setShowProfileMenu(false);
                    if (window.innerWidth <= 1080) {
                      setShowNavigationMenu(false);
                    }
                  }}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    x="0px"
                    y="0px"
                    width="100"
                    height="100"
                    viewBox="0 0 64 64"
                    fill="currentColor"
                  >
                    <path d="M 32 9.0019531 C 31.225957 9.0019531 30.451863 9.2247995 29.78125 9.671875 L 5.78125 25.671875 C 3.9538194 26.890604 3.4532611 29.390166 4.671875 31.21875 C 5.845859 32.97845 8.1947613 33.469534 10 32.417969 L 10 53 A 1.0001 1.0001 0 0 0 11 54 L 21 54 A 1.0001 1.0001 0 1 0 21 52 L 12 52 L 12 30.605469 A 1.0001 1.0001 0 0 0 10.445312 29.773438 L 9.109375 30.664062 C 8.1811589 31.283772 6.9564342 31.039446 6.3359375 30.109375 C 5.7165514 29.179959 5.9620556 27.955208 6.890625 27.335938 L 30.890625 11.335938 C 31.565399 10.886088 32.434601 10.886088 33.109375 11.335938 L 57.109375 27.335938 C 58.037944 27.955208 58.283449 29.179959 57.664062 30.109375 C 57.274555 30.693636 56.643757 31 55.996094 31 C 55.61407 31 55.234406 30.893474 54.890625 30.664062 L 53.554688 29.773438 A 1.0001 1.0001 0 0 0 52 30.605469 L 52 52 L 27 52 L 27 31 L 37 31 L 37 38.732422 C 36.576988 38.452531 36.125457 38.252859 35.654297 38.179688 C 34.786371 38.044899 33.91515 38.22673 33.214844 38.708984 C 32.514538 39.191238 32 40.035337 32 41 C 32 41.957056 32.519219 42.791667 33.216797 43.267578 C 33.914375 43.743489 34.779929 43.925905 35.646484 43.796875 C 36.119122 43.726499 36.574791 43.529252 37 43.25 L 37 49 A 1.0001 1.0001 0 1 0 39 49 L 39 43 C 39 42.247112 38.873416 41.587139 38.673828 40.998047 C 38.87393 40.410772 39 39.752159 39 39 L 39 30 A 1.0001 1.0001 0 0 0 38 29 L 26 29 A 1.0001 1.0001 0 0 0 25 30 L 25 53 A 1.0001 1.0001 0 0 0 26 54 L 53 54 A 1.0001 1.0001 0 0 0 54 53 L 54 32.394531 C 54.627636 32.761057 55.309379 33 55.996094 33 C 57.288431 33 58.561632 32.368489 59.328125 31.21875 C 60.546739 29.390166 60.046181 26.890604 58.21875 25.671875 L 34.21875 9.671875 C 33.548137 9.2247995 32.774043 9.0019531 32 9.0019531 z M 35.0625 40.138672 C 35.155446 40.135537 35.250185 40.141416 35.345703 40.15625 C 35.761884 40.22088 36.181269 40.491448 36.507812 41.005859 C 36.184018 41.504294 35.767551 41.756709 35.353516 41.818359 C 34.970071 41.875449 34.585625 41.778919 34.345703 41.615234 C 34.105781 41.451552 34 41.287444 34 41 C 34 40.688163 34.110462 40.518809 34.347656 40.355469 C 34.525552 40.232964 34.783662 40.148075 35.0625 40.138672 z"></path>
                  </svg>
                  <span>Home</span>
                </NavLink>
              </li>
              <li>
                <NavLink
                  onClick={() => {
                    setShowProfileMenu(false);
                    if (window.innerWidth <= 1080) {
                      setShowNavigationMenu(false);
                    }
                  }}
                  to="/network"
                  className={({ isActive }) => (isActive ? classes.active : "")}
                >
                  <svg
                    width="200"
                    height="200"
                    xmlns="http://www.w3.org/2000/svg"
                    viewBox="0 0 64 64"
                  >
                    <path
                      fill="currentColor"
                      d="M31.9 18.4c4.2 0 7.5-3.4 7.5-7.5S36 3.4 31.9 3.4s-7.5 3.4-7.5 7.5s3.3 7.5 7.5 7.5m0-10.6c1.7 0 3 1.4 3 3s-1.4 3-3 3s-3-1.4-3-3s1.3-3 3-3m-8.3 20.7c2.1-2.3 5.2-3.7 8.4-3.7s6.3 1.3 8.4 3.7c.4.5 1 .7 1.7.7c.5 0 1.1-.2 1.5-.6c.9-.8 1-2.3.1-3.2c-3-3.2-7.3-5.1-11.7-5.1s-8.7 1.9-11.7 5.1c-.8.9-.8 2.3.1 3.2s2.3.8 3.2-.1M13.4 50.1c4.2 0 7.5-3.4 7.5-7.5c0-4.2-3.4-7.5-7.5-7.5s-7.5 3.4-7.5 7.5c-.1 4.2 3.3 7.5 7.5 7.5m0-10.6c1.7 0 3 1.4 3 3c0 1.7-1.4 3-3 3s-3-1.4-3-3c-.1-1.6 1.3-3 3-3m-.3 12.3c-4.4 0-8.7 1.9-11.7 5.1c-.8.9-.8 2.3.1 3.2s2.3.8 3.2-.1c2.1-2.3 5.2-3.7 8.4-3.7s6.3 1.3 8.4 3.7c.4.5 1 .7 1.7.7c.5 0 1.1-.2 1.5-.6c.9-.8 1-2.3.1-3.2c-3-3.2-7.3-5.1-11.7-5.1m37.3-1.7c4.2 0 7.5-3.4 7.5-7.5c0-4.2-3.4-7.5-7.5-7.5c-4.2 0-7.5 3.4-7.5 7.5c0 4.2 3.4 7.5 7.5 7.5m0-10.6c1.7 0 3 1.4 3 3c0 1.7-1.4 3-3 3c-1.7 0-3-1.4-3-3s1.4-3 3-3M62.7 57c-3-3.2-7.3-5.1-11.7-5.1s-8.7 1.9-11.7 5.1c-.8.9-.8 2.3.1 3.2c.9.8 2.3.8 3.2-.1c2.1-2.3 5.2-3.7 8.4-3.7s6.3 1.3 8.4 3.7c.4.5 1 .7 1.7.7c.5 0 1.1-.2 1.5-.6c.8-.9.9-2.3.1-3.2M39.2 40.9c.5-1.1 0-2.5-1.1-3l-3.9-1.8V32c0-1.2-1-2.3-2.2-2.3s-2.3 1-2.3 2.3v4l-4 1.9c-1.1.5-1.6 1.9-1.1 3s1.9 1.6 3 1.1l4.2-2l4.5 2c.3.1.6.2.9.2c.8 0 1.6-.5 2-1.3"
                    />
                  </svg>
                  <span>Network</span>
                </NavLink>
              </li>
              <li>
                <NavLink
                  onClick={() => {
                    setShowProfileMenu(false);
                    if (window.innerWidth <= 1080) {
                      setShowNavigationMenu(false);
                    }
                  }}
                  to="/messaging"
                  className={({ isActive }) => (isActive ? classes.active : "")}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="200"
                    height="200"
                    viewBox="0 0 24 24"
                    fill="#525151"
                  >
                    <g fill="none" stroke="currentColor" strokeLinejoin="round">
                      <path d="M19.5 4.5h-18l3 5v7a3 3 0 0 0 3 3h12a3 3 0 0 0 3-3v-9a3 3 0 0 0-3-3Z" />
                      <path
                        strokeLinecap="round"
                        d="M7.5 8h12m-12 3.5h12M7.5 15H16"
                      />
                    </g>
                  </svg>
                  <span>Messaging</span>
                </NavLink>
              </li>
              <li>
                <NavLink
                  onClick={() => {
                    setShowProfileMenu(false);
                    if (window.innerWidth <= 1080) {
                      setShowNavigationMenu(false);
                    }
                  }}
                  to="/notifications"
                  className={({ isActive }) => (isActive ? classes.active : "")}
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="200"
                    height="200"
                    viewBox="0 0 24 24"
                  >
                    <path
                      fill="currentColor"
                      d="M4 8a8 8 0 1 1 16 0v4.697l2 3V20h-5.611a4.502 4.502 0 0 1-8.777 0H2v-4.303l2-3V8Zm5.708 12a2.5 2.5 0 0 0 4.584 0H9.708ZM12 2a6 6 0 0 0-6 6v5.303l-2 3V18h16v-1.697l-2-3V8a6 6 0 0 0-6-6Z"
                    />
                  </svg>
                  <span>Notications</span>
                </NavLink>
              </li>
            </ul>
          )}

          <button
            className={classes.toggle}
            onClick={() => {
              setShowNavigationMenu((prev) => !prev);
              setShowProfileMenu(false);
            }}
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 448 512"
              fill="currentColor"
            >
              <path d="M0 96C0 78.3 14.3 64 32 64l384 0c17.7 0 32 14.3 32 32s-14.3 32-32 32L32 128C14.3 128 0 113.7 0 96zM0 256c0-17.7 14.3-32 32-32l384 0c17.7 0 32 14.3 32 32s-14.3 32-32 32L32 288c-17.7 0-32-14.3-32-32zM448 416c0 17.7-14.3 32-32 32L32 448c-17.7 0-32-14.3-32-32s14.3-32 32-32l384 0c17.7 0 32 14.3 32 32z" />
            </svg>
            <span>Menu</span>
          </button>
          {user && (
            <Profile
              setShowNavigationMenu={setShowNavigationMenu}
              setShowProfileMenu={setShowProfileMenu}
              showProfileMenu={showProfileMenu}
            />
          )}
        </div>
      </div>
    </header>
  );
}
