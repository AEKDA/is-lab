import React, { createContext, useState, useContext } from 'react'

const TableContext = createContext()

export const TableProvider = ({ children }) => {
    const [storageEntities, setStorageEntities] = useState([])

    return (
        <TableContext.Provider value={{ storageEntities, setStorageEntities }}>
            {children}
        </TableContext.Provider>
    )
}

export const useTableContext = () => {
    const context = useContext(TableContext)
    if (context === undefined) {
        throw new Error('useTableContext must be used within a TableProvider')
    }
    return context
}

