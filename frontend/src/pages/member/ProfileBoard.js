import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import BoardTable from "../../components/BoardTable";
import { useRecoilState } from "recoil";
import { LoginUser } from "../../states/LoginState";
import customAxios from "../../utils/customAxios";

const theme = createTheme();

function ProfileBoard() {
  const [items, setItems] = useState([]);
  const [loginUser, setLoginUser] = useRecoilState(LoginUser);

  useEffect(() => {
    customAxios
      .get("/board/profile")
      .then((response) => setItems(response.data.data))
      .catch((error) => console.log(error));
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <BoardTable rows={items}></BoardTable>
      </Container>
    </ThemeProvider>
  );
}

export default ProfileBoard;
