import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import BoardTable from "../../components/BoardTable";
import customAxios from "../../utils/customAxios";

const theme = createTheme();

function Board() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    customAxios
      .get("/board")
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

export default Board;
