import {
  createContext,
  useContext,
  useEffect,
  useState,
  type ReactNode,
} from "react";
import { CompatClient, Stomp } from "@stomp/stompjs";
const WsContext = createContext<CompatClient | null>(null);
export const useWebSocket = () => useContext(WsContext);

export function WsContextProvider({ children }: { children: ReactNode }) {
  const [stompClient, setStompClient] = useState<CompatClient | null>(null);

  useEffect(() => {
    const client = Stomp.client("ws://localhost:8080/ws");
    client.connect(
      {},
      () => {
        console.log("Connected to websocket");
        setStompClient(client);
      },
      (error: unknown) => {
        console.log("Error connecting to websocket", error);
      },
    );

    return () => {
      if (client.connected) {
        client.disconnect(() => {
          console.log("Disconnected from websocket");
        });
      }
    };
  }, []);

  return (
    <WsContext.Provider value={stompClient}>{children}</WsContext.Provider>
  );
}
