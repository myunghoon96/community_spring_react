import React, { useEffect, useState } from "react";
import { getCookie } from "../../utils/cookie";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { CommentPostTable } from "../../components/CommentTable";
import customAxios from "../../utils/customAxios";

const theme = createTheme();

function ProfileComment() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    customAxios
      .get("/comment/profile", {
        headers: { "X-AUTH-TOKEN": `${getCookie("accessToken")}` },
      })
      .then((response) => setItems(response.data.data))
      .catch((error) => console.log(error));
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <CommentPostTable rows={items}></CommentPostTable>
      </Container>
    </ThemeProvider>
  );
}

export default ProfileComment;
