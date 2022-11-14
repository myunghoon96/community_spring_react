import * as React from "react";
import PropTypes from "prop-types";
import Toolbar from "@mui/material/Toolbar";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";
import GitHubIcon from "@mui/icons-material/GitHub";
import SourceIcon from "@mui/icons-material/Source";
import { removeCookie } from "../../utils/cookie";
import { useRecoilState } from "recoil";
import { LoginState, LoginUser } from "../../states/LoginState";
import LetterAvatars from "../../components/LetterAvater";
import { Avatar } from "@mui/material";

function Header(props) {
  const title = "Hoon";
  const sections = [
    { title: "회원 목록", url: "/member" },
    { title: "글 게시판", url: "/board" },
    { title: "이미지 게시판", url: "/images" },
  ];

  const [isLogin, setIsLogin] = useRecoilState(LoginState);
  const [loginUser, setLoginUser] = useRecoilState(LoginUser);

  const handleLogOut = (event) => {
    event.preventDefault();
    removeCookie("accessToken");
    removeCookie("refreshToken");
    setIsLogin(false);
    setLoginUser("");
    document.location.replace("/");
  };

  return (
    <React.Fragment>
      <Toolbar
        sx={{
          borderBottom: 1,
          borderColor: "divider",
        }}
      >
        <GitHubIcon />
        <Link href="https://github.com/myunghoon96" size="small">
          GitHub
        </Link>
        <SourceIcon sx={{ ml: 1 }} />
        <Link
          href="https://github.com/myunghoon96/community_spring_react"
          size="small"
        >
          Code
        </Link>
        <Typography
          component="h2"
          variant="h5"
          color="inherit"
          align="center"
          sx={{ flex: 1, ml: 1 }}
          href="/"
        >
          <Link href="/" underline="always">
            {title}
          </Link>
        </Typography>

        {isLogin && (
          <>
            <LetterAvatars email={loginUser} />
            <Button
              onClick={handleLogOut}
              variant="outlined"
              size="small"
              sx={{ ml: 1 }}
            >
              로그아웃
            </Button>
            <Button
              href="/profile"
              variant="outlined"
              size="small"
              sx={{ ml: 1 }}
            >
              내정보
            </Button>
          </>
        )}
        {!isLogin && (
          <>
            <Avatar />
            <Button
              href="/signin"
              variant="outlined"
              size="small"
              sx={{ ml: 1 }}
            >
              로그인
            </Button>
            <Button
              href="/signup"
              variant="outlined"
              size="small"
              sx={{ ml: 1 }}
            >
              회원가입
            </Button>
          </>
        )}
      </Toolbar>
      <Toolbar
        component="nav"
        variant="dense"
        sx={{ justifyContent: "space-between", overflowX: "auto" }}
      >
        {sections.map((section) => (
          <Link
            color="inherit"
            noWrap
            key={section.title}
            variant="body2"
            href={section.url}
            sx={{ p: 1, flexShrink: 0 }}
          >
            {section.title}
          </Link>
        ))}
      </Toolbar>
    </React.Fragment>
  );
}

Header.propTypes = {
  sections: PropTypes.arrayOf(
    PropTypes.shape({
      title: PropTypes.string.isRequired,
      url: PropTypes.string.isRequired,
    })
  ),
  // ).isRequired,

  title: PropTypes.string,
};

export default Header;
