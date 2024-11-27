import React, { useEffect, useState } from 'react';
import {
    Box,
    Button,
    MenuItem,
    Select,
    Typography,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    FormControl,
    InputLabel,
    FormHelperText,
    TextField,
} from '@mui/material';
import axios from '../../api/axiosInstance';
import EditableTable from './EditableTable';
import {
    workerColumns,
    organizationColumns,
    personColumns,
    addresesColumns,
    coordinatesColumns,
    locationsColumns,
} from './TableConfig';

// Конфигурация для полей формы для каждой таблицы
import { workerFields, organizationFields, personFields, addressFields, coordinatesFields, locationFields } from './FormConfig';

const TableSwitcher = () => {
    const [selectedTable, setSelectedTable] = useState('workers');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isModalAddctionActionOpen, setIsModalAddctionActionOpen] = useState(false);
    const [addictionActionData, setAddictionActionData] = useState({});
    const [formData, setFormData] = useState({});
    const [loading, setLoading] = useState(false);
    const [newEntity, setNewEntity] = useState(null);
    const [filteredData, setFilteredData] = useState([]);
    const [workersData, setWorkersData] = useState([]);
    const [selectedAction, setSelectedAction] = useState('');
    const [specialState, setSpecialState] = useState(false);

    // Конфигурации таблиц
    const tablesConfig = {
        workers: {
            label: 'Workers',
            entity: 'workers',
            columns: workerColumns,
            fields: workerFields,
        },
        organizations: {
            label: 'Organizations',
            entity: 'organizations',
            columns: organizationColumns,
            fields: organizationFields,
        },
        persons: {
            label: 'Persons',
            entity: 'persons',
            columns: personColumns,
            fields: personFields,
        },
        address: {
            label: 'Address',
            entity: 'address',
            columns: addresesColumns,
            fields: addressFields,
        },
        coordinates: {
            label: 'Coordinates',
            entity: 'coordinates',
            columns: coordinatesColumns,
            fields: coordinatesFields,
        },
        locations: {
            label: 'Locations',
            entity: 'locations',
            columns: locationsColumns,
            fields: locationFields,
        },
    };

    useEffect(() => {
        setNewEntity(workersData); // Обновляем состояние newEntity при изменении данных
    }, [workersData]);

    const handleChange = (event) => {
        setSelectedTable(event.target.value);
        setFormData({}); // Очистка формы при смене таблицы
    };

    const handleActionFieldChange = (key, value) => {
        setAddictionActionData({ ...addictionActionData, [key]: value });
    };

    const { entity, columns, fields } = tablesConfig[selectedTable];

    const handleActionChange = (event) => {
        setSelectedAction(event.target.value);
        setAddictionActionData({});
    };

    const handleExecuteAction = async () => {
        if (selectedAction === 'delete') {
            handleDeleteByEndDate();
        } else if (selectedAction === 'filterByEndDate') {
            handleFilterByEndDate();
        } else if (selectedAction === 'indexSalary') {
            handleIndexSalary();
        }
    };

    const isActionValid = () => {
        if (selectedAction === 'delete' && addictionActionData.endDate) return true;
        if (selectedAction === 'filterByEndDate' && addictionActionData.endDate) return true;
        if (selectedAction === 'indexSalary' && addictionActionData.workerId && addictionActionData.salaryFactor) return true;
        return false;
    };

    const make_native = (dateString) => {
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

    // const make_native = (dateString) => {
    //     if (!dateString) return '';
    //     const date = new Date(dateString);
    //     // return date.toISOString().slice(0, 19); // Преобразуем в формат "yyyy-MM-ddThh:mm"
    //     console.log(date.toISOString());// Преобразуем в формат "yyyy-MM-ddThh:mm"
    //     return date.toISOString();// Преобразуем в формат "yyyy-MM-ddThh:mm"
    // }


    // Открытие и закрытие модального окна
    const handleOpenModal = () => setIsModalOpen(true);
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setFormData({});
    };
    // Открытие и закрытие модального окна
    const handleOpenModalAddictionAction = () => setIsModalAddctionActionOpen(true);
    const handleCloseModalAddictionAction = () => {
        setIsModalAddctionActionOpen(false);
        setAddictionActionData({});
    };

    const handleInputChange = (key, value) => {
        setFormData({ ...formData, [key]: value });
    };

    const handleCreate = async () => {
        try {
            setLoading(true);
            const response = await axios.post(`/${entity}`, formData);
            setLoading(false);
            setNewEntity(response.data);
            handleCloseModal();
        } catch (error) {
            console.error('Ошибка при создании сущности:', error);
            setLoading(false);
        }
    };

    const fetchData = async () => {
        try {
            const response = await axios.get(`/${entity}`);
            console.log(response.data)
            setWorkersData(response.data); // Сохранение данных о работниках или других сущностях
            return response.data
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error);
        }
    };

    // Удаление объектов с заданным endDate
    const handleDeleteByEndDate = async () => {
        const workersData = await fetchData();
        try {
            const toDelete = workersData.filter(item => make_native(item.endDate) === make_native(addictionActionData.endDate));
            for (let item of toDelete) {
                await axios.delete(`/${entity}/${item.id}`);
            }
            fetchData();
        } catch (error) {
            console.error('Ошибка при удалении:', error);
        }
        setNewEntity(null)
    };

    // Фильтрация данных по endDate больше заданной даты
    const handleFilterByEndDate = async () => {
        const workersData = await fetchData();
        const filtered = workersData.filter(item => new Date(item.endDate) > new Date(addictionActionData.endDate));
        setFilteredData(filtered);
        console.log(filtered, workersData, addictionActionData.endDate)
    };

    // Индексация заработной платы на заданный коэффициент
    const handleIndexSalary = async () => {
        const workersData = await fetchData();
        try {
            const worker = workersData.find(item => item.id == addictionActionData.workerId);
            if (worker) {
                const updatedWorker = { ...worker, salary: worker.salary * addictionActionData.salaryFactor };
                let respData = await axios.put(`/${entity}/${addictionActionData.workerId}`, updatedWorker);
                setNewEntity(respData.data)
            }
        } catch (error) {
            console.error('Ошибка при индексации зарплаты:', error);
        }
    };

    return (
        <Box sx={{ padding: '20px' }}>
            <Typography variant="h4" sx={{ marginBottom: '20px', flexDirection: 'row', display: 'flex' }}>
                Table
                <Box sx={{ flexDirection: 'row', display: 'flex', justifyContent: 'flex-end', marginLeft: 'auto' }}>
                    <Button
                        onClick={handleOpenModalAddictionAction}
                        sx={{ width: '50%', maxWidth: '250px' }}
                    >
                        Дополнительные действия
                    </Button>
                    <Button
                        onClick={handleOpenModal}
                        sx={{ width: '50%', maxWidth: '250px' }}
                    >
                        Добавить сущность таблицы
                    </Button>
                </Box>
            </Typography>

            <Select
                value={selectedTable}
                onChange={handleChange}
                sx={{ marginBottom: '20px', minWidth: '200px' }}
            >
                {Object.keys(tablesConfig).map((key) => (
                    <MenuItem key={key} value={key}>
                        {tablesConfig[key].label}
                    </MenuItem>
                ))}
            </Select>

            <EditableTable entity={entity} columns={columns} newEntity={newEntity} fields={fields} />

            <Dialog open={isModalAddctionActionOpen} onClose={handleCloseModalAddictionAction}>
                <DialogTitle>Дополнительные действия</DialogTitle>
                <DialogContent>
                    <FormControl fullWidth margin="dense">
                        <InputLabel>Выберите действие</InputLabel>
                        <Select
                            value={selectedAction}
                            onChange={handleActionChange}
                            label="Выберите действие"
                            fullWidth
                        >
                            <MenuItem value="delete">Удалить по End Date</MenuItem>
                            <MenuItem value="filterByEndDate">Фильтровать по End Date</MenuItem>
                            <MenuItem value="indexSalary">Индексация зарплаты</MenuItem>
                        </Select>
                        <FormHelperText>Выберите действие, для которого будут отображены поля</FormHelperText>
                    </FormControl>

                    {/* Поля для действия "Удалить по End Date" */}
                    {selectedAction === 'delete' && (
                        <TextField
                            label="End Date для удаления"
                            type="datetime-local"
                            value={addictionActionData.endDate || ''}
                            onChange={(e) => handleActionFieldChange('endDate', e.target.value)}
                            fullWidth
                            margin="dense"
                            InputLabelProps={{ shrink: true }}
                        />
                    )}
                    {/* Поля для действия "Фильтровать по End Date" */}
                    {selectedAction === 'filterByEndDate' && (
                        <TextField
                            label="End Date для фильтрации"
                            type="date"
                            value={addictionActionData.endDate || ''}
                            onChange={(e) => handleActionFieldChange('endDate', e.target.value)}
                            fullWidth
                            margin="dense"
                            InputLabelProps={{ shrink: true }}
                        />
                    )}

                    {/* Поля для действия "Индексация зарплаты" */}
                    {selectedAction === 'indexSalary' && (
                        <>
                            <TextField
                                label="ID сотрудника"
                                type="number"
                                value={addictionActionData.workerId || ''}
                                onChange={(e) => handleActionFieldChange('workerId', e.target.value)}
                                fullWidth
                                margin="dense"
                            />
                            <TextField
                                label="Коэффициент индексации"
                                type="number"
                                value={addictionActionData.salaryFactor || ''}
                                onChange={(e) => handleActionFieldChange('salaryFactor', e.target.value)}
                                fullWidth
                                margin="dense"
                            />
                        </>
                    )}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseModalAddictionAction}>Закрыть</Button>
                    <Button
                        onClick={handleExecuteAction}
                        disabled={loading || !isActionValid()}
                    >
                        Выполнить
                    </Button>
                </DialogActions>
            </Dialog>

            <Dialog open={isModalOpen} onClose={handleCloseModal}>
                <DialogTitle>Добавить {tablesConfig[selectedTable].label}</DialogTitle>
                <DialogContent>
                    {fields.map((field) => (
                        field.type === 'enum' ? (
                            <Select
                                key={field.key}
                                label={field.label}
                                value={formData[field.key] || ''}
                                onChange={(e) => handleInputChange(field.key, e.target.value)}
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
                                value={field.type === 'datetime-local' ? make_native(formData[field.key]) : formData[field.key] || ''}
                                onChange={(e) => handleInputChange(field.key, e.target.value)}
                                fullWidth
                                margin="dense"
                            />
                        )
                    ))}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseModal}>Отмена</Button>
                    <Button onClick={handleCreate} disabled={loading}>
                        {loading ? 'Создание...' : 'Создать'}
                    </Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default TableSwitcher;
