import type { User } from "../../../authentication/contexts/AuthenticationContextProvider";
import type { MessageInterface } from "../conversations/Conversations";
import { Message } from "./Components/Message";
import classes from "./Messages.module.scss";

interface MessagesProps {
  messages: MessageInterface[];
  user: User | null;
}

export function Messages({ messages, user }: MessagesProps) {
  return (
    <div className={classes.root}>
      {messages.map((message) => {
        return <Message key={message.id} message={message} user={user} />;
      })}
    </div>
  );
}
