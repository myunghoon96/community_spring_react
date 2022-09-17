import axios from "axios";
import { config } from "process";
import { getCookie } from "./cookie";
import { silentRefresh } from "./silentRefresh";

export const customAxios = axios.create({
  timeout: 10000,
  baseURL: "/api",
  headers: {
    "Content-Type": `application/json`,
    "X-AUTH-TOKEN": getCookie("accessToken"),
    // "REFRESH-TOKEN": getCookie("refreshToken"),
  },
});

customAxios.interceptors.request.use(silentRefresh(config));
customAxios.interceptors.response.use(
  function (response) {
    return response;
  },
  async function (error) {
    const originalRequest = error.config;
    if (error.response.status === 401 || error.response.status === 406) {
      window.location.href = "/error";
      return Promise.reject(error);
    }
  }
);
export default customAxios;
