import axios from "axios";
import { getCookie } from "./cookie";
import * as jwt from "jsonwebtoken";

export const silentRefresh = async (config) => {
  const accessToken = getCookie("accessToken");

  if (accessToken) {
    const decode = jwt.decode(accessToken);
    const nowDate = new Date().getTime() / 1000;

    if (decode && decode.exp < nowDate) {
      await axios.post("/api/member/refresh", "", {
        headers: {
          "Content-Type": `application/json`,
          "X-AUTH-TOKEN": getCookie("accessToken"),
          "REFRESH-TOKEN": getCookie("refreshToken"),
        },
      });
    }
  }
};
