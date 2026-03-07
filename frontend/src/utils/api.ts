const BASE_URL = import.meta.env.VITE_API_URL;

interface RequestParams<T> {
  endpoint: string;
  method?: "GET" | "POST" | "PUT" | "DELETE";
  body?: BodyInit;
  onSuccess: (data: T) => void;
  onFailure: (message: string) => void;
}

export const request = async <T>({
  endpoint,
  method = "GET",
  body,
  onSuccess,
  onFailure,
}: RequestParams<T>) => {
  try {
    const response = await fetch(`${BASE_URL}${endpoint}`, {
      method,
      headers: {
        Authorization: `Bearer ${localStorage.getItem("token")}`,
        "Content-Type": "application/json",
      },
      body,
    });

    if (!response.ok) {
      const { message } = await response.json();
      throw new Error(message);
    }

    // avoid JSON parsing for DELETE requests
    if (method === "DELETE") {
      onSuccess(undefined as T);
      return;
    }

    const data: T = await response.json();
    onSuccess(data);
  } catch (error) {
    if (error instanceof Error) {
      onFailure(error.message);
    } else {
      onFailure("Something went wrong. Please try again later");
    }
  }
};
