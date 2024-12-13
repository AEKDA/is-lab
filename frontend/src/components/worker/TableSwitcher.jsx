import React, { useCallback, useEffect, useState } from 'react';
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
    Input,
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
    historyColumns,
} from './TableConfig';

import { workerFields, organizationFields, personFields, addressFields, coordinatesFields, locationFields, historyFields } from './FormConfig';
import dateFormat from './time-format';
import { useTableContext } from 'src/pages/table-context';
import { Alert } from '../forms/ui/alert';


const TABLE_STATE_DYNAMIC = 'DYNAMIC';

// Конфигурации таблиц.
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
    history: {
        label: 'History',
        entity: 'history',
        columns: historyColumns,
        fields: historyFields,
    },
};


const TableSwitcher = () => {
    // Выбранная табилица.
    const [selectedTable, setSelectedTable] = useState('workers');
    const [{ entity, columns, fields, }, setTableEntity] = useState(tablesConfig[selectedTable]);
    // Модальные окна.
    const [isModalOpen, setIsModalOpen] = useState(false);
    // Модальные окна.
    const [isModalAddctionActionOpen, setIsModalAddctionActionOpen] = useState(false);
    // выбранное действие.
    const [selectedAction, setSelectedAction] = useState('');
    // Данный сущностей из таблицы.
    const [entities, setEntities] = useState([]);
    // Состояине таблицы (либо постоянное обновление раз в интерфал, либо статические данные).
    const [tableState, setTableState] = useState(TABLE_STATE_DYNAMIC);
    // Данные из формы с дополнительным действием.
    const [addictionActionData, setAddictionActionData] = useState({});
    // Данные с формой для создания сущностей.
    const [formNewEntity, setFormNewEntity] = useState({});
    // loader  показывает статус запроса.
    const [loading, setLoading] = useState(false);
    // message  показывает статус запроса.
    const [addictionActionMessage, setAddictionActionMessage] = useState("");
    // данные для установки в контекст
    const { storageEntities, setStorageEntities } = useTableContext()

    useEffect(
        () => { setTableEntity(tablesConfig[selectedTable]); setEntities([]); }, [selectedTable]
    );

    // Функция для сравнения двух сущностей по полям 
    const areEntitiesEqual = (entity1, entity2) => { return Object.keys(entity1).every(key => entity1[key] === entity2[key]); };

    useEffect(() => {
        // Сопоставление id существующих и новых сущностей 
        const existingIds = new Set(storageEntities.map(e => e.id));
        const newIds = new Set(entities.map(e => e.id));

        // Проверка на совпадение id и различия в полях
        const hasDifferences = storageEntities.length !== entities.length ||
            entities.some(newEntity => {
                const existingEntity = storageEntities.find(e => e.id === newEntity.id);
                return !existingEntity || !areEntitiesEqual(newEntity, existingEntity);
            });
        if (hasDifferences) {
            setStorageEntities(entities);
        }

    }, [entities]
    );

    // Функция для загрузки данных с сервера.
    const fetchEntities = useCallback(async () => {
        try {
            const response = await axios.get(`/${entity}`)
            setEntities(response.data);
            return response.data
        } catch (error) {
            console.error('Ошибка при загрузке данных:', error)
        }
    }, [entity])

    useEffect(() => {
        if (tableState === TABLE_STATE_DYNAMIC) {
            const interval = setInterval(fetchEntities, 500)
            return () => clearInterval(interval)
        }
    }, [tableState, fetchEntities])


    // Вызывает при изменнии текущей таблицы.
    const handleTableChange = (event) => {
        setSelectedTable(event.target.value);
        setFormNewEntity({}); // Очистка формы при смене таблицы.
    };

    // Вызываетмя при создании новой сущьности.
    const handleCreateEntity = async () => {
        try {
            setLoading(true);
            const response = await axios.post(`/${entity}`, formNewEntity);
            setLoading(false);
            handleCloseModal();
        } catch (error) {
            console.error('Ошибка при создании сущности:', error);
            setLoading(false);
        }
    };
    //START-----ДОПОЛЬНИТЕЛЬНЫЕ ДЕЙСТВИЯ-----// 
    const isActionValid = () => {
        if (selectedAction === 'delete' && addictionActionData.endDate) return true;
        if (selectedAction === 'sumRating') return true;
        if (selectedAction === 'indexSalary' && addictionActionData.workerId && addictionActionData.salaryFactor) return true;
        if (selectedAction === 'importFile') return true;
        return false;
    };

    const handleExecuteAction = async () => {
        if (selectedAction === 'delete') {
            handleDeleteByEndDate();
        } else if (selectedAction === 'sumRating') {
            handleSumRating();
        } else if (selectedAction === 'indexSalary') {
            handleIndexSalary();
        } else if (selectedAction === 'importFile') {
            onFileUpload();
        }
    };

    const [selectedFile, setSelectedFile] = useState(null);
    const [message, setMessage] = useState('');

    const onFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const onFileUpload = async () => {
        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            const response = await axios.post('/uploader/import', formData, { headers: { 'Content-Type': 'multipart/form-data' } });

            const result = await response.text();
            setMessage(result);
        } catch (error) {
            setMessage('Ошибка при загрузке файла');
        }
    };


    // Удаление объектов с заданным endDate.
    const handleDeleteByEndDate = async () => {
        const fetchingEntities = await fetchEntities();
        try {
            const toDelete = fetchingEntities.filter(item => dateFormat(item.endDate) === dateFormat(addictionActionData.endDate));
            for (let item of toDelete) {
                await axios.delete(`/${entity}/${item.id}`);
            }
            fetchEntities();
        } catch (error) {
            console.error('Ошибка при удалении:', error);
        }
    };

    function sumArray(arr) {
        return arr.reduce((sum, current) => sum + current, 0);
    }

    // Фильтрация данных по endDate больше заданной даты
    const handleSumRating = async () => {
        const fetchingEntities = await fetchEntities();
        const filtered = fetchingEntities.map(item => item.rating);
        const res = sumArray(filtered)
        setAddictionActionMessage(`Суммарный рейтинг работников ${res}`)
        console.log(filtered, fetchingEntities, addictionActionData.endDate)
    };

    // Индексация заработной платы на заданный коэффициент
    const handleIndexSalary = async () => {
        const fetchingEntities = await fetchEntities();
        try {
            const worker = fetchingEntities.find(item => item.id == addictionActionData.workerId);
            if (worker) {
                const updatedWorker = { ...worker, salary: worker.salary * addictionActionData.salaryFactor };
                let respData = await axios.put(`/${entity}/${addictionActionData.workerId}`, updatedWorker);
            }
        } catch (error) {
            console.error('Ошибка при индексации зарплаты:', error);
        }
    };

    const handleActionFieldChange = (key, value) => {
        setAddictionActionData({ ...addictionActionData, [key]: value });
    };

    const handleActionChange = (event) => {
        setSelectedAction(event.target.value);
        setAddictionActionData({});
        setAddictionActionMessage("")
    };
    //END-----ДОПОЛЬНИТЕЛЬНЫЕ ДЕЙСТВИЯ-----// 


    //START-----МОДАЛЬНЫЕ ОКНА-----//
    // Открытие и закрытие модального окна
    const handleOpenModal = () => setIsModalOpen(true);
    const handleCloseModal = () => {
        setIsModalOpen(false);
        setFormNewEntity({});
    };
    // Открытие и закрытие модального окна
    const handleOpenModalAddictionAction = () => setIsModalAddctionActionOpen(true);
    const handleCloseModalAddictionAction = () => {
        setIsModalAddctionActionOpen(false);
        setAddictionActionData({});
    };

    const handleInputChange = (key, value) => {
        setFormNewEntity({ ...formNewEntity, [key]: value });
    };
    //END-----МОДАЛЬНЫЕ ОКНА-----//

    //START-----ИЗМЕНЕНИЯ В EDITABLETABLE-----//
    // Обработчик для обновления строки
    const onUpdate = async (entityID, editedData) => {
        try {
            await axios.put(`/${entity}/${entityID}`, editedData);
            fetchEntities()
        } catch (error) {
            console.error('Ошибка при обновлении данных:', error);
        }
    };

    // Обработчик для удаления строки
    const onDelete = async (entityID) => {
        try {
            await axios.delete(`/${entity}/${entityID}`);
            fetchEntities();
        } catch (error) {
            console.error('Ошибка при удалении данных:', error);
        }
    };
    //END-----ИЗМЕНЕНИЯ В EDITABLETABLE-----//

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
                onChange={handleTableChange}
                sx={{ marginBottom: '20px', minWidth: '200px' }}
            >
                {Object.keys(tablesConfig).map((key) => (
                    <MenuItem key={key} value={key}>
                        {tablesConfig[key].label}
                    </MenuItem>
                ))}
            </Select>

            <EditableTable columns={columns} fields={fields} onDelete={onDelete} onUpdate={onUpdate} />

            <Dialog open={isModalAddctionActionOpen} onClose={handleCloseModalAddictionAction}>
                <DialogTitle>Дополнительные действия</DialogTitle>
                <DialogContent>
                    {addictionActionMessage !== "" && (<>
                        <InputLabel>Сообщение</InputLabel>
                        <Alert className="mb-4" status="success">{addictionActionMessage}</Alert>
                    </>
                    )}
                    <FormControl fullWidth margin="dense">
                        <InputLabel>Выберите действие</InputLabel>
                        <Select
                            value={selectedAction}
                            onChange={handleActionChange}
                            label="Выберите действие"
                            fullWidth
                        >
                            <MenuItem value="delete">Удалить по End Date</MenuItem>
                            <MenuItem value="sumRating">Сумма рейтингов всех работников</MenuItem>
                            <MenuItem value="indexSalary">Индексация зарплаты</MenuItem>
                            <MenuItem value="importFile">загрузка файла</MenuItem>
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
                    {/* Поля для действия "Фильтровать по End Date"*/}
                    {selectedAction === 'importFile' && (
                        <Input type="file" onChange={onFileChange} />
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
                                value={formNewEntity[field.key] || ''}
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
                                value={field.type === 'datetime-local' ? dateFormat(formNewEntity[field.key]) : formNewEntity[field.key] || ''}
                                onChange={(e) => handleInputChange(field.key, e.target.value)}
                                fullWidth
                                margin="dense"
                            />
                        )
                    ))}
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseModal}>Отмена</Button>
                    <Button onClick={handleCreateEntity} disabled={loading}>
                        {loading ? 'Создание...' : 'Создать'}
                    </Button>
                </DialogActions>
            </Dialog>
        </Box >
    );
}


export default TableSwitcher;