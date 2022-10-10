import * as React from 'react';
import * as ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider, Route} from "react-router-dom";

import App from './routes/App';
import ErrorPage from './error-page';
import Account from './routes/Account';

import "./index.css";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    errorElement: <ErrorPage />,
  },
  {
    path: "/account",
    element: <Account />
  },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>
);


// import App from './components/homepage/App'
// import './index.css'
// import { TransactionProvider } from './context/TransactionContext';

// ReactDOM.createRoot(document.getElementById('root')).render(  
//   <TransactionProvider>
//     <React.StrictMode>
//     <App />
//   </React.StrictMode>
//   </TransactionProvider>,
//)
