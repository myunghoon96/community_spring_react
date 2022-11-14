import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import BoardTable from "../../components/BoardTable";
import customAxios from "../../utils/customAxios";
import { Box, Button } from "@mui/material";

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
        <Box align="center">
          <Button
            href="/board/post"
            variant="contained"
            // size="large"
            sx={{ mt: 5, mb: 5, width: "100%" }}
          >
            게시글 작성
          </Button>
        </Box>
        <BoardTable rows={items}></BoardTable>
      </Container>
    </ThemeProvider>
  );
}

export default Board;
