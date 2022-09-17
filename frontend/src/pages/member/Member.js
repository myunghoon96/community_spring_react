import React, { useEffect, useState } from "react";
import CssBaseline from "@mui/material/CssBaseline";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import MemberTable from "../../components/MemberTable";
import customAxios from "../../utils/customAxios";

const theme = createTheme();

function Member() {
  const [members, setMembers] = useState([]);

  useEffect(() => {
    customAxios
      .get("/member")
      .then((response) => setMembers(response.data.data))
      .catch((error) => console.log(error));
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <MemberTable rows={members}></MemberTable>
      </Container>
    </ThemeProvider>
  );
}

export default Member;
