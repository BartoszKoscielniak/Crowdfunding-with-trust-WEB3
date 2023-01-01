import * as React from 'react';
import * as ReactDOM from 'react-dom/client';
import {createBrowserRouter, RouterProvider, Route} from "react-router-dom";

import App from './routes/App';
import ErrorPage from './error-page';
import Account from './routes/Account';
import Collections from './routes/Collections';
import Funds from './routes/Funds';
import Earn from './routes/Earn';

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
  {
    path: "/collections",
    element: <Collections />
  },
  {
    path: "/funds",
    element: <Funds />
  },
  {
    path: "/earn",
    element: <Earn />
  },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router}/>
  </React.StrictMode>
);
