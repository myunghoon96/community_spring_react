import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import Box from "@mui/material/Box";
import LetterAvatars from "../../components/LetterAvater";
import { Button } from "@mui/material";
import customAxios from "../../utils/customAxios";

const theme = createTheme();

function Profile() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    customAxios
      .get("member/profile")
      .then((response) => setItems(response.data.data))
      .catch((error) => console.log(error));
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
        >
          {items.email && <LetterAvatars email={items.email} />}
          <p>이메일 : {items.email}</p>
          <p>권한 : {items.role}</p>
          <Button
            href="/profile/post"
            variant="outlined"
            size="large"
            sx={{ m: 5 }}
          >
            내가 작성한 글
          </Button>

          <Button href="/profile/comment" variant="outlined" size="large">
            내가 작성한 댓글
          </Button>
        </Box>
      </Container>
    </ThemeProvider>
  );
}

export default Profile;
