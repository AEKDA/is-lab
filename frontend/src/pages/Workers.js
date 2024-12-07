import React, { useEffect } from 'react';
import TableSwitcher from '../components/worker/TableSwitcher';
import { TableProvider } from './table-context';

function Workers() {

  return (
    <div >
      <TableProvider>
      <TableSwitcher />
      </TableProvider>
    </div>
  );
}

export default Workers;
