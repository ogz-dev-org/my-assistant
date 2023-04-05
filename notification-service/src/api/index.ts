import axios from "axios";

const MicroServiceInstance = axios.create({
  baseURL: "http://localhost:8080/api/v1/reminder",
});

export const checkReminder = async (id: string) => {
  return await MicroServiceInstance.post(`/check/${id}`);
};
