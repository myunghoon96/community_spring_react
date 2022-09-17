import React from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Grid from "@mui/material/Grid";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import MainFeaturedPost from "./MainFeaturedPost";
import FeaturedPost from "./FeaturedPost";

const mainFeaturedPost = {
  title: "토이 프로젝트",
  description: "Spring Rest API with React",
  image: "https://source.unsplash.com/random",
  imageText: "main image description",
  // linkText: '자세한 내용 보기',
};

const featuredPosts = [
  {
    title: "Spring",
    date: "Backend",
    description: "Java의 오픈소스 프레임워크",
    image: "https://source.unsplash.com/random",
    imageLabel: "Image Text",
  },
  {
    title: "JPA",
    date: "ORM",
    description: "Java의 ORM 기술",
    image: "https://source.unsplash.com/random",
    imageLabel: "Image Text",
  },
  {
    title: "Spring Security",
    date: "Authentication / Authorization",
    description: "Spring 하위 프레임워크",
    image: "https://source.unsplash.com/random",
    imageLabel: "Image Text",
  },
  {
    title: "JWT",
    date: "Token",
    description: "Json 형식의 Web Token",
    image: "https://source.unsplash.com/random",
    imageLabel: "Image Text",
  },
];

const theme = createTheme();

export default function Commnunity() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <MainFeaturedPost post={mainFeaturedPost} />
        <Grid container spacing={4}>
          {featuredPosts.map((post) => (
            <FeaturedPost key={post.title} post={post} />
          ))}
        </Grid>
      </Container>
    </ThemeProvider>
  );
}
