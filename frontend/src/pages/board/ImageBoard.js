import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import customAxios from "../../utils/customAxios";
import { Button, Box } from "@mui/material";
import { useNavigate } from "react-router-dom";
import TitlebarImageList from "../../components/TitlebarImageList";

const theme = createTheme();

function ImageBoard() {
  const [images, setImages] = useState([]);
  const navigate = useNavigate();
  const files = [];

  const getImages = () => {
    customAxios
      .get("/image")
      .then((response) => setImages(response.data.data))
      .catch((error) => console.log(error));
  };

  const onSaveFiles = (e) => {
    const uploadFiles = Array.prototype.slice.call(e.target.files);

    uploadFiles.forEach((uploadFile) => {
      files.push(uploadFile);
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    const formData = new FormData();

    if (files.length == 0) {
      alert("이미지를 먼저 선택하세요");
    } else {
      files.forEach((file) => {
        formData.append("multipartFiles", file);
      });

      customAxios
        .post("/image", formData)
        .then((response) => {
          getImages();
          alert("이미지 업로드 완료");
        })
        .catch((error) => {
          alert("이미지 업로드 실패, " + error.response.data.error.msg);
        });
    }
  };

  useEffect(() => {
    getImages();
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <Box>
          <input
            type="file"
            id="file"
            accept="image/*"
            // style={{ display: "none" }}
            multiple
            onChange={onSaveFiles}
          />
          <Button
            type="submit"
            // fullWidth
            variant="contained"
            sx={{ width: "15%" }}
            onClick={handleSubmit}
          >
            업로드
          </Button>
        </Box>
        <TitlebarImageList images={images} />
      </Container>
    </ThemeProvider>
  );
}

export default ImageBoard;
