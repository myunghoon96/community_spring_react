import React from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import Box from "@mui/material/Box";
import axios from "axios";
import { useEffect } from "react";
import * as kakao from "../../constants/kakao";
import { useNavigate, useRoutes } from "react-router-dom";
import { useRecoilState } from "recoil";
import { LoginState, LoginUser } from "../../states/LoginState";

const theme = createTheme();

function KakaoRedirect() {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useRecoilState(LoginState);
  const [loginUser, setLoginUser] = useRecoilState(LoginUser);

  const getJWTFromBackendByToken = async (token) => {
    // const url = "http://localhost:8080/api/member/oauth/kakao";
    const url = kakao.BACKEND_URL;
    await axios
      .post(url, token, {
        withCredentials: true,
      })
      // .get(url, { params: { token } }, {withCredentials: true} )
      .then((response) => {
        console.log(response.data);
        setIsLogin(true);
        setLoginUser(response.data.data);
        navigate("/");
      })
      .catch((error) => {
        console.log(error.response);
      });
  };

  const getKakaoToken = async (code) => {
    axios
      .post(
        `${kakao.KAKAO_TOKEN_URL}?grant_type=${kakao.GRANT_TYPE}&client_id=${kakao.CLIENT_ID}&redirect_uri=${kakao.REDIRECT_URI}&code=${code}`,
        {
          headers: {
            "Content-type": "application/x-www-form-urlencoded;charset=utf-8",
          },
        }
      )
      .then((response) => {
        getJWTFromBackendByToken(response.data.access_token);
      })
      .catch((error) => {
        console.log(error.response);
      });
  };

  useEffect(() => {
    const params = new URL(document.location.toString()).searchParams;
    const code = params.get("code");
    getKakaoToken(code);
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        ></Box>
      </Container>
    </ThemeProvider>
  );
}

export default KakaoRedirect;
