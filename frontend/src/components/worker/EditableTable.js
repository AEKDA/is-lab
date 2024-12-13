import React, { useEffect, useMemo, useState } from 'react';
import { useReactTable, useReactSortBy, useReactFilters, getPaginationRowModel, getFilteredRowModel, getSortedRowModel, flexRender, getCoreRowModel } from '@tanstack/react-table';
import { BiSortAlt2, BiSortDown, BiSortUp } from 'react-icons/bi'
import {
  TextField,
  Button,
  Box,
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableRow,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  Select,
  MenuItem,
} from '@mui/material';
import dateFormat from './time-format';
import { useTableContext } from 'src/pages/table-context';

const EditableTable = ({ columns, fields, onDelete, onUpdate }) => {
  const [selectedRow, setSelectedRow] = useState(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editedData, setEditedData] = useState({});
  const [filterMode, setFilterMode] = useState('exact');
  const [columnFilters, setColumnFilters] = useState([])
  const [sorting, setSorting] = useState([])

  const { storageEntities } = useTableContext()

  // Обработчик переключения режима фильтрации
  const toggleFilterMode = () => {
    setFilterMode((prevMode) => (prevMode === 'exact' ? 'startsWith' : 'exact'));
  };

  const customFilter = (row, columnId, filterValue) => {
    const rowValue = String(row.getValue(columnId) || '').toLowerCase();
    const searchValue = String(filterValue || '').toLowerCase();

    if (filterMode === 'exact') {
      return rowValue === searchValue;
    } else if (filterMode === 'startsWith') {
      return rowValue.startsWith(searchValue);
    } else {
      return rowValue.startsWith(searchValue);
    }
  };

  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });

  const tableInstance = useReactTable(
    {
      columns,
      data: storageEntities,
      getPaginationRowModel: getPaginationRowModel(),
      getFilteredRowModel: getFilteredRowModel(),
      getCoreRowModel: getCoreRowModel(),
      getSortedRowModel: getSortedRowModel(),
      onPaginationChange: setPagination,
      onSortingChange: setSorting,
      onColumnFiltersChange: setColumnFilters,
      filterFns: {
        customFilter: customFilter
      },
      state: { pagination, columnFilters, sorting },
    }
  );

  const {
    getHeaderGroups,
    getState,
    setPageIndex,
    getPageCount,
    getRowModel,
  } = tableInstance;

  // Функция для открытия диалога
  const handleEdit = (row) => {
    setSelectedRow(row);
    setEditedData(row.original);
    setIsDialogOpen(true);
  };


  return (
    <Box sx={{ padding: '20px', width: '100%', overflow: 'auto' }}>
      <Button variant="contained" onClick={toggleFilterMode} sx={{ mb: 2 }}>
        Режим фильтрации: {filterMode === 'exact' ? 'Полное совпадение' : 'Начинается с'}
      </Button>
      <Table sx={{ minWidth: 650, border: '1px solid #ddd', tableLayout: 'fixed' }}>
        <TableHead >
          {getHeaderGroups().map((headerGroup) => (
            <TableRow key={headerGroup.id}>
              {headerGroup.headers.map((column) => (
                <TableCell key={column.id}
                  onClick={column.column.getToggleSortingHandler()}
                  sx={{
                    textAlign: 'center'
                  }}>
                  {
                    flexRender(
                      column.column.columnDef.header,
                      column.getContext()
                    )}
                  <span>
                    {column.column.getIsSorted() ? (
                      column.column.getIsSorted() === 'asc' ? (
                        <BiSortUp />
                      ) : (
                        <BiSortDown />
                      )
                    ) : (
                      <BiSortAlt2 />
                    )}
                  </span>
                  {
                    <TextField
                      variant="standard"
                      size="small"
                      placeholder="Фильтр"
                      onChange={(e) => column.column.setFilterValue(e.target.value)}
                      sx={{ width: '100%', marginTop: '5px' }}
                    />
                  }
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableHead>
        <TableBody>
          {getRowModel().rows.map((row) => {
            return (
              <TableRow

                onClick={() => handleEdit(row)}
                sx={{
                  cursor: 'pointer',
                  backgroundColor: selectedRow?.id === row.id ? '#f0f8ff' : 'inherit',
                }}
              >
                {row.getVisibleCells().map((cell) => (
                  <TableCell
                    sx={{
                      whiteSpace: 'normal',
                      overflow: 'hidden',
                      textOverflow: 'ellipsis',
                      minWidth: 150,
                    }}>
                    {cell.column.columnDef.header.toLowerCase().includes('link') ? (
                      <a href={cell.getValue()} target="_blank" rel="noopener noreferrer">
                        {"click here for get file"}
                      </a>
                    ) : (
                      flexRender(
                        cell.column.columnDef.cell,
                        cell.getContext()
                      )
                    )}
                  </TableCell>
                ))}
              </TableRow>
            );
          })}
        </TableBody>
      </Table>

      <Box sx={{ display: 'flex', justifyContent: 'space-between', marginTop: '20px' }}>
        <Button onClick={() => setPageIndex(0)} disabled={getState().pagination.pageIndex === 0}>
          Первая
        </Button>
        <Button onClick={() => setPageIndex(getState().pagination.pageIndex - 1)} disabled={getState().pagination.pageIndex === 0}>
          Назад
        </Button>
        <Button onClick={() => setPageIndex(getState().pagination.pageIndex + 1)} disabled={getState().pagination.pageIndex === getPageCount() - 1}>
          Вперед
        </Button>
        <Button onClick={() => setPageIndex(getPageCount() - 1)} disabled={getState().pagination.pageIndex === getPageCount() - 1}>
          Последняя
        </Button>
        <span>
          Страница {getState().pagination.pageIndex + 1} из {getPageCount() === 0 ? 1 : getPageCount()}
        </span>
      </Box>

      <Dialog open={isDialogOpen} onClose={() => setIsDialogOpen(false)}>
        <DialogTitle>Редактировать запись</DialogTitle>
        <DialogContent>
          {fields.map((field) =>
            field.type === 'enum' ? (
              <Select
                key={field.key}
                label={field.label}
                value={editedData[field.key] || ''}
                onChange={(e) => setEditedData({ ...editedData, [field.key]: e.target.value })}
                fullWidth
                margin="dense"
                displayEmpty
                sx={{ marginBottom: '20px' }}
              >
                <MenuItem value="" disabled>
                  Выберите {field.label.toLowerCase()}
                </MenuItem>
                {field.options.map((option) => (
                  <MenuItem key={option} value={option}>
                    {option}
                  </MenuItem>
                ))}
              </Select>
            ) : (
              <TextField
                key={field.key}
                label={field.label}
                type={field.type || 'text'}
                value={
                  field.type === 'datetime-local'
                    ? dateFormat(editedData[field.key])
                    : editedData[field.key] || ''
                }
                onChange={(e) => setEditedData({ ...editedData, [field.key]: e.target.value })}
                fullWidth
                margin="dense"
              />
            )
          )}
        </DialogContent>
        <DialogActions>
          <Button onClick={() => { onUpdate(editedData.id, editedData); setIsDialogOpen(false); }}>Сохранить</Button>
          <Button onClick={() => { onDelete(selectedRow.original.id); setIsDialogOpen(false); }} color="error">
            Удалить
          </Button>
          <Button onClick={() => { setIsDialogOpen(false); }}>Отмена</Button>
        </DialogActions>
      </Dialog>
    </Box >
  );
};

export default EditableTable;
