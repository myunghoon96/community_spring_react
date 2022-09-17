import React from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import Box from "@mui/material/Box";
import ErrorOutlineIcon from "@mui/icons-material/ErrorOutline";
import { LoginState, LoginUser } from "../states/LoginState";
import { useRecoilState } from "recoil";
import { removeCookie } from "../utils/cookie";

const theme = createTheme();

function Error() {
  const [isLogin, setIsLogin] = useRecoilState(LoginState);
  const [loginUser, setLoginUser] = useRecoilState(LoginUser);
  removeCookie("accessToken");
  removeCookie("refreshToken");
  setIsLogin(false);
  setLoginUser("");

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
        >
          <ErrorOutlineIcon color="primary" sx={{ fontSize: 250 }} />

          <h1>에러 페이지</h1>
          <h3>Access Token, Refresh Token이 유효하지 않습니다.</h3>
          <h3>다시 로그인 부탁드립니다.</h3>
        </Box>
      </Container>
    </ThemeProvider>
  );
}

export default Error;
