import * as React from "react";
import Avatar from "@mui/material/Avatar";

function stringToColor(string) {
  let hash = 0;
  let i;

  for (i = 0; i < string.length; i += 1) {
    hash = string.charCodeAt(i) + ((hash << 5) - hash);
  }

  let color = "#";
  for (i = 0; i < 3; i += 1) {
    const value = (hash >> (i * 8)) & 0xff;
    color += `00${value.toString(16)}`.slice(-2);
  }

  return color;
}

function stringAvatar(name, width, height) {
  return {
    sx: {
      bgcolor: stringToColor(name),

      width: width,
      height: height,
    },
    children: `${name.split(" ")[0][0]}`,
  };
}

export default function LetterAvatars(props) {
  return <Avatar {...stringAvatar(props.email, props.width, props.height)} />;
}
