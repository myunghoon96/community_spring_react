import * as React from "react";
import ImageList from "@mui/material/ImageList";
import ImageListItem from "@mui/material/ImageListItem";
import ImageListItemBar from "@mui/material/ImageListItemBar";
import ListSubheader from "@mui/material/ListSubheader";
import IconButton from "@mui/material/IconButton";
import InfoIcon from "@mui/icons-material/Info";

export default function TitlebarImageList(props) {
  const itemData = props.images;

  return (
    <ImageList>
      <ImageListItem key="Subheader" cols={2}>
        {/* <ListSubheader component="div">December</ListSubheader> */}
      </ImageListItem>
      {itemData.map((item) => (
        <ImageListItem key={item.id}>
          <img
            src={item.newFileName}
            srcSet={item.newFileName}
            // alt={item.originalFileName}
            loading="lazy"
          />
          <ImageListItemBar
            title={item.originalFileName}
            subtitle={item.newFileName}
            // actionIcon={
            //   <IconButton
            //     sx={{ color: "rgba(255, 255, 255, 0.54)" }}
            //     aria-label={`info about ${item.originalFileName}`}
            //   >
            //     <InfoIcon />
            //   </IconButton>
            // }
          />
        </ImageListItem>
      ))}
    </ImageList>
  );
}
