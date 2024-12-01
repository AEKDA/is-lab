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
import axios from '../../api/axiosInstance';

const EditableTable = ({ columns, entity, newEntity, fields, onItemChange }) => {
  const [data, setData] = useState([]);
  const [selectedRow, setSelectedRow] = useState(null);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editedData, setEditedData] = useState({});
  const [filterMode, setFilterMode] = useState('exact');
  const [columnFilters, setColumnFilters] = useState([])
  const [sorting, setSorting] = useState([])

  // Функция для загрузки данных с сервера
  const fetchData = async () => {
    try {
      const response = await axios.get(`/${entity}`);
      setData(response.data);
    } catch (error) {
      console.error('Ошибка при загрузке данных:', error);
    }
  };

  // Обработчик переключения режима фильтрации
  const toggleFilterMode = () => {
    setFilterMode((prevMode) => (prevMode === 'exact' ? 'startsWith' : 'exact'));
  };

  const customFilter = (row, columnId, filterValue) => {
    console.log('last', row, columnId, filterValue)
    const rowValue = String(row.getValue(columnId) || '').toLowerCase();
    const searchValue = String(filterValue || '').toLowerCase();

    if (filterMode === 'exact') {
      return rowValue === searchValue;
    } else {
      return rowValue.startsWith(searchValue);
    }
  };


  useEffect(() => {
    fetchData();
  }, [entity]);

  const [pagination, setPagination] = useState({
    pageIndex: 0,
    pageSize: 10,
  });

  const tableInstance = useReactTable(
    {
      columns,
      data,
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

  // Обработчик для обновления строки
  const handleSave = async () => {
    try {
      await axios.put(`/${entity}/${editedData.id}`, editedData);
      fetchData();
      setIsDialogOpen(false);
    } catch (error) {
      console.error('Ошибка при обновлении данных:', error);
    }
  };

  const make_native = (dateString) => {
    if (!dateString) return '';
    const date = new Date(dateString);

    // Получаем компоненты даты и времени
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');

    // Форматируем в нужный вид
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  };

  // Обработчик для удаления строки
  const handleDelete = async () => {
    try {
      await axios.delete(`/${entity}/${selectedRow.original.id}`);
      fetchData();
      setIsDialogOpen(false);
      setSelectedRow(null);
    } catch (error) {
      console.error('Ошибка при удалении данных:', error);
    }
  };

  useEffect(() => {
    if (newEntity && newEntity != null) {
      setData((prevData) => {
        const existingIndex = prevData.findIndex((item) => item.id === newEntity.id);

        if (existingIndex !== -1) {
          // Если объект с таким id уже существует, заменяем его
          return prevData.map((item) => (item.id === newEntity.id ? newEntity : item));
        } else {
          // Если объекта с таким id нет, добавляем его в начало
          return [newEntity, ...prevData];
        }
      });
    }
  }, [newEntity]);

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
                      column.column.getIsSorted() === 'asc'? (
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
                    }}>{flexRender(
                      cell.column.columnDef.cell,
                      cell.getContext()
                    )}</TableCell>
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
          Страница {getState().pagination.pageIndex + 1} из {getPageCount()}
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
                    ? make_native(editedData[field.key])
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
          <Button onClick={handleSave}>Сохранить</Button>
          <Button onClick={handleDelete} color="error">
            Удалить
          </Button>
          <Button onClick={() => setIsDialogOpen(false)}>Отмена</Button>
        </DialogActions>
      </Dialog>
    </Box >
  );
};

export default EditableTable;
