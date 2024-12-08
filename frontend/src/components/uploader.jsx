import React, { useState } from 'react';

const FileUpload = () => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [message, setMessage] = useState('');

    const onFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const onFileUpload = async () => {
        const formData = new FormData();
        formData.append('file', selectedFile);

        try {
            const response = await fetch('/api/import/upload', {
                method: 'POST',
                body: formData,
            });

            const result = await response.text();
            setMessage(result);
        } catch (error) {
            setMessage('Ошибка при загрузке файла');
        }
    };

    return (
        <div>
            <input type="file" onChange={onFileChange} />
            <button onClick={onFileUpload}>Загрузить файл</button>
            <p>{message}</p>
        </div>
    );
};

export default FileUpload;
