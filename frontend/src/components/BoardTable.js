import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { Link } from "react-router-dom";

export default function BoardTable(props) {
  const rows = props.datas.data;
  const pageSize = props.datas.pageInfo.size;
  const totalElements = props.datas.pageInfo.totalElements;
  const emptyRows = pageSize - totalElements;

  return (
    <TableContainer component={Paper}>
      <Table sx={{ minWidth: 650 }} aria-label="simple table">
        <TableHead>
          <TableRow>
            <TableCell>제목</TableCell>
            <TableCell>조회 수</TableCell>
            <TableCell>작성자</TableCell>
            <TableCell>작성일자</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow
              key={row.id}
              sx={{ "&:last-child td, &:last-child th": { border: 0 } }}
            >
              <TableCell component={Link} to={"/board/post/" + row.id}>
                {row.title.substring(0, 15)}
              </TableCell>
              <TableCell>{row.view}</TableCell>
              <TableCell>{row.email}</TableCell>
              <TableCell>{row.createdDate}</TableCell>
            </TableRow>
          ))}
          {emptyRows > 0 && (
            <TableRow
              style={{
                height: 58 * emptyRows,
              }}
            ></TableRow>
          )}
        </TableBody>
      </Table>
    </TableContainer>
  );
}
