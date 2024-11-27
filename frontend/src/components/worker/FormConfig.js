export const workerFields = [
    { label: 'Name', key: 'name' },
    { label: 'Salary', key: 'salary', type: 'number' },
    { label: 'Rating', key: 'rating', type: 'number' },
    { label: 'Position', key: 'position', type: 'enum', options: ['HUMAN_RESOURCES', 'HEAD_OF_DEPARTMENT', 'CLEANER'] },
    { label: 'Start Date', key: 'startDate', type: 'datetime-local' },
    { label: 'End Date', key: 'endDate', type: 'datetime-local' },
    { label: 'Creation Date', key: 'creationDate', type: 'datetime-local' },
    { label: 'Organization ID', key: 'organizationId', type: 'number' },
    { label: 'Person ID', key: 'personId', type: 'number' },
    { label: 'Coordinates ID', key: 'coordinatesId', type: 'number' }
];

export const organizationFields = [
    { label: 'Full Name', key: 'fullName' },
    { label: 'Annual Turnover', key: 'annualTurnover', type: 'number' },
    { label: 'Employers Count', key: 'employeesCount', type: 'number' },
    { label: 'Type', key: 'type', type: 'enum', options: ['COMMERCIAL', 'PUBLIC', 'GOVERNMENT', 'TRUST'] },
    { label: 'Official Address ID', key: 'addressId', type: 'number' }
];
export const personFields = [
    { label: 'Eye Color', key: 'eyeColor', type: 'enum', options: ['RED', 'ORANGE', 'WHITE', 'BROWN'] },
    { label: 'Hair Color', key: 'hairColor', type: 'enum', options: ['RED', 'ORANGE', 'WHITE', 'BROWN'] },
    { label: 'Weight', key: 'weight', type: 'number' },
    { label: 'Location ID', key: 'locationId' },
];
export const addressFields = [
    { key: 'zipCode', label: 'ZipCode' },
];

export const coordinatesFields = [
    { key: 'y', label: 'X', type: 'number' },
    { key: 'x', label: 'Y', type: 'number' },
];

export const locationFields = [
    { key: 'name', label: 'Name' },
    { key: 'y', label: 'X', type: 'number' },
    { key: 'x', label: 'Y', type: 'number' },
];
