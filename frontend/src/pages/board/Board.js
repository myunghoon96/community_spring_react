import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import BoardTable from "../../components/BoardTable";
import customAxios from "../../utils/customAxios";
import { Box, Button } from "@mui/material";
import Pagination from "@mui/material/Pagination";
import { Stack } from "@mui/system";

const theme = createTheme();

function Board() {
  const [items, setItems] = useState(null);
  const [page, setPage] = useState(1);

  const getBoardList = (p) => {
    const params = { page: p, size: 5 };
    customAxios
      .get("/board", { params })
      .then((response) => {
        setItems(response.data.data);
      })
      .catch((error) => console.log(error));
  };

  useEffect(() => {
    getBoardList(page);
  }, []);

  const handleChange = (e, p) => {
    setPage(p);
    getBoardList(p);
  };

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
        {items && <BoardTable datas={items}></BoardTable>}
        {items && (
          <Stack mt={2} alignItems="center">
            <Pagination
              color="primary"
              count={items.pageInfo.totalPages}
              page={page}
              onChange={handleChange}
            />
          </Stack>
        )}
      </Container>
    </ThemeProvider>
  );
}

export default Board;
