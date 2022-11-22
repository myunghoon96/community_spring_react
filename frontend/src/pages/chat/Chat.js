import { useRef, useState, useEffect } from "react";
import * as StompJs from "@stomp/stompjs";
import styled from "@emotion/styled";
import { TextField, Paper, Button, Typography, Box } from "@mui/material";
import { createStyles, makeStyles } from "@mui/styles";
import { MessageLeft, MessageRight } from "../../components/Message";
import SendIcon from "@mui/icons-material/Send";
import { createTheme } from "@mui/material";
import { Stack } from "@mui/system";
import { LoginState, LoginUser } from "../../states/LoginState";
import { useRecoilState } from "recoil";
import moment from "moment";
import uuid from "react-uuid";

const theme = createTheme();
const useStyles = styled(() =>
  createStyles({
    paper: {
      width: "80vw",
      height: "80vh",
      maxWidth: "500px",
      maxHeight: "700px",
      display: "flex",
      alignItems: "center",
      flexDirection: "column",
      position: "relative",
    },
    container: {
      width: "100vw",
      height: "100vh",
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
    },
    messagesBody: {
      width: "calc( 100% - 20px )",
      margin: 10,
      overflowY: "scroll",
      height: "calc( 100% - 80px )",
    },
  })
);

function Chat() {
  const [chatList, setChatList] = useState([]);
  const [message, setMessage] = useState("");
  const classes = useStyles();
  const [loginUser, setLoginUser] = useRecoilState(LoginUser);
  const [isLogin, setIsLogin] = useRecoilState(LoginState);

  const roomId = "1";
  const client = useRef({});

  const connect = () => {
    client.current = new StompJs.Client({
      // brokerURL: "ws://localhost:8080/ws",
      brokerURL: "wss://hoon.ml/ws",
      onConnect: () => {
        console.log("success");
        subscribe();
      },
    });
    client.current.activate();
  };

  const publish = (chat) => {
    if (!client.current.connected) return;
    const date = new Date(Date.now());
    const timeStamp = moment(date).format("MM/DD HH:mm");

    client.current.publish({
      destination: "/pub/chat",
      body: JSON.stringify({
        roomId: roomId,
        email: loginUser,
        message: message,
        timeStamp: timeStamp,
      }),
    });

    setMessage("");
  };

  const subscribe = () => {
    client.current.subscribe("/sub/chat/" + roomId, (body) => {
      const json_body = JSON.parse(body.body);
      setChatList((_chat_list) => [..._chat_list, json_body]);
    });
  };

  const disconnect = () => {
    client.current.deactivate();
  };

  const handleChange = (event) => {
    setMessage(event.target.value);
  };

  const handleSubmit = (event, message) => {
    event.preventDefault();
    publish(message);
  };

  const handleOnKeyPress = (e) => {
    if (e.key === "Enter") {
      handleSubmit(e, message);
    }
  };

  useEffect(() => {
    connect();
    if (!isLogin && loginUser == "") {
      setLoginUser("익명_" + uuid());
    }
    return () => {
      if (!isLogin && loginUser != "") {
        setLoginUser("");
      }
      disconnect();
    };
  }, []);

  return (
    <div>
      <div className={classes.container}>
        <Typography variant="h6" align="center">
          *비회원 : 익명_uuid, 회원 : 이메일 표시
        </Typography>
        <Paper className={classes.paper}>
          <Paper id="style-1" className={classes.messagesBody}>
            <MessageLeft
              message="상대방 채팅 내용"
              timestamp="MM/DD 00:00"
              displayName="익명 or 이메일"
            />
            <MessageRight message="나의 채팅 내용" timestamp="MM/DD 00:00" />

            {chatList.map((chat, idx) =>
              chat.email == loginUser ? (
                <MessageRight
                  key={idx}
                  message={chat.message}
                  timestamp={chat.timeStamp}
                />
              ) : (
                <MessageLeft
                  key={idx}
                  message={chat.message}
                  displayName={chat.email}
                  timestamp={chat.timeStamp}
                />
              )
            )}
          </Paper>

          <Stack direction={"horizontal"}>
            <TextField
              id="standard-text"
              label="채팅 내용 입력"
              fullWidth
              onChange={handleChange}
              value={message}
              onKeyPress={handleOnKeyPress}
            />
            <Button
              variant="contained"
              color="primary"
              onClick={(event) => handleSubmit(event, message)}
            >
              <SendIcon />
            </Button>
          </Stack>
        </Paper>
      </div>
    </div>
  );
}

export default Chat;
