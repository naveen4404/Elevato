import { useEffect, useState, type Dispatch, type SetStateAction } from "react";
import {
  useAuthentication,
  type User,
} from "../../../authentication/contexts/AuthenticationContextProvider";
import classes from "./Notifications.module.scss";
import { request } from "../../../../utils/api";
import { LeftSideBar } from "../../components/LeftSideBar/LeftSideBar";
import { RightSideBar } from "../../components/RightSideBar/RightSideBar";
import { useNavigate } from "react-router-dom";
import { TimeAgo } from "../../components/TimeAgo/TimeAgo";
import { useWebSocket } from "../../../websocket/WsContextProvider";
import { usePageTitle } from "../../../../hooks/usePageTitle";

type NotificationType = "LIKE" | "COMMENT";

export interface Notification {
  id: number;
  recipient: User;
  actor: User;
  read: boolean;
  type: NotificationType;
  resourceId: number;
  createdAt: string;
}

export function Notifications() {
  usePageTitle("Notifications");
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const WsClient = useWebSocket();
  const { user } = useAuthentication();
  useEffect(() => {
    const fetchNotifications = async () => {
      await request<Notification[]>({
        endpoint: "/api/v1/notifications",
        onSuccess: setNotifications,
        onFailure: (error) => console.log(error),
      });
    };

    fetchNotifications();
  }, []);

  useEffect(() => {
    const subscription = WsClient?.subscribe(
      `/topic/users/${user?.id}/notifications`,
      (message) => {
        const nt: Notification = JSON.parse(message.body);
        const index = notifications.findIndex(
          (notification) => notification.id === nt.id,
        );
        if (index === -1) {
          setNotifications([nt, ...notifications]);
        } else {
          setNotifications(
            notifications.map((notification) =>
              notification.id === nt.id ? nt : notification,
            ),
          );
        }
      },
    );

    return () => subscription?.unsubscribe();
  }, [user?.id, WsClient, notifications]);

  return (
    <div className={classes.root}>
      <div className={classes.left}>
        <LeftSideBar />
      </div>
      <div className={classes.center}>
        {notifications.map((notification) => (
          <Notification
            key={notification.id}
            notification={notification}
            setNotifications={setNotifications}
          />
        ))}
        {notifications.length === 0 && (
          <p
            style={{
              padding: "1rem",
            }}
          >
            No notifications
          </p>
        )}
      </div>
      <div className={classes.right}>
        <RightSideBar />
      </div>
    </div>
  );
}

function Notification({
  notification,
  setNotifications,
}: {
  notification: Notification;

  setNotifications: Dispatch<SetStateAction<Notification[]>>;
}) {
  const navigate = useNavigate();

  function markNotificationAsRead(notificationId: number) {
    request({
      endpoint: `/api/v1/notifications/${notificationId}`,
      method: "PUT",
      onSuccess: () => {
        setNotifications((prev) =>
          prev.map((notification) =>
            notification.id === notificationId
              ? { ...notification, isRead: true }
              : notification,
          ),
        );
      },
      onFailure: (error) => console.log(error),
    });
  }
  return (
    <button
      onClick={() => {
        markNotificationAsRead(notification.id);
        navigate(`/posts/${notification.resourceId}`);
      }}
      className={
        notification.read
          ? classes.notification
          : `${classes.notification} ${classes.unread}`
      }
    >
      <img
        src={notification.actor.profilePicture}
        alt=""
        className={classes.avatar}
      />

      <p
        style={{
          marginRight: "auto",
        }}
      >
        <strong>
          {notification.actor.firstName + " " + notification.actor.lastName}
        </strong>{" "}
        {notification.type === "LIKE" ? "liked" : "commented on"} your post.
      </p>
      <TimeAgo date={notification.createdAt} />
    </button>
  );
}
