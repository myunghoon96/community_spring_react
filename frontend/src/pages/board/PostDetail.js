import React, { useEffect, useState } from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Box from "@mui/material/Box";
import DescriptionIcon from "@mui/icons-material/Description";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { useNavigate, useParams } from "react-router-dom";
import Divider from "@mui/material/Divider";
import { CommentTable } from "../../components/CommentTable";
import customAxios from "../../utils/customAxios";

const theme = createTheme();

export default function PostDetail() {
  const [items, setItems] = useState([]);
  const [inputComment, setInputComment] = useState("");
  const { postId } = useParams();

  const [errorMsg, setErrorMsg] = React.useState(null);
  const [commentErrorMsg, setCommentErrorMsg] = React.useState(null);
  const [comments, setComments] = useState([]);

  useEffect(() => {
    customAxios
      .get("/board/" + postId)
      .then((response) => {
        setItems(response.data.data);
        getCommentList();
      })
      .catch((error) => {
        setErrorMsg(error.response.data.error.msg);
      });
  }, []);

  const getCommentList = () => {
    customAxios
      .get("/comment/board/" + postId)
      .then((response) => {
        setComments(response.data.data);
      })
      .catch((error) => {
        setCommentErrorMsg(error.response.data.error.msg);
      });
  };

  const handleCommentSubmit = (event) => {
    event.preventDefault();
    const data = new FormData(event.currentTarget);
    const commentDto = { content: data.get("comment") };

    customAxios
      .post("/comment/board/" + postId, JSON.stringify(commentDto))
      .then((response) => {
        getCommentList();
        setInputComment("");
        setCommentErrorMsg(null);
      })
      .catch((error) => {
        setCommentErrorMsg(error.response.data.error.msg);
      });
  };

  return (
    <ThemeProvider theme={theme}>
      <Container maxWidth="lg">
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
            <DescriptionIcon />
          </Avatar>

          <Box sx={{ width: "100%" }}>
            {items && items.view >= 0 && (
              <TextField
                // fullWidth
                sx={{ width: "25%" }}
                margin="normal"
                id="view"
                label="조회수"
                value={items.view || 0}
              />
            )}
            {items && items.member && items.member.email && (
              <TextField
                // fullWidth
                sx={{ width: "25%" }}
                margin="normal"
                id="email"
                label="작성자"
                value={items["member"].email || ""}
              />
            )}
            <TextField
              // fullWidth
              sx={{ width: "25%" }}
              margin="normal"
              id="createdDate"
              label="작성일"
              value={items.createdDate || ""}
            />
            <TextField
              // fullWidth
              sx={{ width: "25%" }}
              margin="normal"
              id="modifiedDate"
              label="수정일"
              value={items.modifiedDate || ""}
            />
          </Box>

          <TextField
            margin="normal"
            required
            fullWidth
            name="title"
            label="제목"
            type="title"
            id="title"
            multiline
            value={items.title || ""}
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
            value={items.content || ""}
          />
          {errorMsg && <h3 style={{ color: "red" }}>{errorMsg}</h3>}

          <Divider />
          <Box
            component="form"
            onSubmit={handleCommentSubmit}
            noValidate
            sx={{ mt: 1, width: "100%" }}
          >
            <TextField
              sx={{ width: "85%" }}
              margin="normal"
              id="comment"
              label="새 댓글"
              name="comment"
              value={inputComment}
              onChange={(e) => {
                setInputComment(e.target.value);
              }}
            />
            <Button
              type="submit"
              // fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2, width: "15%" }}
            >
              댓글 작성
            </Button>
            {commentErrorMsg && (
              <h3 style={{ color: "red" }}>{commentErrorMsg}</h3>
            )}
          </Box>
          <CommentTable rows={comments} />
        </Box>
      </Container>
    </ThemeProvider>
  );
}
