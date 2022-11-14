import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Commnunity from "./pages/community/Commnunity";
import Board from "./pages/board/Board";
import Member from "./pages/member/Member";
import Profile from "./pages/member/Profile";
import SignIn from "./pages/auth/SignIn";
import SignUp from "./pages/auth/SignUp";
import Container from "@mui/material/Container";
import Header from "./pages/community/Header";
import Post from "./pages/board/Post";
import PostDetail from "./pages/board/PostDetail";
import Error from "./pages/Error";
import ProfileBoard from "./pages/member/ProfileBoard";
import ProfileComment from "./pages/member/ProfileComment";
import { Navigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { LoginState } from "./states/LoginState";
import KakaoRedirect from "./pages/auth/KakaoRedirect";
import ImageBoard from "./pages/board/ImageBoard";

function App() {
  const [isLogin, setIsLogin] = useRecoilState(LoginState);
  const ProtectedRoute = ({ children }) => {
    if (!isLogin) {
      return <Navigate to="/signin" replace />;
    }
    return children;
  };

  return (
    <Container maxWidth="lg">
      <Header />
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Commnunity />} />
          <Route path="/member" element={<Member />} />
          <Route path="/signup" element={<SignUp />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="/error" element={<Error />} />
          <Route path="/oauth/kakao" element={<KakaoRedirect />} />

          <Route
            path="/board"
            element={
              <ProtectedRoute>
                <Board />
              </ProtectedRoute>
            }
          />
          <Route
            path="/board/post"
            element={
              <ProtectedRoute>
                <Post />
              </ProtectedRoute>
            }
          />
          <Route
            path="/board/post/:postId"
            element={
              <ProtectedRoute>
                <PostDetail />
              </ProtectedRoute>
            }
          />
          <Route
            path="/images"
            element={
              <ProtectedRoute>
                <ImageBoard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            }
          />
          <Route
            path="/profile/post"
            element={
              <ProtectedRoute>
                <ProfileBoard />
              </ProtectedRoute>
            }
          />
          <Route
            path="/profile/comment"
            element={
              <ProtectedRoute>
                <ProfileComment />
              </ProtectedRoute>
            }
          />
        </Routes>
      </BrowserRouter>
    </Container>
  );
}

export default App;
