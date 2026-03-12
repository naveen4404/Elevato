import { useEffect, useState } from "react";
import { request } from "../../../../utils/api";
import {
  useAuthentication,
  type User,
} from "../../../authentication/contexts/AuthenticationContextProvider";
import { useWebSocket } from "../../../websocket/WsContextProvider";
import classes from "./Conversations.module.scss";
import { ConversationCard } from "./components/ConversationCard";

export interface MessageInterface {
  id: number;
  sender: User;
  receiver: User;
  content: string;
  read: boolean;
  createdAt: string;
}

export interface CoversationInterface {
  id: number;
  author: User;
  recipient: User;
  messages: MessageInterface[];
}

export function Conversations() {
  const [conversations, setConversations] = useState<CoversationInterface[]>(
    [],
  );
  const { user } = useAuthentication();
  const WsClient = useWebSocket();

  useEffect(() => {
    request<CoversationInterface[]>({
      endpoint: "/api/v1/messaging/conversations",
      method: "GET",
      onSuccess: (data) => {
        setConversations(data);
      },
      onFailure: (message) => {
        console.error(message);
      },
    });
  }, []);

  useEffect(() => {
    const subscription = WsClient?.subscribe(
      "/topic/users/" + user?.id + "/conversations",
      (message) => {
        const conversation: CoversationInterface = JSON.parse(message.body);
        setConversations((PrevConversations) => {
          const index = PrevConversations.findIndex(
            (c) => c.id === conversation.id,
          );

          if (index === -1) {
            return [conversation, ...PrevConversations];
          }

          return PrevConversations;
        });
      },
    );

    return () => subscription?.unsubscribe();
  }, [user?.id, WsClient, conversations]);

  return (
    <div className={classes.root}>
      {conversations.map((conversation) => {
        return (
          <ConversationCard key={conversation.id} conversation={conversation} />
        );
      })}
      {conversations.length === 0 && (
        <div
          className={classes.welcome}
          style={{
            padding: "1rem",
          }}
        >
          No conversation to display.
        </div>
      )}
    </div>
  );
}
