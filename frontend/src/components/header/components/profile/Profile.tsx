import { type Dispatch, type SetStateAction } from "react";
import classes from "./Profile.module.scss";
import { useAuthentication } from "../../../../features/authentication/contexts/AuthenticationContextProvider";
import { Button } from "../../../button/Button";
import { Link, useNavigate } from "react-router-dom";

interface ProfileProps {
  setShowProfileMenu: Dispatch<SetStateAction<boolean>>;
  setShowNavigationMenu: Dispatch<SetStateAction<boolean>>;
  showProfileMenu: boolean;
}

export function Profile({
  setShowProfileMenu,
  setShowNavigationMenu,
  showProfileMenu,
}: ProfileProps) {
  const { user, logout } = useAuthentication();
  const navigate = useNavigate();

  return (
    <div className={classes.root}>
      <button
        className={classes.toggle}
        onClick={() => {
          setShowProfileMenu((prev) => !prev);
          if (window.innerWidth <= 1080) {
            setShowNavigationMenu(false);
          }
        }}
      >
        <img
          className={`${classes.top} ${classes.avatar}`}
          src={user?.profilePicture || "/avatar.jpg"}
          alt="profile picture"
        />
        <div className={classes.name}>
          <div>{user?.firstName + " " + user?.lastName?.charAt(0) + "."}</div>
        </div>
      </button>

      {showProfileMenu && (
        <div className={classes.menu}>
          <div className={classes.content}>
            <img
              className={`${classes.left} ${classes.avatar}`}
              src={user?.profilePicture || "/avatar.jpg"}
              alt="profile picture"
            />
            <div className={classes.right}>
              <div className={classes.name}>
                {user?.firstName + " " + user?.lastName}
              </div>
              <div className={classes.title}>
                {"Student" + " at " + user?.college}
              </div>
            </div>
          </div>
          <div className={classes.links}>
            <Button
              size="small"
              outline
              onClick={() => {
                setShowProfileMenu(false);
                navigate("/profile/" + user?.id);
              }}
            >
              View Profile
            </Button>

            <Link
              to="/settings"
              onClick={() => {
                setShowProfileMenu(false);
              }}
            >
              Settings and Privacy
            </Link>

            <Link
              to="/logout"
              onClick={(e) => {
                e.preventDefault();
                logout();
              }}
            >
              Logout
            </Link>
          </div>
        </div>
      )}
    </div>
  );
}
