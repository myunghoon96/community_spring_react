import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import PostAddIcon from "@mui/icons-material/PostAdd";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { useNavigate } from "react-router-dom";
import customAxios from "../../utils/customAxios";

const theme = createTheme();

export default function Post() {
  const navigate = useNavigate();

  const [errorMsg, setErrorMsg] = React.useState(null);

  const handleSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);

    const itemDto = { title: data.get("title"), content: data.get("content") };
    customAxios
      .post("/board", JSON.stringify(itemDto))
      .then((response) => {
        navigate("/board/post/" + response.data.data);
      })
      .catch((error) => {
        setErrorMsg(error.response.data.error.msg);
      });
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="lg">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1 }}>
            <PostAddIcon />
          </Avatar>

          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <TextField
              margin="normal"
              required
              fullWidth
              id="title"
              label="제목"
              name="title"
              // autoComplete="title"
              autoFocus
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="content"
              label="내용"
              type="content"
              id="content"
              multiline
              rows={10}
              // defaultValue="Default Value"
              // autoComplete="current-content"
            />
            {errorMsg && <h3 style={{ color: "red" }}>{errorMsg}</h3>}
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              제출
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
