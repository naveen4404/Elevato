import { useEffect, useState } from "react";
import type { CoversationInterface, MessageInterface } from "../Conversations";
import classes from "./ConversationCard.module.scss";
import { useAuthentication } from "../../../../authentication/contexts/AuthenticationContextProvider";
import { useNavigate, useParams } from "react-router-dom";
import { useWebSocket } from "../../../../websocket/WsContextProvider";

interface ConversationCardProps {
  conversation: CoversationInterface;
}

export function ConversationCard(props: ConversationCardProps) {
  const [conversation, setConversation] = useState<CoversationInterface>(
    props.conversation,
  );
  const { user } = useAuthentication();
  const { id } = useParams();
  const navigate = useNavigate();
  const WsClient = useWebSocket();

  const conversationUserToDisplay =
    conversation.recipient.id === user?.id
      ? conversation.author
      : conversation.recipient;

  const unReadCount = conversation?.messages?.filter(
    (message) => message?.receiver.id === user?.id && !message?.read,
  ).length;

  useEffect(() => {
    const subscription = WsClient?.subscribe(
      "/topic/conversations/" + conversation.id + "/messages",
      (data) => {
        const message: MessageInterface = JSON.parse(data.body);
        setConversation((prevConversation) => {
          const index = prevConversation?.messages.findIndex(
            (m) => m.id === message.id,
          );
          if (index === -1) {
            return {
              ...prevConversation,
              messages: [...prevConversation.messages, message],
            };
          }

          return {
            ...prevConversation,
            messages: prevConversation.messages.map((m) =>
              m.id !== message.id ? m : message,
            ),
          };
        });
      },
    );

    return () => subscription?.unsubscribe();
  }, [WsClient, conversation.id]);

  return (
    <button
      key={conversation.id}
      className={`${classes.root} ${id && Number(id) === conversation.id ? classes.selected : ""}`}
      onClick={() => navigate(`/messaging/conversations/${conversation.id}`)}
    >
      <img
        className={classes.avatar}
        src={conversationUserToDisplay.profilePicture}
        alt=""
      />

      {unReadCount > 0 && <div className={classes.unread}>{unReadCount}</div>}

      <div>
        <div className={classes.name}>
          {conversationUserToDisplay.firstName}{" "}
          {conversationUserToDisplay.lastName}
        </div>
        <div className={classes.content}>
          {conversation.messages[conversation.messages.length - 1]?.content}
        </div>
      </div>
    </button>
  );
}
